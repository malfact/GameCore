package net.malfact.gamecore.event;

import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class RegisterGlobalLibraryEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<LuaLib> libs;
    private final Map<Class<?>, TypeHandler<?>> handlers;

    public RegisterGlobalLibraryEvent(List<LuaLib> libs, Map<Class<?>, TypeHandler<?>> handlers) {
        this.libs = libs;
        this.handlers = handlers;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public void registerLib(LuaLib lib) {
        libs.add(lib);
    }

    public <T> void registerTypeHandler(TypeHandler<T> handler, Class<T> typeClass) {
        handlers.put(typeClass, handler);
    }
}
