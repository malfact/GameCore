package net.malfact.gamecore.api;

import net.malfact.gamecore.game.Game;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public interface TypeHandler<T> {

    default LuaValue getUserdataOf(T object) {
        return LuaConstant.NIL;
    }

    default LuaValue getUserdataOf(T object, Game instance) {
        return getUserdataOf(object);
    }
}
