package net.malfact.gamecore.lua;

import net.malfact.gamecore.GameCore;
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

    private final LuaFunction func_getPlayers =     LuaUtil.toFunction(this::getPlayers);
    private final LuaFunction func_getEntities =    LuaUtil.toFunction(this::getEntities);
    private final LuaFunction func_joinGame =       LuaUtil.toFunction(this::joinGame);
    private final LuaFunction func_leaveGame =      LuaUtil.toFunction(this::leaveGame);
    private final LuaFunction func_isPlaying =      LuaUtil.toFunction(this::isPlaying);
    private final LuaFunction func_stop =           LuaUtil.toFunction(this::stopGame);

    public GameLib(Game instance) {
        super(instance, "game");
    }

    @Override
    protected LuaValue createLib(LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("getPlayers",   func_getPlayers);
        lib.set("getEntities",  func_getEntities);
        lib.set("joinGame",     func_joinGame);
        lib.set("leaveGame",    func_leaveGame);
        lib.set("isPlaying",    func_isPlaying);
        lib.set("stop",         func_stop);

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

    private LuaValue getPlayers() {
        Player[] players = instance.getPlayers();

        LuaTable table = new LuaTable();
        int i = 1;
        for (var player : players) {
            table.set(i++, LuaApi.userdataOf(player, instance));
        }

        return table;
    }

    private LuaValue getEntities() {
        Entity[] players = instance.getEntities();

        LuaTable table = new LuaTable();
        int i = 1;
        for (var player : players) {
            table.set(i++, LuaApi.userdataOf(player, instance));
        }

        return table;
    }

    private LuaValue joinGame(LuaValue args) {
        Player player = args.arg1().checkuserdata(Player.class);
        Game game = GameCore.gameManager().getGame(player);

        if (game != null)
            return LuaConstant.FALSE;

        var joined = GameCore.gameManager().joinGame(player, instance);

        return joined ? LuaApi.userdataOf(player, instance) : LuaConstant.FALSE;
    }

    private LuaValue leaveGame(LuaValue arg) {
        Player player = arg.checkuserdata(Player.class);
        Game game = GameCore.gameManager().getGame(player);
        if (game == null)
            return LuaConstant.FALSE;

        if (!game.getName().equals(instance.getName()))
            return LuaConstant.FALSE;

        return LuaValue.valueOf(GameCore.gameManager().leaveGame(player));
    }

    private LuaValue isPlaying(LuaValue arg) {
        Player player = arg.checkuserdata(Player.class);
        Game game = GameCore.gameManager().getGame(player);

        if (game == null)
            return LuaConstant.FALSE;

        return LuaApi.valueOf(game.getName().equals(instance.getName()));
    }

    private void stopGame() {
        instance.stop();
    }
}
