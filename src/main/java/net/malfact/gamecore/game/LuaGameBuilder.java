package net.malfact.gamecore.game;

import net.malfact.gamecore.script.LuaScript;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.coercion.JavaOnly;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class LuaGameBuilder {

    private final List<LuaGameCallback> callbacks;
    private final List<LuaGameCallback> listeners;

    private final String name;
    private final LuaScript script;
    private String displayName = "";

    @JavaOnly
    public LuaGameBuilder(LuaScript script) {
        this.name = script.getName();
        this.script = script;
        this.callbacks = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void registerCallback(String eventTypeName, LuaFunction callback) {
        callbacks.add(new LuaGameCallback(eventTypeName, callback));
    }

    public void registerListener(String eventName, LuaFunction listener) {
        listeners.add(new LuaGameCallback(eventName, listener));
    }

    @JavaOnly
    public Game build() {
        if (displayName == null || displayName.isEmpty())
            displayName = name;

        if (script != null) {
            LuaGame luaGame = new LuaGame(script, displayName);
            for (LuaGameCallback callback : callbacks) {
                List<LuaFunction> list = luaGame.callbacks.computeIfAbsent(callback.eventType, k -> new ArrayList<>());
                list.add(callback.callback);
            }
            for (LuaGameCallback listener : listeners) {
                if (luaGame.listeners.containsKey(listener.eventType))
                    continue;

                luaGame.registerListener(listener.eventType, listener.callback);
            }
            return luaGame;
        }

        return null;
    }

    private record LuaGameCallback(String eventType, LuaFunction callback) {}
}