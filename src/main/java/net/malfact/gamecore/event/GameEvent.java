package net.malfact.gamecore.event;

import net.malfact.gamecore.game.Game;
import org.bukkit.event.Event;

@SuppressWarnings("unused")
public abstract class GameEvent extends Event {

    protected final Game game;

    public GameEvent(final Game game) {
        this.game = game;
    }

    public final Game getGame() {
        return this.game;
    }
}
