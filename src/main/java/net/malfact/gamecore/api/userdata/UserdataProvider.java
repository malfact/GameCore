package net.malfact.gamecore.api.userdata;

import org.luaj.vm2.LuaValue;

public interface UserdataProvider {

    boolean accepts(Object o);

    LuaValue getUserdataOf(Object o);
}
