package net.malfact.gamecore.game.player;

import net.malfact.gamecore.game.Game;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class InstancedPlayerProxy extends PlayerProxy {

    private final UUID uuid;

    private Game game;
    private Player player;
    private String name;

    private InvalidReason invalidReason;
    private Instant disconnectInstant;

    public InstancedPlayerProxy(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        invalidReason = InvalidReason.VALID;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public boolean isValid() {
        return isTracked() && player != null && player.isOnline();
    }

    @Override
    public boolean isTracked() {
        return invalidReason != InvalidReason.RELEASED;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean isOnline() {
        if (invalidReason == InvalidReason.OFFLINE)
            return false;

        return player != null ? player.isOnline() : null;
    }

    @Override
    public Boolean isDead() {
        return player != null ? player.isDead() : null;
    }

    @Override
    public long getTimeOffline() {
        if (isOnline() || disconnectInstant == null)
            return 0;

        return Duration.between(disconnectInstant, Instant.now()).toSeconds();
    }

    @Override
    public void invalidate(InvalidReason reason) {
        if (!isTracked() || reason == InvalidReason.VALID)
            return;

        switch (reason) {
            case OFFLINE:
                disconnectInstant = Instant.now();
                break;
            case RELEASED:
                disconnectInstant = null;
                game = null;
                break;
            default:
                return;
        }

        this.player = null;
        this.invalidReason = reason;
    }

    @Override
    public void revalidate(Player player) {
        if (!isTracked() || player == null)
            return;

        this.player = player;
        this.name = player.getName();
        this.invalidReason = InvalidReason.VALID;
        this.disconnectInstant = null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InstancedPlayerProxy other && this.uuid.equals(other.uuid);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
