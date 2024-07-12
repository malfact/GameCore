package net.malfact.gamecore.player;

import com.google.gson.reflect.TypeToken;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameCoreManager;
import net.malfact.gamecore.event.player.PlayerJoinTeamEvent;
import net.malfact.gamecore.event.player.PlayerLeaveTeamEvent;
import net.malfact.gamecore.team.GameTeam;
import net.malfact.gamecore.util.Json;
import net.malfact.gamecore.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class PlayerManager extends GameCoreManager implements Listener {

    private final Map<UUID, QueuedPlayer> players;
    private final Map<UUID, BukkitTask> cleaningTasks;
    private static final Type dataType = new TypeToken<PlayerData>() {}.getType();

    public PlayerManager(GameCore plugin) {
        super(plugin);

        players = new HashMap<>();
        cleaningTasks = new HashMap<>();
    }

    public QueuedPlayer getPlayer(@NotNull Player player) {
        if (players == null || players.isEmpty())
            return null;

        return players.get(player.getUniqueId());
    }

    /**
     * Trys to load a player's data file from storage, returning an optional of the PlayerData
     */
    private Optional<PlayerData> loadPlayerData(UUID uuid) {
        return Optional.ofNullable(Json.read(plugin, "data/players/" + uuid, dataType));
    }

    /**
     * Saves a player's data file to storage
     */
    private void savePlayerData(UUID uuid, PlayerData playerData) {
        Json.write(plugin, "data/players/" + uuid, playerData);
    }

    /**
     * Deletes a player's data file from storage
     */
    private void deletePlayerData(UUID uuid) {
        Json.delete(plugin, "data/players/" + uuid);
    }

    // Currently nothing will save since they get removed from teams & queues
    // Will probably add option to NOT remove offline players
    private void cleanPlayer(QueuedPlayer player) {
        plugin.logInfo("Running Data-Clean on " + player);
        if (player.inSystem()) {
            String queueName = player.getQueueName();
            String teamName = player.getTeamName();

            if (!queueName.isEmpty()) {
                plugin.logInfo("Removing " + player + " from Queue <" + queueName + ">");
                GameCore.getQueueManager().getQueue(queueName).removePlayer(player);
            }

            if (!teamName.isEmpty()) {
                plugin.logInfo("Removing " + player + " from Team <" + teamName + ">");
                GameCore.getTeamManager().getTeam(teamName).removePlayer(player);
            }
        }

        /* TODO: Figure out logic for not clearing cache if it brings you to an exit-point
         *       Otherwise THEN reset it when 'spawn-on-join' is true
         */

        // Clears cached teleport if spawn-on-join is true
        if (shouldPlayerSpawnOnJoin(player)) {
            player.clearCachedTeleport();
        }

        PlayerData playerData = player.getPlayerData();
        if (playerData.hasSavableData())
            savePlayerData(player.getUniqueId(), playerData);
        else
            deletePlayerData(player.getUniqueId());
    }

    @Override
    public void clean() {
        plugin.logInfo("Canceling " + cleaningTasks.size() + " Data-Clean Tasks");
        cleaningTasks.values().forEach(BukkitTask::cancel);

        players.values().forEach(this::cleanPlayer);
    }

    // Note: For whatever reason this would always fire before PlayerJoinedEvent.
    // So I am just running the data loading here instead...
    @EventHandler
    private void onPlayerSpawnLocation(@NotNull PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        QueuedPlayer gamePlayer;
        if (!players.containsKey(uuid)) {
            Optional<PlayerData> playerData = loadPlayerData(uuid);

            gamePlayer = playerData.map(
                data -> new QueuedPlayer(player, data)
            ).orElseGet(
                () -> new QueuedPlayer(player)
            );

            plugin.logInfo("Loaded Player Data for " + gamePlayer);

            players.put(uuid, gamePlayer);
        } else {
            // Rebind player to existing object
            gamePlayer = players.get(uuid);
            gamePlayer.bindPlayer(player);

            plugin.logInfo("Using active Player Data for " + gamePlayer);

            // cancel the cleaning task
            cleaningTasks.get(uuid).cancel();
            plugin.logInfo("Canceled Data-Clean for " + gamePlayer);
        }

        gamePlayer.setOnline(true);

        // Runs cached teleports
        if (gamePlayer.hasCachedTeleport()) {
            plugin.logInfo("Player " + player + " has cached teleports. Changing Spawn Location");
            event.setSpawnLocation(gamePlayer.getCachedTeleport());
            gamePlayer.clearCachedTeleport();
        } else if (shouldPlayerSpawnOnJoin(gamePlayer)){
            event.setSpawnLocation(Bukkit.getWorlds().getFirst().getSpawnLocation());
        }
    }

    @EventHandler
    private void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        QueuedPlayer gamePlayer = players.get(event.getPlayer().getUniqueId());

        if (gamePlayer == null)
            return;

        gamePlayer.setOnline(false);

        // Gets the config settings for cleaning time
        String cleanSetting = "cleaning.player-cleaning-delay." +
            (gamePlayer.inSystem() ? "in-system." : "out-system.") +
            quitReasonToConfig(event.getReason());

        int cleanTimer = plugin.getConfig().getInt(cleanSetting, 60);

        plugin.logInfo("Scheduling Data-Clean for " + gamePlayer + " in " + cleanTimer + " seconds");

        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(
            plugin,
            () -> cleanPlayer(gamePlayer),
            cleanTimer * 20L
        );

        cleaningTasks.put(gamePlayer.getUniqueId(), task);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerRespawn(@NotNull PlayerRespawnEvent event) {
        QueuedPlayer player = Validate.isGamePlayer(event.getPlayer());

        player.setDead(false);

        // Runs cached teleports
        if (player.hasCachedTeleport()) {
            event.setRespawnLocation(player.getCachedTeleport());
            player.clearCachedTeleport();
        }

        GameTeam team = player.getTeam();

        // Respawn players at Team's spawn if player is in team and the spawn exists
        if (team != null && team.getSpawn() != null)
            event.setRespawnLocation(team.getSpawn());
    }

    @EventHandler
    private void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        QueuedPlayer player = Validate.isGamePlayer(event.getPlayer());
        player.setDead(true);

        GameTeam team = player.getTeam();

        // Remove player from their Team if flag is set
        if (team != null && team.getLeaveOnDeath()) {
            team.removePlayer(player);
        }
    }

    @EventHandler
    private void onPlayerJoinTeam(@NotNull PlayerJoinTeamEvent event) {
        QueuedPlayer player = event.getPlayer();
        GameTeam team = event.getTeam();

        if (team.getSpawn() != null)
            player.teleport(team.getSpawn(), true);
    }

    @EventHandler
    private void onPlayerLeaveTeam(@NotNull PlayerLeaveTeamEvent event) {
        QueuedPlayer player = event.getPlayer();
        GameTeam team = event.getTeam();

        if (team.getExit() != null)
            player.teleport(team.getExit(), true);
    }

    private String quitReasonToConfig(PlayerQuitEvent.QuitReason reason) {
        return switch (reason) {
            case DISCONNECTED -> "disconnected";
            case KICKED -> "kicked";
            case TIMED_OUT -> "timed-out";
            default -> "other";
        };
    }

    private boolean shouldPlayerSpawnOnJoin(QueuedPlayer player) {
        return plugin.getConfig().getBoolean("spawn-on-join",false) && !player.hasPermission("gamecore.ignore-spawn-on-join");
    }
}
