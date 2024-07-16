package net.malfact.gamecore.event.player;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.PlayerProxy;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.event.player.PlayerQuitEvent.QuitReason;

public class PlayerDisconnectEvent extends PlayerGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final QuitReason quitReason;

    public PlayerDisconnectEvent(Game game, PlayerProxy player, QuitReason quitReason) {
        super(game, player);
        this.quitReason = quitReason;
    }

    public QuitReason getQuitReason() {
        return quitReason;
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
