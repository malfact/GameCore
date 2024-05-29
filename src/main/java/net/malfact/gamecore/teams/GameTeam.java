package net.malfact.gamecore.teams;

import net.malfact.gamecore.players.GamePlayer;
import net.malfact.gamecore.util.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * A wrapper for {@link Team Minecraft Teams} with extra functionality.
 * Only accepts players.
 */
public class GameTeam implements DataHolder<TeamData> {
    public final TeamManager teamManager;
    public final String name;
    private final List<GamePlayer> players;
    private Team handle;
    private Instant handleTime;

    private Location spawn;
    private Location exit;

    GameTeam(final String name, final TeamManager teamManager) {
        this.teamManager = teamManager;
        this.name = name;

        players = new ArrayList<>();
    }

    GameTeam(final TeamData data, final TeamManager teamManager) {
        this(data.name, teamManager);

        this.spawn = data.spawn;
        this.exit = data.exit;
    }

    void clean() {
        empty();

        try {
            if (handle != null) {
                handle.unregister();
                handle = null;
                handleTime = null;
            }
        } catch (IllegalStateException ignored) {}
    }

    void refreshHandle() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(name);
        if (team == null)
            team = scoreboard.registerNewTeam(name);

        handle = team;
        handleTime = Instant.now();
    }

    /**
     * Get the underling minecraft scoreboard Team of this GameTeam
     * @return the minecraft scoreboard Team
     */
    public Team handle() {
        if (handle == null || handleTime == null)
            refreshHandle();

        if (ChronoUnit.SECONDS.between(Instant.now(), handleTime) >= 30)
            refreshHandle();

        return handle;
    }

    /**
     * Get the GamePlayers currently in this Queue
     * @return a set of GamePlayers in this Queue
     */
    public Set<GamePlayer> getPlayers() {
        return new HashSet<>(players);
    }

    /**
     * Adds a GamePlayer to the Team
     * @param player the GamePlayer to add
     * @return <i>true</i> if the player was added successfully, false otherwise
     */
    public boolean addPlayer(@NotNull GamePlayer player) {
        if (players.contains(player))
            return false;

        String oldTeam = teamManager.getPlayerTeam(player.uuid);
        if (oldTeam != null) {
            teamManager.getTeam(oldTeam).silentRemovePlayer(player);
        }
        teamManager.setPlayerTeam(player.uuid, this.name);

        players.add(player);

        handle().addPlayer(player.handle());

        if (spawn != null)
            player.teleport(spawn);

        return true;
    }

    public void addPlayers(@NotNull List<GamePlayer> gamePlayers) {
        List<Player> players = gamePlayers.stream().map(GamePlayer::handle).toList();
        handle().addEntities(new HashSet<>(players));

        for (GamePlayer gamePlayer : gamePlayers) {
            this.players.add(gamePlayer);

            String oldTeam = teamManager.getPlayerTeam(gamePlayer.uuid);
            if (oldTeam != null) {
                teamManager.getTeam(oldTeam).silentRemovePlayer(gamePlayer);
            }
            teamManager.setPlayerTeam(gamePlayer.uuid, this.name);

            if (spawn != null)
                gamePlayer.teleport(spawn);
        }
    }

    private void silentRemovePlayer(@NotNull GamePlayer player) {
        try {
            handle().removePlayer(player.handle());
        } catch (IllegalStateException ignored) {}

        players.remove(player);
    }

    private void silentRemovePlayers(@NotNull List<GamePlayer> players) {
        List<Player> playerList = players.stream().map(GamePlayer::handle).toList();

        try {
            handle().removeEntities(new HashSet<>(playerList));
        } catch (IllegalStateException ignored) {}

        for (GamePlayer gamePlayer : players) {
            this.players.remove(gamePlayer);
        }
    }

    /**
     * Removes a GamePlayer from the Team
     * @param player the GamePlayer to remove
     * @return <i>true</i> if the player was removed successfully, false otherwise
     */
    public boolean removePlayer(@NotNull GamePlayer player) {
        if (players.isEmpty() || !players.contains(player))
            return false;

        teamManager.setPlayerTeam(player.uuid, null);

        try {
            handle().removePlayer(player.handle());
        } catch (IllegalStateException ignored) {}

        players.remove(player);
        if (exit != null)
            player.teleport(exit);

        return true;
    }

    public void removePlayers(@NotNull List<GamePlayer> gamePlayers) {
        List<Player> players = gamePlayers.stream().map(GamePlayer::handle).toList();

        try {
            handle().removeEntities(new HashSet<>(players));
        } catch (IllegalStateException ignored) {}

        for (GamePlayer gamePlayer : gamePlayers) {
            if (!this.players.contains(gamePlayer))
                continue;

            teamManager.setPlayerTeam(gamePlayer.uuid, null);

            this.players.remove(gamePlayer);
            if (exit != null)
                gamePlayer.teleport(exit);
        }
    }

    public void empty() {
        if (players.isEmpty())
            return;

        try {
            handle().removeEntries(handle().getEntries());
        } catch (IllegalStateException ignored) {}

        Iterator<GamePlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            GamePlayer p = iterator.next();
            teamManager.setPlayerTeam(p.uuid, null);
            p.teleport(exit);

            iterator.remove();
        }
    }

    /**
     * Get the spawn location for this Team
     * @return the spawn location
     */
    public Location getSpawn() {
        return spawn == null ? null : spawn.clone();
    }

    /**
     * Get the exit location for this Team
     * @return the exit location
     */
    public Location getExit() {
        return exit == null ? null : exit.clone();
    }

    /**
     * Set the spawn location for this Team
     * @param location the location to set spawn to
     */
    public void setSpawn(Location location) {
        System.out.println("SETTING SPAWN " + location);
        this.spawn = location == null ? null : location.clone();
    }

    /**
     * Set the exit location for this Team
     * @param location the location to set exit to
     */
    public void setExit(Location location) {
        System.out.println("SETTING EXIT" + location);
        this.exit = location == null ? null : location.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameTeam team)) return false;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public TeamData getDataObject() {
        return new TeamData(name, spawn, exit);
    }

    @Override
    public void setDataObject(TeamData data) {
        this.spawn = data.spawn;
        this.exit = data.exit;
    }
}
