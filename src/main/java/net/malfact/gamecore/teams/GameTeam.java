package net.malfact.gamecore.teams;

import net.malfact.gamecore.event.PlayerJoinTeamEvent;
import net.malfact.gamecore.event.PlayerLeaveTeamEvent;
import net.malfact.gamecore.players.GamePlayer;
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
    private final List<GamePlayer> players;
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
    public Set<GamePlayer> getPlayers() {
        return new HashSet<>(players);
    }

    /**
     * Adds a GamePlayer to the Team
     *
     * @param player the GamePlayer to add
     */
    public void addPlayer(@NotNull GamePlayer player) {
        if (players.contains(player))
            return;

        String oldTeam = teamManager.getPlayerTeam(player.uuid);
        if (oldTeam != null) {
            teamManager.getTeam(oldTeam).silentRemovePlayer(player);
        }
        teamManager.setPlayerTeam(player.uuid, this.name);

        players.add(player);
        new PlayerJoinTeamEvent(player, this).callEvent();

        handle().addPlayer(player.handle());
    }


    public void silentRemovePlayer(@NotNull GamePlayer player) {
        try {
            handle().removePlayer(player.handle());
        } catch (IllegalStateException ignored) {}

        players.remove(player);
    }


    /**
     * Removes a GamePlayer from the Team
     * @param player the GamePlayer to remove
     */
    public void removePlayer(@NotNull GamePlayer player) {
        if (players.isEmpty() || !players.contains(player))
            return;

        teamManager.setPlayerTeam(player.uuid, null);

        new PlayerLeaveTeamEvent(player, this).callEvent();

        try {
            handle().removePlayer(player.handle());
        } catch (IllegalStateException ignored) {}

        players.remove(player);
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
            new PlayerLeaveTeamEvent(p, this).callEvent();

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
    public boolean hasPlayer(GamePlayer player) {
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
