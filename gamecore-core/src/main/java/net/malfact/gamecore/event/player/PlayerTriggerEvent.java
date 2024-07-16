package net.malfact.gamecore.event.player;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.PlayerProxy;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerTriggerEvent extends PlayerGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String trigger;

    public PlayerTriggerEvent(Game game, PlayerProxy player, String trigger) {
        super(game, player);
        this.trigger = trigger;
    }

    public String getTrigger() {
        return trigger;
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
