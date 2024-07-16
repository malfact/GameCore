package net.malfact.gamecore.event.player;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.PlayerProxy;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerLeaveGameEvent extends PlayerGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerLeaveGameEvent(Game game, PlayerProxy player) {
        super(game, player);
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
