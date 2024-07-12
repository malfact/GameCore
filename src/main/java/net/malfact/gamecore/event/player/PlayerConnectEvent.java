package net.malfact.gamecore.event.player;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.PlayerProxy;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerConnectEvent extends PlayerGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Location spawnLocation;

    public PlayerConnectEvent(Game game, PlayerProxy player, Location spawnLocation) {
        super(
            Objects.requireNonNull(game),
            Objects.requireNonNull(player)
        );
        this.spawnLocation = Objects.requireNonNull(spawnLocation);
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = Objects.requireNonNull(location);
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
