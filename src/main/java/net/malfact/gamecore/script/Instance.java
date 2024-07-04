package net.malfact.gamecore.script;

import net.malfact.gamecore.api.AttributeLib;
import net.malfact.gamecore.api.event.EventUserdata;
import net.malfact.gamecore.api.inventory.meta.ItemMetaLib;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.game.ScriptedGame;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public abstract class Instance {

    public LuaValue getValueOf(Object obj) {
        if (obj == null)
            return LuaConstant.NIL;

        switch (obj) {
            case LuaValue v:            return v;
            case Boolean v:             return LuaValue.valueOf(v);
            case Integer v:             return LuaValue.valueOf(v);
            case Number v:              return LuaValue.valueOf(v.doubleValue());
            case byte[] v:              return LuaValue.valueOf(v);
            case String v:              return LuaValue.valueOf(v);
            default: break;
        }

        var typeLib = ScriptManager.getTypeLib(obj.getClass());
        if (typeLib != null)
            return typeLib.getUserdataOf(obj);

        var userdata = getUserdataOf(obj);
        if (!userdata.isnil())
            return userdata;

        if (obj instanceof Enum<?> v)
            return LuaValue.valueOf(v.toString().toLowerCase(Locale.ROOT));

        return LuaConstant.NIL;
    }

    public abstract LuaValue getUserdataOf(Object obj);

    public abstract ScriptedGame getGame();

    public abstract LuaScript getScript();

    public abstract boolean isGlobal();

    protected List<UserdataProvider> providers = null;

    final void setProviders(List<UserdataProvider> providers) {
        this.providers = providers;
    }

    void clean() {
        providers = null;
    }

    void setGame(ScriptedGame game) {}

    void setScript(LuaScript script) {}

    static class GlobalInstance extends Instance {

        @Override
        public LuaValue getUserdataOf(Object obj) {
            if (obj == null)
                return LuaConstant.NIL;

            var typeLib = ScriptManager.getTypeLib(obj.getClass());
            if (typeLib != null)
                return typeLib.getUserdataOf(obj);

            switch (obj) {
                case ItemMeta v -> {
                    return ItemMetaLib.userdataOf(v);
                }
                case AttributeInstance v -> {
                    return AttributeLib.userdataOf(v);
                }
                case AttributeModifier v -> {
                    return AttributeLib.userdataOf(v);
                }
                default -> {}
            }

            if (providers != null) {
                for (var provider : providers) {
                    if (provider.accepts(obj))
                        return provider.getUserdataOf(obj);
                }
            }

            return LuaConstant.NIL;
        }

        @Override
        public ScriptedGame getGame() {
            return null;
        }

        @Override
        public LuaScript getScript() {
            return null;
        }

        @Override
        public boolean isGlobal() {
            return true;
        }
    }

    static class LocalInstance extends Instance {

        private final Instance global;
        private ScriptedGame game;
        private LuaScript script;

        public LocalInstance(Instance global) {
            this.global = global;
            this.game = game;
        }

        @Override
        public LuaValue getUserdataOf(Object obj) {
            if (obj == null)
                return LuaConstant.NIL;

            if (obj instanceof Event e)
                return EventUserdata.of(e, this);

            if (providers != null) {
                for (var provider : providers) {
                    if (provider.accepts(obj))
                        return provider.getUserdataOf(obj);
                }
            }

            return global.getUserdataOf(obj);
        }

        @Override
        public ScriptedGame getGame() {
            return game;
        }

        @Override
        public LuaScript getScript() {
            return script;
        }

        @Override
        public boolean isGlobal() {
            return false;
        }

        @Override
        void setGame(ScriptedGame game) {
            this.game = game;
        }

        @Override
        public void setScript(LuaScript script) {
            this.script = script;
        }
    }
}
