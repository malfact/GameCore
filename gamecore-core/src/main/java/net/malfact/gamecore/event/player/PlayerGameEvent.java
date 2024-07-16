package net.malfact.gamecore.event.player;

import net.malfact.gamecore.event.GameEvent;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.PlayerProxy;

public abstract class PlayerGameEvent extends GameEvent {

    protected final PlayerProxy player;

    public PlayerGameEvent(Game game, PlayerProxy player) {
        super(game);
        this.player = player;
    }

    public final PlayerProxy getPlayer() {
        return player;
    }
}
