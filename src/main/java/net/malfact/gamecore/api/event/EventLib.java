package net.malfact.gamecore.api.event;

import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.game.ScriptedGame;
import net.malfact.gamecore.script.Instance;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.Objects;

/**
 * Parent class for libraries that can return events from the {@link EventRegistry}.
 * <p>
 *     {@code EventLib}s are instanced to each game, therefore require a {@link ScriptedGame Game}
 *     object be passed on construction.
 * </p>
 */
public abstract class EventLib extends InstancedLib {

    /** The event type */
    protected final String eventType;

    /**
     * @param instance the instance
     * @param eventType the event type
     * @see EventRegistry
     */
    public EventLib(Instance instance, String eventType) {
        super(instance);
        this.eventType = Objects.requireNonNull(eventType);
    }

    /**
     * {@code createLib} should add the library to the environment itself. <br>
     * The library must be returned so that proper event indexing can happen.
     * @see #onIndex(LuaValue) 
     * @see #onNewIndex(LuaValue, LuaValue) 
     */
    protected abstract LuaValue createLib(LuaValue moduleName, LuaValue env);

    /**
     * Metatable metamethod {@code __index}
     * @param key the key
     * @return the value, if any
     * @see #onNewIndex(LuaValue, LuaValue) 
     */
    @Nullable
    protected LuaValue onIndex(LuaValue key) {
        return LuaConstant.NIL;
    }

    /**
     * Metatable metamethod {@code __newindex}
     * @param key the key
     * @param value the value to set
     * @see #onIndex(LuaValue)
     */
    protected void onNewIndex(LuaValue key, LuaValue value) {}

    @Override
    public final LuaValue call(LuaValue moduleName, LuaValue env) {
        LuaValue lib = createLib(moduleName, env);
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX, new LibIndex());
        meta.set(LuaConstant.MetaTag.NEWINDEX, new LibNewIndex());
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        lib.setmetatable(meta);
        return lib;
    }

    protected LuaValue getUserdataOf(EventRegistry.EventEntry entry) {
        if (entry == null)
            return LuaConstant.NIL;
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.CALL, new Event__call(instance.getGame()));
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(entry, meta);
    }

    private class LibIndex extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue tbl, LuaValue key) {
            LuaValue value = onIndex(key);

            if (value != null && !value.isnil())
                return value;

            if (!key.isstring())
                return LuaConstant.NIL;

            return getUserdataOf(EventRegistry.getEvent(eventType, key.tojstring()));
        }
    }

    private class LibNewIndex extends ThreeArgFunction {

        @Override
        public LuaValue call(LuaValue tbl, LuaValue key, LuaValue value) {
            onNewIndex(key, value);
            return LuaConstant.NIL;
        }
    }

    // Event.<NAME>(function(event) ... end)
    private static class Event__call extends VarArgFunction {

        private final ScriptedGame game;

        private Event__call(final ScriptedGame game) {
            this.game = game;
        }

        @Override
        public Varargs invoke(Varargs args) {
            var event = args.arg1().checkuserdata(EventRegistry.EventEntry.class);
            event.registerListener(game.getFunctionCallback(args.arg(3).checkfunction()));
            return LuaConstant.NIL;
        }
    }
}
