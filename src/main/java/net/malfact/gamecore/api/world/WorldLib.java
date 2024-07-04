package net.malfact.gamecore.api.world;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.game.ScriptedGame;
import net.malfact.gamecore.script.Instance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class WorldLib extends InstancedLib {

    public WorldLib(Instance instance) {
        super(instance);
    }

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("setBlockData", new World_setBlockData());
        lib.set("setBlockType", new World_setBlockType());
        lib.set("getBlockType", new World_getBlockType());

        env.set("World", lib);
        return env;
    }

    private static Varargs safeError(String message) {
        GameCore.logWarning("LuaError: " + message);
        return varargsOf(LuaConstant.FALSE, valueOf(message));
    }

    private static Location getLocation(LuaValue value) {
        if (value.isuserdata(Location.class)) {
            return value.touserdata(Location.class);
        } else if (value.isuserdata(Vector3.class)) {
            return Vector3Lib.toLocation(value.touserdata(Vector3.class), Bukkit.getWorlds().getFirst());
        }

        return null;
    }

    private class World_setBlockData extends VarArgFunction {

        @Override
        public Varargs invoke(Varargs args) {
            ScriptedGame game = instance.getGame();
            if (!game.isRunning()) {
                GameCore.logWarning(game.getName() + " attempted to call setBlockData before running!");
                return LuaConstant.NIL;
            }

            Location location = getLocation(args.arg1());
            if (location == null)
                return safeError("Location expected, got " +  args.arg1().getType() + ")");

            World world = location.getWorld();

            String data = args.checkjstring(2);
            BlockData blockData;
            try {
                blockData = Bukkit.createBlockData(data);
            } catch (IllegalArgumentException ignored) {
                return safeError("Invalid Block Data");
            }

            game.registerBlockChange(location);
            world.setBlockData(location, blockData);

            return LuaConstant.NIL;
        }
    }

    private class World_setBlockType extends VarArgFunction {

        @Override
        public Varargs invoke(Varargs args) {
            ScriptedGame game = instance.getGame();
            if (!game.isRunning()) {
                GameCore.logWarning(game.getName() + " attempted to call setBlockType before running!");
                return LuaConstant.NIL;
            }

            Location location = getLocation(args.arg1());
            if (location == null)
                return safeError("Location expected, got " +  args.arg1().getType() + ")");

            World world = location.getWorld();

            game.registerBlockChange(location);
            world.setType(location, LuaApi.checkMaterial(args.arg(2)));

            return LuaConstant.NIL;
        }
    }

    private class World_getBlockType extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Location location = getLocation(args.arg1());
            if (location == null)
                return safeError("Location expected, got " +  args.arg1().getType() + ")");
            World world = location.getWorld();
            return instance.getValueOf(world.getType(location));
        }
    }
}
