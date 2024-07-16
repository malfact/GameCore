package net.malfact.gamecore.event.library;

import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegisterLocalLibraryEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Game instance;
    private final List<LuaLib> libs;

    public RegisterLocalLibraryEvent(Game instance, List<LuaLib> libs) {
        this.instance = instance;
        this.libs = libs;
    }

    public Game getInstance() {
        return instance;
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
}
