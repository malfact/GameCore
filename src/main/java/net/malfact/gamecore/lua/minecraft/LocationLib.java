package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.lua.Vector3Lib;
import org.bukkit.Location;
import org.bukkit.World;
import org.luaj.vm2.*;

public class LocationLib implements LuaLib, TypeHandler<Location> {

    private static final LuaFunction func_new =         LuaUtil.toVarargFunction(LocationLib::newLocation);
    private static final LuaFunction LOC_INDEX =        LuaUtil.toFunction(LocationLib::locationIndex);
    private static final LuaFunction LOC_NEW_INDEX =    LuaUtil.toFunction(LocationLib::locationNewIndex);

    private static final LuaTable sharedLib = new LuaTable();

    static {
        sharedLib.set("clone",      LuaUtil.toFunction(LocationLib::locationClone));
        sharedLib.set("negate",     LuaUtil.toFunction(LocationLib::locationNegate));
        sharedLib.set("add",        LuaUtil.toFunction(LocationLib::locationAdd));
        sharedLib.set("sub",        LuaUtil.toFunction(LocationLib::locationSubtract));
        sharedLib.set("multiply",   LuaUtil.toFunction(LocationLib::locationMultiply));
        sharedLib.set("divide",     LuaUtil.toFunction(LocationLib::locationDivide));
        sharedLib.set("toHighest",  LuaUtil.toFunction(LocationLib::locationToHighest));
        sharedLib.set("length",     LuaUtil.toFunction(LocationLib::locationLength));
        sharedLib.set("squareLength", LuaUtil.toFunction(LocationLib::locationSquareLength));
        sharedLib.set("toString",   LuaUtil.toFunction(LocationLib::locationToString));
    }

    @Override
    public void load(LuaValue env) {
        LuaTable loc = new LuaTable();
        loc.set("new", func_new);

        // Copy all functions to library
        for (Varargs n = sharedLib.next(LuaConstant.NIL); !n.isnil(1); n = sharedLib.next(n.arg1())) {
            loc.set(n.arg(1), n.arg(2));
        }

        env.set("Location", loc);
    }

    // Location::new(world, vec)
    // Location::new(world, vec, yaw, pitch)
    // Location::new(world, x, y, z)
    // Location::new(world, x, y, z, yaw, pitch)
    private static Varargs newLocation(Varargs args) {
        return switch (args.narg()) {
            case 0, 1, 5 -> throw new LuaError("bad argument #" + (args.narg() + 1) + "( value expected)");
            case 2 -> userdataOf(createLocation(LuaUtil.toWorld(args.checkjstring(1)), args.arg(2).checkuserdata(Vector3.class)));
            case 4 -> args.isuserdata(2)
                ? userdataOf(createLocation(LuaUtil.toWorld(args.checkjstring(1)), args.arg(2).checkuserdata(Vector3.class), (float) args.checkdouble(3), (float) args.checkdouble(4)))
                : userdataOf(new Location(LuaUtil.toWorld(args.checkjstring(1)), args.checkdouble(2), args.checkdouble(3), args.checkdouble(4)));
            default -> userdataOf(new Location(LuaUtil.toWorld(args.checkjstring(1)), args.checkdouble(2), args.checkdouble(3), args.checkdouble(4), (float) args.checkdouble(5), (float) args.checkdouble(6)));
        };
    }

    private static Location createLocation(World world, Vector3 vec) {
        return new Location(world, vec.x, vec.y, vec.z);
    }

    private static Location createLocation(World world, Vector3 vec, float yaw, float pitch) {
        return new Location(world, vec.x, vec.y, vec.z, yaw, pitch);
    }

    private static LuaValue locationIndex(LuaValue arg1, LuaValue key) {
        Location loc = arg1.checkuserdata(Location.class);

        if (!key.isstring())
            return LuaConstant.NIL;

        return switch (key.tojstring()) {
            case "world" ->     loc.getWorld() == null ? LuaConstant.NIL : LuaValue.valueOf(loc.getWorld().getName());
            case "x" ->         LuaValue.valueOf(loc.x());
            case "y" ->         LuaValue.valueOf(loc.y());
            case "z" ->         LuaValue.valueOf(loc.z());
            case "blockX" ->    LuaValue.valueOf(loc.getBlockX());
            case "blockY" ->    LuaValue.valueOf(loc.getBlockY());
            case "blockZ" ->    LuaValue.valueOf(loc.getBlockZ());
            case "position" ->  Vector3Lib.userdataOf(loc.toVector());
            case "direction" -> Vector3Lib.userdataOf(loc.getDirection());
            case "yaw" ->       LuaValue.valueOf(loc.getYaw());
            case "pitch" ->     LuaValue.valueOf(loc.getPitch());
            default ->          sharedLib.get(key);
        };
    }

