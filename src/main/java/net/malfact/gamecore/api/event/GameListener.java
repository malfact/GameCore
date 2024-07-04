package net.malfact.gamecore.api.event;

import net.malfact.gamecore.script.Instance;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class GameListener implements Listener, EventExecutor {

    protected final Instance instance;

    public GameListener(Instance instance) {
        this.instance = instance;
    }

    public abstract void onEvent(Event event);

    @Override
    public final void execute(@NotNull Listener listener, @NotNull Event event) {
        if (instance.getGame().acceptsEvent(event))
            onEvent(event);
    }
}
