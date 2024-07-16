package net.malfact.gamecore.game.player;

import net.malfact.gamecore.game.Game;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InvalidPlayerProxy extends PlayerProxy {

    public static final PlayerProxy INSTANCE = new InvalidPlayerProxy();

    private InvalidPlayerProxy(){}

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isTracked() {
        return false;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Boolean isOnline() {
        return null;
    }

    @Override
    public Boolean isDead() {
        return null;
    }

    @Override
    public long getTimeOffline() {
        return 0;
    }

    @Override
    public void invalidate(InvalidReason reason) {}

    @Override
    public void revalidate(Player player) {}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InvalidPlayerProxy;
    }

    @Override
    public String toString() {
        return "<INVALID_PLAYER>";
    }
}
