package net.malfact.gamecore.api;

import net.malfact.gamecore.api.entity.PlayerLib;
import net.malfact.gamecore.api.event.EventLib;
import net.malfact.gamecore.game.ScriptedGame;
import net.malfact.gamecore.script.Instance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class GameLib extends EventLib {
    public GameLib(Instance instance) {
        super(instance, "game");
    }

    @Override
    protected LuaValue createLib(LuaValue moduleName, LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("getPlayers", new Game_getPlayers());
        lib.set("getEntities", new Game_getEntities());

        env.set("Game", lib);

        return lib;
    }

    @Override
    protected LuaValue onIndex(LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        ScriptedGame game = getGame();
        String k = key.tojstring();
        return switch (k) {
            case "time" -> valueOf(game.time());
            case "name" -> valueOf(game.getName());
            case "displayName" -> valueOf(game.getDisplayName());
            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected void onNewIndex(LuaValue key, LuaValue value) {
        if (!key.isstring())
            return;

        if (key.tojstring().equals("displayName")) {
            instance.getGame().setDisplayName(value.checkjstring());
        }
    }

    public ScriptedGame getGame() {
        return instance.getGame();
    }

    private class Game_getPlayers extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            Player[] players = instance.getGame().getPlayers();

            LuaTable table = new LuaTable();
            int i = 1;
            for (var player : players) {
                table.set(i++, PlayerLib.userdataOf(player));
            }

            return table;
        }
    }

    private class Game_getEntities extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            Entity[] players = instance.getGame().getEntities();

            LuaTable table = new LuaTable();
            int i = 1;
            for (var player : players) {
                table.set(i++, PlayerLib.userdataOf(player));
            }

            return table;
        }
    }

    private class GameGetPlayer extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            if (!arg2.isuserdata(Player.class))
                return LuaConstant.NIL;

            Player player = arg2.touserdata(Player.class);

            if (instance.getGame().hasPlayer(player))
                instance.getUserdataOf(player);

            return null;
        }
    }
}
