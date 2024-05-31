package net.malfact.gamecore.players;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.teams.GameTeam;
import net.malfact.gamecore.util.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class GamePlayer implements DataHolder<PlayerData> {
    public final UUID uuid;
    private String name;
    private Player handle;
    private Instant handleTime;

    private String queue = "";
    private Location teleportLocation = null;

    GamePlayer(Player player) {
        uuid = player.getUniqueId();
        name = player.getName();
        handle = player;
        handleTime = Instant.now();
    }

    GamePlayer(Player player, PlayerData data) {
        this(player);
        teleportLocation = data.teleportLocation;
    }

    void refreshHandle() {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            handle = player;
            handleTime = Instant.now();
        }
    }

    /**
     * {@return The Player referenced by this GamePlayer}
     */
    public Player handle() {
        if (ChronoUnit.SECONDS.between(Instant.now(), handleTime) >= 30)
            refreshHandle();

        return handle;
    }

    public String getName() {
        return name;
    }

    /**
     * {@return <i>true</i> if the GamePlayer is online, otherwise <i>false</i>}
     */
    public boolean isOnline() {
        return exists() && this.handle().isOnline();
    }

    /**
     * {@return <i>true</i> if the GamePlayer is dead, otherwise <i>false</i>}
     */
    public boolean isDead() {
        return this.handle().isDead();
    }

    public boolean exists() {
        Player player = this.handle();
        return player != null;
    }

    /**
     * {@return the name of the current queue}
     */
    public String getQueue() {
        return this.queue;
    }

    /**
     * Teleports this Player to the provided location. <br>
     * <i>If the player is offline, they will teleport once they log in again.</i>
     * @param location the location to teleport the player to
     */
    public void teleport(Location location) {
        teleport(location, false);
    }

    /**
     * Teleports this Player to the provided location. <br>
     * If whenTeleportReady is set and the player is dead or offline, the location
     * will be saved and the player will be teleported when it is possible.
     * @param location The location to teleport the player to
     * @param whenTeleportReady If <i>true</i>, then the location will be saved until
     *                          the player can be teleported
     */
    public void teleport(Location location, boolean whenTeleportReady) {
        if (location == null)
            return;

        if (whenTeleportReady && (isDead() || !isOnline()))
            teleportLocation = location;
        else
            handle().teleport(location);
    }

    public boolean hasTeleportLocation() {
        return teleportLocation != null;
    }

    public Location getTeleportLocation() {
        return teleportLocation.clone();
    }

    public void setTeleportLocation(Location location) {
        this.teleportLocation = location;
    }

    /**
     * Sets the current queue name for this GamePlayer and notifies them.
     * Does not physically change their queues.
     *
     * @param queue the name of the queue
     * @see net.malfact.gamecore.queues.GameQueue#addPlayer(GamePlayer)  GameQueue.addPlayer(GamePlayer)
     */
    public void setQueue(String queue) {
        if (queue == null)
            queue = "";

        String lastQueue = this.queue;
        this.queue = queue;

        if (GameCore.tagPlayers) {
            if (!lastQueue.isEmpty())
                handle().removeScoreboardTag(GameCore.queuePrefix + lastQueue);

            if (!queue.isEmpty())
                handle().addScoreboardTag(GameCore.queuePrefix + queue);
        }

    }

    public @Nullable GameTeam getTeam() {
        return GameCore.getTeamManager().getTeam(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GamePlayer && this.uuid.equals(((GamePlayer) obj).uuid);
    }

    @Override
    public PlayerData getDataObject() {
        return new PlayerData(uuid, handle().getName(), teleportLocation);
    }

    @Override
    public void setDataObject(PlayerData data) {
        this.teleportLocation = data.teleportLocation;
    }
}
