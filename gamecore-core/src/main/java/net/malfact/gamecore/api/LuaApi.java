package net.malfact.gamecore.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.game.Game;
import org.bukkit.Keyed;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

import java.util.Locale;

public interface LuaApi {

    <T> TypeHandler<? super T> getTypeHandler(T obj);

    LuaValue getUserdataOf(Object obj);

    LuaValue getUserdataOf(Object obj, Game instance);

    // -----#----- Helper Methods -----#-----

    static LuaValue typeOf(LuaValue value) {
        if (!value.isuserdata())
            return valueOf(value.getType().typeName);

        LuaValue meta = value.getmetatable();
        if (meta == null || meta.isnil())
            return valueOf(value.getType().typeName);

        LuaValue type = meta.get("__userdata_type__");
        return type.isnil() ? LuaValue.valueOf(value.getType().typeName) : type;
    }

    // -# userdataOf

    static LuaValue userdataOf(Object o) {
        LuaValue value = GameCore.luaApi().getUserdataOf(o);
        return value.isnil() ? valueOf(o) : value;
    }

    static LuaValue userdataOf(Object o, Game instance) {
        LuaValue value = GameCore.luaApi().getUserdataOf(o, instance);
        return value.isnil() ? valueOf(o) : value;
    }

    // -# valueOf

    static LuaValue valueOf(boolean b) {
        return LuaValue.valueOf(b);
    }

    static LuaValue valueOf(int i) {
        return LuaValue.valueOf(i);
    }

    static LuaValue valueOf(double d) {
        return LuaValue.valueOf(d);
    }

    static LuaValue valueOf(char c) {
        return LuaValue.valueOf(c);
    }

    static LuaValue valueOf(float f) {
        return LuaValue.valueOf(f);
    }

    static LuaValue valueOf(byte[] bytes) {
        return LuaValue.valueOf(bytes);
    }

    static LuaValue valueOf(String s) {
        return LuaValue.valueOf(s);
    }

    // ----- ----- ----- ----- -----

    static LuaValue valueOf(Component c) {
        return LuaValue.valueOf(MiniMessage.miniMessage().serialize(c));
    }

    // ----- ----- ----- ----- -----

    static LuaValue valueOf(Object o) {
        if (o == null)
            return LuaConstant.NIL;

        if (o instanceof LuaValue v)
            return v;

        return switch (o) {
            case Boolean v ->   valueOf(v.booleanValue());
            case Integer v ->   valueOf(v.intValue());
            case Double v ->    valueOf(v.doubleValue());
            case Character v -> valueOf(v.charValue());
            case Float v ->     valueOf(v.floatValue());
            case Long v ->      valueOf(v.longValue());
            case byte[] v ->    valueOf(v);
            case String v ->    valueOf(v);
            case Component v -> valueOf(v);
            case Keyed v ->     LuaValue.valueOf(v.getKey().asMinimalString().toLowerCase(Locale.ROOT));
            case Enum<?> v ->   LuaValue.valueOf(v.toString().toLowerCase(Locale.ROOT));
            default ->          LuaConstant.NIL;
        };
    }

}
