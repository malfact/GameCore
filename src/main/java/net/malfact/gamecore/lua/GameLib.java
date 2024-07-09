package net.malfact.gamecore.lua;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.event.EventLib;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class GameLib extends EventLib {

    private final LuaFunction func_getPlayers = LuaUtil.toFunction(this::getPlayers);
    private final LuaFunction func_getEntities = LuaUtil.toFunction(this::getEntities);

    public GameLib(Game instance) {
        super(instance, "game");
    }

    @Override
    protected LuaValue createLib(LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("getPlayers",   func_getPlayers);
        lib.set("getEntities",  func_getEntities);

        env.set("Game", lib);

        return lib;
    }

    @Override
    protected LuaValue get(LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        String k = key.tojstring();
        return switch (k) {
            case "time" -> LuaValue.valueOf(instance.time());
            case "name" -> LuaValue.valueOf(instance.getName());
            case "displayName" -> LuaValue.valueOf(instance.getDisplayName());
            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected void set(LuaValue key, LuaValue value) {
        if (!key.isstring())
            return;

        if (key.tojstring().equals("displayName")) {
            instance.setDisplayName(value.checkjstring());
        }
    }

    private LuaValue getPlayers(LuaValue self) {
        Game instance = self.checkuserdata(Game.class);
        Player[] players = instance.getPlayers();

        LuaTable table = new LuaTable();
        int i = 1;
        for (var player : players) {
            table.set(i++, LuaApi.userdataOf(player, instance));
        }

        return table;
    }

    private LuaValue getEntities(LuaValue self) {
        Game instance = self.checkuserdata(Game.class);
        Entity[] players = instance.getEntities();

        LuaTable table = new LuaTable();
        int i = 1;
        for (var player : players) {
            table.set(i++, LuaApi.userdataOf(player, instance));
        }

        return table;
    }
}
