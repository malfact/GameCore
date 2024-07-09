package net.malfact.gamecore.api;

import net.malfact.gamecore.game.Game;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public interface TypeHandler<T> {

    Class<T> getTypeClass();

    default LuaValue getUserdataOf(T object) {
        return LuaConstant.NIL;
    }

    default LuaValue getUserdataOf(T object, Game instance) {
        return getUserdataOf(object);
    }

    default LuaValue getUserdataOfRaw(Object object) {
        return getUserdataOf(getTypeClass().cast(object));
    }

    default LuaValue getUserdataOfRaw(Object object, Game instance) {
        return getUserdataOf(getTypeClass().cast(object), instance);
    }
}
