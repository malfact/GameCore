package net.malfact.gamecore.lua;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class DataStoreLib extends InstancedLib {

    private final LuaFunction func_getPlayerData =      LuaUtil.toFunction(this::getPlayerData);
    private final LuaFunction func_setPlayerData =      LuaUtil.toFunction(this::setPlayerData);
    private final LuaFunction func_clearPlayerData =    LuaUtil.toFunction(this::clearPlayerData);
    private final LuaFunction func_getGameData =        LuaUtil.toFunction(this::getGameData);
    private final LuaFunction func_setGameData =        LuaUtil.toFunction(this::setGameData);
    private final LuaFunction func_clearGameData =      LuaUtil.toFunction(this::clearGameData);

    public DataStoreLib(Game instance) {
        super(instance);
    }

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("getPlayerData",    func_getPlayerData);
        lib.set("setPlayerData",    func_setPlayerData);
        lib.set("clearPlayerData",  func_clearPlayerData);
        lib.set("getGameData",      func_getGameData);
        lib.set("setGameData",      func_setGameData);
        lib.set("clearGameData",    func_clearGameData);

        env.set("DataStore", lib);
    }

    private LuaValue getPlayerData(LuaValue arg1, LuaValue arg2) {
        Player player = arg1.checkuserdata(Player.class);
        String key = arg2.checkjstring();
        var data = GameCore.getDataManager().getPlayerData(player, key);
        return LuaApi.valueOf(data);
    }

    private void setPlayerData(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        Player player = arg1.checkuserdata(Player.class);
        String key = arg2.checkjstring();
        Object data = unwrap(arg3);
        GameCore.getDataManager().setPlayerData(player, key, data);
    }

    private void clearPlayerData(LuaValue arg1, LuaValue arg2) {
        Player player = arg1.checkuserdata(Player.class);
        String key = arg2.checkjstring();
        GameCore.getDataManager().clearPlayerData(player, key);
    }

    private LuaValue getGameData(LuaValue arg) {
        String key = arg.checkjstring();
        var data = GameCore.getDataManager().getGameData(instance, key);
        return LuaApi.valueOf(data);
    }

    private void setGameData(LuaValue arg1, LuaValue arg2) {
        String key = arg1.checkjstring();
        Object data = unwrap(arg2);

        GameCore.getDataManager().setGameData(instance, key, data);
    }

    private void clearGameData(LuaValue arg) {
        String key = arg.checkjstring();
        GameCore.getDataManager().clearGameData(instance, key);
    }

    private static Object unwrap(LuaValue value) {
        if (value.isboolean())
            return value.toboolean();
        else if (value.isstring())
            return value.toString();
        else if (value.isint())
            return value.toint();
        else if (value.isnumber())
            return value.tolong();

        return null;
    }

}
