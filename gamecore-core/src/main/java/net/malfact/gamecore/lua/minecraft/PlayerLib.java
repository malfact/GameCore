package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.event.EventLib;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class PlayerLib extends EventLib {

    public PlayerLib(Game instance) {
        super(instance, "player");
    }

    // This just allows for Player.<EVENT>
    @Override
    protected LuaValue createLib(LuaValue env) {
        LuaTable lib = new LuaTable();

        env.set("Player", lib);

        return lib;
    }
}
