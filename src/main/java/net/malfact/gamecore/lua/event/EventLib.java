package net.malfact.gamecore.lua.event;

import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.ScriptedGame;
import org.luaj.vm2.*;

import java.util.Objects;

/**
 * Parent class for libraries that can return events from the {@link EventRegistry}.
 * <p>
 *     {@code EventLib}s are instanced to each game, therefore require a {@link ScriptedGame Game}
 *     object be passed on construction.
 * </p>
 */
public abstract class EventLib extends InstancedLib {

    private final LuaFunction func_index =      LuaUtil.toFunction(this::onIndex);
    private final LuaFunction func_newindex =   LuaUtil.toFunction(this::onNewIndex);
    private final LuaFunction func_call =       LuaUtil.toFunction(this::registerListener);
    private final LuaFunction func_tostring =   LuaUtil.toFunction(this::onToString);

    /** The event type */
    protected final String eventType;

    /**
     * @param eventType the event type
     * @see EventRegistry
     */
    public EventLib(Game instance, String eventType) {
        super(instance);
        this.eventType = Objects.requireNonNull(eventType);
    }

    /**
     * {@code createLib} should add the library to the environment itself. <br>
     * The library must be returned so that proper event indexing can happen.
     * @see #get(LuaValue)
     * @see #set(LuaValue, LuaValue)
     */
    protected abstract LuaValue createLib(LuaValue env);

    /**
     * Metatable metamethod {@code __index}
     *
     * @param key the key
     * @return the value, if any
     * @see #set(LuaValue, LuaValue)
     */
    protected LuaValue get(LuaValue key) {
        return LuaConstant.NIL;
    }

    /**
     * Metatable metamethod {@code __newindex}
     *
     * @param key   the key
     * @param value the value to set
     * @see #get(LuaValue)
     */
    protected void set(LuaValue key, LuaValue value) {}

    @Override
    public void load(LuaValue env) {
        LuaValue lib = createLib(env);
        LuaTable meta = new LuaTable();

        meta.set("instance", env.get("instance"));
        meta.set(LuaConstant.MetaTag.INDEX,     func_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX,  func_newindex);
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        lib.setmetatable(meta);
    }

    protected LuaValue getUserdataOf(EventRegistry.EventEntry entry) {
        if (entry == null)
            return LuaConstant.NIL;
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.CALL,      func_call);
        meta.set(LuaConstant.MetaTag.TOSTRING,  func_tostring);
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(entry, meta);
    }

    private LuaValue onIndex(LuaValue tbl, LuaValue key) {
        LuaValue value = get(key);

        if (value != null && !value.isnil())
            return value;

        if (!key.isstring())
            return LuaConstant.NIL;

        return getUserdataOf(EventRegistry.getEvent(eventType, key.tojstring()));
    }

    private void onNewIndex(LuaValue tbl, LuaValue key, LuaValue value) {
        set(key, value);
    }

    // Event.<NAME>(function(event) ... end)
    private void registerListener(LuaValue arg1, LuaValue arg2) {
        if (instance instanceof ScriptedGame g) {
            var event = arg1.checkuserdata(EventRegistry.EventEntry.class);
            event.registerListener(instance, g.getFunctionCallback(arg2.checkfunction()));
        }
    }

    private LuaValue onToString(LuaValue self) {
        return LuaValue.valueOf(self.checkuserdata(EventRegistry.EventEntry.class).name);
    }
}
