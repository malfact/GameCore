package net.malfact.gamecore.event.player;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.GamePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PlayerJoinGameEvent extends PlayerGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerJoinGameEvent(Game game, GamePlayer player) {
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
