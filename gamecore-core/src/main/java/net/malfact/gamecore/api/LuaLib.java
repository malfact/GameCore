package net.malfact.gamecore.api;

import org.luaj.vm2.LuaValue;

public interface LuaLib {

    void load(LuaValue env);
}
