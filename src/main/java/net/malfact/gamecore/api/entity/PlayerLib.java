package net.malfact.gamecore.api.entity;

import net.malfact.gamecore.api.event.EventLib;
import net.malfact.gamecore.script.Instance;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class PlayerLib extends EventLib {

    public PlayerLib(Instance instance) {
        super(instance, "player");
    }

    @Override
    protected LuaValue createLib(LuaValue moduleName, LuaValue env) {
        LuaTable lib = new LuaTable();

        env.set("Player", lib);

        return lib;
    }
}
