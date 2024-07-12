package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameCoreManager;
import net.malfact.gamecore.event.player.*;
import net.malfact.gamecore.game.player.InstancedPlayerProxy;
import net.malfact.gamecore.game.player.InvalidPlayerProxy;
import net.malfact.gamecore.game.player.PlayerProxy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.*;

public class GameManager extends GameCoreManager implements Listener {

    private final Map<String, Game> games;
    private final Map<UUID, PlayerProxy> players = new HashMap<>();

    public GameManager(GameCore plugin) {
        super(plugin);
        games = new HashMap<>();
    }

    public void registerGame(Game game) {
        if (games.containsKey(game.getName()))
            throw new IllegalArgumentException("Game " + game.getName() + " already registered");

        GameCore.logDebug("Registered Game: " + game.getName());
        games.put(game.getName(), game);
    }

    public void unregisterGame(String name) {
        var game = games.remove(name);
        if (game != null && game.isRunning()) {
            game.forceStop();
        }

        List<UUID> releases = new ArrayList<>();
        for (var player : players.values()) {
            if (game.equals(player.getGame()))
                releases.add(player.getUniqueId());
        }
        players.entrySet().removeIf(entry -> releases.contains(entry.getKey()));
    }

    public Game getGame(String game) {
        return games.get(game);
    }

    public Game getGame(UUID uuid) {
        for (var game : games.values()) {
            if (game.getUniqueId().equals(uuid))
                return game;
        }
        return null;
    }

    public Game getGame(Player player) {
        if (!players.containsKey(player.getUniqueId()))
            return null;

        return players.get(player.getUniqueId()).getGame();
    }

    public boolean joinGame(Player player, Game game) {
        if (players.containsKey(player.getUniqueId()))
            return false;

        if (game.getState() != Game.State.RUNNING)
            return false;

        PlayerProxy proxy = trackPlayer(player, game);
        if (!proxy.isValid())
            return false;

        players.put(player.getUniqueId(), proxy);

        Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(game, proxy));
        return true;
    }

    public boolean leaveGame(Player player) {
        PlayerProxy proxy = getPlayer(player.getUniqueId());
        if (!proxy.isTracked())
            return false;

        Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent(proxy.getGame(), proxy));

        releasePlayer(player);
        return true;
    }

    public PlayerProxy[] getPlayers(Game game) {
        if (game == null || !game.isActive())
            return new PlayerProxy[0];

        List<PlayerProxy> players = new ArrayList<>();
        for (var player : this.players.values()) {
            if (game.equals(player.getGame()))
                players.add(player);
        }

        return players.toArray(PlayerProxy[]::new);
    }

    /**
     * Stop all currently running games.<br>
     * Used for shutdown sequence.
     */
    public void stopGames() {
        if (games.isEmpty())
            return;

        for (Game game : games.values()) {
            game.stop();
            game.forceStop();
        }

        games.clear();
    }

    public String[] getGameNames() {
        return games.keySet().toArray(new String[0]);
    }

    public boolean isPlayerInGame(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public boolean isPlayerInGame(Player player, Game game) {
        if (!players.containsKey(player.getUniqueId()))
            return false;

        return game.equals(players.get(player.getUniqueId()).getGame());
    }

    void onGameStopped(Game game) {
        List<UUID> releases = new ArrayList<>();
        for (var player : players.values()) {
            if (game.equals(player.getGame())) {
                releases.add(player.getUniqueId());
            }
        }
        players.entrySet().removeIf(entry -> releases.contains(entry.getKey()));
    }

    /**
     * Gets a GamePlayer instance for a tracked player.
     * @param uuid the tracked player's uuid
     * @return the GamePlayer instance, or an invalid instance.
     */
    @NotNull
    public PlayerProxy getPlayer(UUID uuid) {
        return players.getOrDefault(uuid, InvalidPlayerProxy.INSTANCE);
    }

    @NotNull
    private PlayerProxy trackPlayer(Player player, Game game) {
        if (player == null || game == null || !player.isOnline() || !game.isActive())
            return InvalidPlayerProxy.INSTANCE;

        if (players.containsKey(player.getUniqueId())) {
            PlayerProxy proxy = players.get(player.getUniqueId());
            if (game.equals(proxy.getGame()))
                return proxy;

            GameCore.logger().warn("Attempt to register already registered Player {} to {}", player.getName(), game.getName());

            return InvalidPlayerProxy.INSTANCE;
        }

        PlayerProxy proxy = new InstancedPlayerProxy(player, game);
        players.put(player.getUniqueId(), proxy);
        GameCore.logger().debug("Started Tracking Player {}!", player.getName());
        return proxy;
    }

    private void releasePlayer(Player player) {
        if (player == null || !players.containsKey(player.getUniqueId()))
            return;

        PlayerProxy proxy = players.get(player.getUniqueId());
        proxy.invalidate(PlayerProxy.InvalidReason.RELEASED);

        GameCore.logger().debug("Stopped Tracking Player {}!", player.getName());
        players.remove(player.getUniqueId());
    }

    // Redelegates
    // Bukkit:PlayerJoinEvent to GameCore:PlayerSpawnEvent if the player is registered with a game.
    @EventHandler
    private void onPlayerJoined(@NotNull PlayerJoinEvent event) {
        PlayerProxy proxy = getPlayer(event.getPlayer().getUniqueId());
        if (!proxy.isTracked())
            return;

        Bukkit.getPluginManager().callEvent(new PlayerSpawnEvent(proxy.getGame(), proxy));
    }

    // Redelegates
    // Spigot:PlayerSpawnLocationEvent to GameCore:PlayerConnectEvent if the player is registered with a game.
    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerSpawnLocation(@NotNull PlayerSpawnLocationEvent event) {
        PlayerProxy proxy = getPlayer(event.getPlayer().getUniqueId());
        if (!proxy.isTracked())
            return;

        proxy.revalidate(event.getPlayer());

        var playerConnectEvent = new PlayerConnectEvent(proxy.getGame(), proxy, event.getSpawnLocation());
        Bukkit.getPluginManager().callEvent(playerConnectEvent);
        event.setSpawnLocation(event.getSpawnLocation());
    }

    // Redelegates
    // Bukkit:PlayerQuitEvent to GameCore:PlayerDisconnectEvent if the player is registered with a game.
    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        PlayerProxy proxy = getPlayer(event.getPlayer().getUniqueId());
        if (!proxy.isTracked()) {
            return;
        }

        proxy.invalidate(PlayerProxy.InvalidReason.OFFLINE);
        Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(proxy.getGame(), proxy, event.getReason()));
    }
}
