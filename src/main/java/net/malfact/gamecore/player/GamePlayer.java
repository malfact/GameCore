package net.malfact.gamecore.player;

import net.kyori.adventure.text.Component;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.team.GameTeam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class GamePlayer {
    private Player player;

    private String queue = "";
    private boolean online;

    private final PlayerData playerData;

    GamePlayer(Player player) {
        this(player, new PlayerData(player.getUniqueId(), player.getName()));
    }

    GamePlayer(Player player, PlayerData data) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(data);

        this.playerData = data;

        bindPlayer(player);
    }

    void bindPlayer(@NotNull Player player) {
        this.player = player;
    }

    PlayerData getPlayerData() {
        return playerData;
    }

    boolean hasCachedTeleport() {
        return playerData.teleportLocation != null;
    }

    Location getCachedTeleport() {
        return playerData.teleportLocation.clone();
    }

    void clearCachedTeleport() {
        this.playerData.teleportLocation = null;
    }

    void setOnline(boolean value) {
        this.online = value;
    }

    void setDead(boolean value) {
        this.playerData.dead = value;
    }

    boolean inSystem() {
        return !this.getTeamName().isEmpty() || !this.getQueueName().isEmpty();
    }

    /**
     * {@return the UUID of the player}
     */
    public UUID getUniqueId() {
        return playerData.uuid;
    }

    /**
     * {@return the name of the player}
     */
    public String getName() {
        return playerData.name;
    }

    /**
     * {@return <i>true</i> if the GamePlayer is online, otherwise <i>false</i>}
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * {@return <i>true</i> if the GamePlayer is dead, otherwise <i>false</i>}
     */
    public boolean isDead() {
        return this.playerData.dead;
    }

    /**
     * {@return the name of the current queue}
     */
    public String getQueueName() {
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
        if (location == null) {
            return;
        }

        location = location.clone();

        if (whenTeleportReady && (isDead() || !isOnline()))
            playerData.teleportLocation = location;
        else
            player.teleport(location);
    }

    public void teleportAsync(Location location, boolean whenTeleportReady) {
        if (location == null)
            return;

        location = location.clone();

        if (whenTeleportReady && (isDead() || !isOnline()))
            playerData.teleportLocation = location;
        else
            player.teleportAsync(location);
    }

    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * Sets the current queue name for this GamePlayer and notifies them.
     * Does not physically change their queues.
     *
     * @param queue the name of the queue
     * @see net.malfact.gamecore.queue.GameQueue#addPlayer(GamePlayer)  GameQueue.addPlayer(GamePlayer)
     */
    public void setQueue(String queue) {
        if (queue == null)
            queue = "";

        String lastQueue = this.queue;
        this.queue = queue;

        if (GameCore.tagPlayers) {
            if (!lastQueue.isEmpty())
                player.removeScoreboardTag(GameCore.queuePrefix + lastQueue);

            if (!queue.isEmpty())
                player.addScoreboardTag(GameCore.queuePrefix + queue);
        }

    }

    public @Nullable GameTeam getTeam() {
        return GameCore.getTeamManager().getTeam(this);
    }

    public String getTeamName() {
        GameTeam team = getTeam();
        return team == null ? "" : team.name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GamePlayer && this.playerData.uuid.equals(((GamePlayer) obj).playerData.uuid);
    }

    @Override
    public String toString() {
        return playerData.name + "@{" + playerData.uuid + "}";
    }
}