    private static void locationNewIndex(LuaValue arg1, LuaValue key, LuaValue value) {
        Location loc = LuaUtil.checkLocation(arg1);
        if (!key.isstring())
            return;

        switch (key.tojstring()) {
            case "position" -> {
                Vector3 vec = value.checkuserdata(Vector3.class);
                loc.set(vec.x, vec.y, vec.z);
            }
            case "direction" -> loc.setDirection(Vector3Lib.toVector(value.checkuserdata(Vector3.class)));
            case "yaw" -> loc.setYaw((float) value.checkdouble());
            case "pitch" -> loc.setPitch((float) value.checkdouble());
        }
    }

    // Location::clone(loc) -> new Location
    private static LuaValue locationClone(LuaValue arg) {
        return userdataOf(LuaUtil.checkLocation(arg).clone());
    }

    // Location::neg(loc) -> new location
    private static LuaValue locationNegate(LuaValue arg) {
        return userdataOf(LuaUtil.checkLocation(arg).clone().multiply(-1));
    }

    // Location::add(loc,vec) -> new location
    // Location::add(loc,loc) -> new location
    // Location::add(loc,scalar) -> new location
    private static LuaValue locationAdd(LuaValue arg1, LuaValue arg2) {
        Location loc = LuaUtil.checkLocation(arg1).clone();
        Object obj = arg2.checkuserdata();

        if (obj instanceof Vector3 vec)
            loc.add(vec.x, vec.y, vec.z);
        else if (obj instanceof Location loc2)
            loc.add(loc2);
        else {
            double s = arg2.checkdouble();
            loc.add(s, s, s);
        }

        return userdataOf(loc);
    }

    // Location::sub(loc,vec) -> new location
    // Location::sub(loc,loc) -> new location
    // Location::sub(loc,scalar) -> new location
    private static LuaValue locationSubtract(LuaValue arg1, LuaValue arg2) {
        Location loc = LuaUtil.checkLocation(arg1).clone();
        Object obj = arg2.checkuserdata();

        if (obj instanceof Vector3 vec)
            loc.subtract(vec.x, vec.y, vec.z);
        else if (obj instanceof Location loc2)
            loc.subtract(loc2);
        else {
            double s = arg2.checkdouble();
            loc.subtract(s, s, s);
        }

        return userdataOf(loc);
    }

    // Location::mul(loc,scalar) -> new location
    private static LuaValue locationMultiply(LuaValue arg1, LuaValue arg2) {
        return userdataOf(LuaUtil.checkLocation(arg1).clone().multiply(arg2.checkdouble()));
    }

    // Location::div(loc,scalar) -> new location
    private static LuaValue locationDivide(LuaValue arg1, LuaValue arg2) {
        return userdataOf(LuaUtil.checkLocation(arg1).clone().multiply(1.0/arg2.checkdouble()));
    }

    // Location::toHighest(loc) -> new location
    private static LuaValue locationToHighest(LuaValue arg) {
        return userdataOf(LuaUtil.checkLocation(arg).toHighestLocation());
    }

    // Location::len(loc) -> double
    private static LuaValue locationLength(LuaValue arg) {
        return LuaValue.valueOf(LuaUtil.checkLocation(arg).length());
    }

    // Location::len(loc) -> double
    private static LuaValue locationSquareLength(LuaValue arg) {
        return LuaValue.valueOf(LuaUtil.checkLocation(arg).lengthSquared());
    }


    // Location::toString(loc) -> string
    private static LuaValue locationToString(LuaValue arg) {
        Location loc = LuaUtil.checkLocation(arg);
        World world = loc.getWorld();
        return LuaValue.valueOf("<" + (world == null ? "NIL" : world.getName()) + "|<" + loc.x() + "," + loc.y() + ","
            + loc.z() + ">|[" + loc.getYaw() + "," + loc.getPitch() + "]>");
    }

    @Override
    public LuaValue getUserdataOf(Location location) {
        return userdataOf(location);
    }

    public static LuaValue userdataOf(Location location) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, LOC_INDEX);
        meta.set(LuaConstant.MetaTag.NEWINDEX, LOC_NEW_INDEX);
        meta.set(LuaConstant.MetaTag.TOSTRING, sharedLib.get("toString"));
        meta.set(LuaConstant.MetaTag.ADD, sharedLib.get("add"));
        meta.set(LuaConstant.MetaTag.SUB, sharedLib.get("sub"));
        meta.set(LuaConstant.MetaTag.MUL, sharedLib.get("mul"));
        meta.set(LuaConstant.MetaTag.DIV, sharedLib.get("div"));
        meta.set(LuaConstant.MetaTag.LEN, sharedLib.get("length"));
        meta.set(LuaConstant.MetaTag.UNM, sharedLib.get("neg"));

        meta.set("__userdata_type__", "location");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(location, meta);
    }
}