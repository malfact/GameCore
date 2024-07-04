package net.malfact.gamecore.api;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.script.Instance;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class DataStoreLib extends InstancedLib {

    public DataStoreLib(Instance instance) {
        super(instance);
    }

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("getPlayerData", getPlayerData);
        lib.set("setPlayerData", setPlayerData);
        lib.set("clearPlayerData", clearPlayerData);
        lib.set("getGameData", getGameData);
        lib.set("setGameData", setGameData);
        lib.set("clearGameData", clearGameData);

        env.set("DataStore", lib);
        return env;
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

    private final LuaFunction getPlayerData = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Player player = arg1.checkuserdata(Player.class);
            String key = arg2.checkjstring();
            var data = GameCore.getDataManager().getPlayerData(player, key);
            return instance.getValueOf(data);
        }
    };

    private final LuaFunction setPlayerData = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            Player player = arg1.checkuserdata(Player.class);
            String key = arg2.checkjstring();
            Object data = unwrap(arg3);

            GameCore.getDataManager().setPlayerData(player, key, data);

            return LuaConstant.NIL;
        }
    };

    private final LuaFunction clearPlayerData = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Player player = arg1.checkuserdata(Player.class);
            String key = arg2.checkjstring();
            GameCore.getDataManager().clearPlayerData(player, key);
            return LuaConstant.NIL;
        }
    };

    private final LuaFunction getGameData = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue arg) {
            String key = arg.checkjstring();
            var data = GameCore.getDataManager().getGameData(instance.getGame(), key);
            return instance.getValueOf(data);
        }
    };

    private final LuaFunction setGameData = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            String key = arg1.checkjstring();
            Object data = unwrap(arg2);

            GameCore.getDataManager().setGameData(instance.getGame(), key, data);

            return LuaConstant.NIL;
        }
    };

    private final LuaFunction clearGameData = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue arg) {
            String key = arg.checkjstring();
            GameCore.getDataManager().clearGameData(instance.getGame(), key);
            return LuaConstant.NIL;
        }
    };

}
