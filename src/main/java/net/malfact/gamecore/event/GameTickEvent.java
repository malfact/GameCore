package net.malfact.gamecore.event;

import net.malfact.gamecore.game.Game;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameTickEvent extends GameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public GameTickEvent(Game game) {
        super(game);
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
