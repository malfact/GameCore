package net.malfact.gamecore.lua.event;

import net.malfact.gamecore.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class EventListener implements Listener, EventExecutor {

    protected final Game instance;

    public EventListener(Game instance) {
        this.instance = instance;
    }

    public abstract void onEvent(Event event);

    @Override
    public final void execute(@NotNull Listener listener, @NotNull Event event) {
        if (instance.acceptsEvent(event))
            onEvent(event);
    }
}
