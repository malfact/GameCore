package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.Vector3Lib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.luaj.vm2.*;

public class WorldLib extends InstancedLib {

    private final LuaFunction func_getBlockData = LuaUtil.toVarargFunction(this::getBlockData);
    private final LuaFunction func_setBlockData = LuaUtil.toVarargFunction(this::setBlockData);
    private final LuaFunction func_getBlockType = LuaUtil.toVarargFunction(this::getBlockType);
    private final LuaFunction func_setBlockType = LuaUtil.toVarargFunction(this::setBlockType);
    private final LuaFunction func_playSound =    LuaUtil.toVarargFunction(this::playSound);
    private final LuaFunction func_strikeLightning = LuaUtil.toFunction(this::strikeLightning);
    private final LuaFunction func_strikeLightningEffect = LuaUtil.toFunction(this::strikeLightningEffect);

    public WorldLib(Game instance) {
        super(instance);
    }

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("setBlockData", func_getBlockData);
        lib.set("setBlockType", func_setBlockData);
        lib.set("getBlockType", func_getBlockType);
        lib.set("getBlockType", func_setBlockType);
        lib.set("playSound",    func_playSound);
        lib.set("strikeLightning", func_strikeLightning);
        lib.set("strikeLightningEffect", func_strikeLightningEffect);

        env.set("World", lib);
    }

    private static Location getLocation(LuaValue value) {
        if (value.isuserdata(Location.class)) {
            return value.touserdata(Location.class);
        } else if (value.isuserdata(Vector3.class)) {
            return Vector3Lib.toLocation(value.touserdata(Vector3.class), Bukkit.getWorlds().getFirst());
        }

        return null;
    }

    private Varargs setBlockData(Varargs args) {
        if (!instance.isRunning()) {
            GameCore.logger().warn("{} attempted to call setBlockData before running!", instance.getName());
            return LuaConstant.NIL;
        }

        Location location = getLocation(args.arg1());
        if (location == null)
            return LuaConstant.NIL; // instance.warn("Location expected, got " +  args.arg1().getType() + ")");

        World world = location.getWorld();

        String data = args.checkjstring(2);
        BlockData blockData;
        try {
            blockData = Bukkit.createBlockData(data);
        } catch (IllegalArgumentException ignored) {
            return LuaConstant.NIL; // instance.warn("Invalid Block Data");
        }

        instance.registerBlockChange(location);
        world.setBlockData(location, blockData);

        return LuaConstant.NIL;
    }

    private Varargs setBlockType(Varargs args) {
        if (!instance.isRunning()) {
            GameCore.logger().warn("{} attempted to call setBlockType before running!", instance.getName());
            return LuaConstant.NIL;
        }

        Location location = getLocation(args.arg1());
        if (location == null)
            return LuaConstant.NIL; // instance.warn("Location expected, got " +  args.arg1().getType() + ")");

        World world = location.getWorld();

        instance.registerBlockChange(location);
        world.setType(location, LuaUtil.checkMaterial(args.arg(2)));

        return LuaConstant.NIL;
    }

    private Varargs getBlockData(Varargs args) {
        Location location = getLocation(args.arg1());
        if (location == null)
            return LuaConstant.NIL; // instance.warn("Location expected, got " +  args.arg1().getType() + ")");
        World world = location.getWorld();
        BlockData data = world.getBlockData(location);
        return LuaValue.varargsOf(
            LuaApi.userdataOf(data.getMaterial()),
            LuaApi.userdataOf(data.getPlacementMaterial()),
            LuaApi.valueOf(data.getAsString())
        );
    }

    private Varargs getBlockType(Varargs args) {
        Location location = getLocation(args.arg1());
        if (location == null)
            return LuaConstant.NIL; // instance.warn("Location expected, got " +  args.arg1().getType() + ")");
        World world = location.getWorld();
        return LuaApi.valueOf(world.getType(location));
    }

    private void playSound(Varargs args) {
        World world;
        Location location = LuaUtil.toLocation(args.arg(1));
        if (location == null) {
            var entity = args.arg1().touserdata(Entity.class);
            if (entity == null)
                LuaUtil.argumentError(1, "Location or Entity", args.arg1());

            location = entity.getLocation();
        }

        world = location.getWorld();

        var sound = LuaUtil.toSound(args.arg(2));
        if (sound == null)
            LuaUtil.argumentError(2, "Sound", args.arg(2));

        float volume = (float) args.optdouble(3, 1);
        float pitch = (float) args.optdouble(4, 1);

        world.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    private void strikeLightning(LuaValue arg1) {
        Location loc = LuaUtil.checkLocation(arg1);
        loc.getWorld().strikeLightning(loc);
    }

    private void strikeLightningEffect(LuaValue arg1) {
        Location loc = LuaUtil.checkLocation(arg1);
        loc.getWorld().strikeLightningEffect(loc);
    }
}
