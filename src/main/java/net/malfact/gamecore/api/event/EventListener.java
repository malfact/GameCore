package net.malfact.gamecore.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EventListener<E extends Event> implements EventExecutor, Listener {

    public final Class<E> clazz;
    private final Consumer<E> consumer;

    public EventListener(Class<E> clazz, Consumer<E> consumer) {
        this.clazz = clazz;
        this.consumer = consumer;
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {

    }
}
