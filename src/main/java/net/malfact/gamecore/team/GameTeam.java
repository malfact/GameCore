package net.malfact.gamecore.team;

import net.malfact.gamecore.event.player.PlayerJoinTeamEvent;
import net.malfact.gamecore.event.player.PlayerLeaveTeamEvent;
import net.malfact.gamecore.player.QueuedPlayer;
import net.malfact.gamecore.util.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private final List<QueuedPlayer> players;
    private Team handle;
    private Instant handleTime;

    private Location spawn;
    private Location exit;
    private boolean leaveOnDeath;

    GameTeam(final String name, final TeamManager teamManager) {
        this.teamManager = teamManager;
        this.name = name;

        refreshHandle();

        players = new ArrayList<>();
    }

    GameTeam(final TeamData data, final TeamManager teamManager) {
        this(data.name, teamManager);

        this.setDataObject(data);
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
        if (handle == null || handleTime == null || handle.getScoreboard() == null)
            refreshHandle();

        if (ChronoUnit.SECONDS.between(Instant.now(), handleTime) >= 30)
            refreshHandle();

        return handle;
    }

    /**
     * Get the GamePlayers currently in this Queue
     * @return a set of GamePlayers in this Queue
     */
    public Set<QueuedPlayer> getPlayers() {
        return new HashSet<>(players);
    }

    /**
     * Adds a GamePlayer to the Team
     *
     * @param player the GamePlayer to add
     */
    public void addPlayer(@NotNull QueuedPlayer player) {
        if (players.contains(player))
            return;

        String oldTeam = teamManager.getPlayerTeam(player.getUniqueId());
        if (oldTeam != null) {
            teamManager.getTeam(oldTeam).players.remove(player);
        }
        teamManager.setPlayerTeam(player.getUniqueId(), this.name);

        players.add(player);
        new PlayerJoinTeamEvent(player, this).callEvent();
    }


    /**
     * Removes a GamePlayer from the Team
     * @param player the GamePlayer to remove
     */
    public void removePlayer(@NotNull QueuedPlayer player) {
        if (players.isEmpty() || !players.contains(player))
            return;

        teamManager.setPlayerTeam(player.getUniqueId(), null);

        new PlayerLeaveTeamEvent(player, this).callEvent();

        players.remove(player);
    }

    public void empty() {
        if (players.isEmpty())
            return;

        try {
            handle().removeEntries(handle().getEntries());
        } catch (IllegalStateException ignored) {}

        Iterator<QueuedPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            QueuedPlayer player = iterator.next();
            teamManager.setPlayerTeam(player.getUniqueId(), null);
            new PlayerLeaveTeamEvent(player, this).callEvent();

            iterator.remove();
        }
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Gets if the Team contains a player
     * @param player the player to check for
     * @return <i>true</i> if player is in team, <i>false</i> otherwise
     */
    public boolean hasPlayer(QueuedPlayer player) {
        if (players.isEmpty())
            return false;

        return players.contains(player);
    }

    /**
     * Get the spawn location for this Team
     * @return the spawn location
     */
    public Location getSpawn() {
        return spawn == null ? null : spawn.clone();
    }

    /**
     * Set the spawn location for this Team
     * @param location the location to set spawn to
     */
    public void setSpawn(Location location) {
        this.spawn = location == null ? null : location.clone();
    }

    /**
     * Get the exit location for this Team
     * @return the exit location
     */
    public Location getExit() {
        return exit == null ? null : exit.clone();
    }

    /**
     * Set the exit location for this Team
     * @param location the location to set exit to
     */
    public void setExit(Location location) {
        this.exit = location == null ? null : location.clone();
    }

    /**
     * {@return <i>true<i> if players should leave the Team on death, otherwise <i>false</i>}
     */
    public boolean getLeaveOnDeath() {
        return this.leaveOnDeath;
    }

    /**
     * Sets if players should leave the Team on death
     * @param value should players leave the team on death
     */
    public void setLeaveOnDeath(boolean value) {
        this.leaveOnDeath = value;
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
        return new TeamData(name, spawn, exit, leaveOnDeath);
    }

    @Override
    public void setDataObject(TeamData data) {
        this.spawn = data.spawn;
        this.exit = data.exit;
        this.leaveOnDeath = data.leaveOnDeath;
    }
}
