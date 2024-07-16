package net.malfact.gamecore.lua;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

public class EnvGlobalLib implements LuaLib {

    private static final LuaFunction func_typeOf = LuaUtil.toFunction(LuaApi::typeOf);

    @Override
    public void load(LuaValue env) {
        env.set("typeOf", func_typeOf);
    }
}
