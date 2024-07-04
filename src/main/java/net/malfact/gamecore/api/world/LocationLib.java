package net.malfact.gamecore.api.world;

import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.script.Instance;
import org.bukkit.Location;
import org.bukkit.World;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class LocationLib extends InstancedLib implements UserdataProvider {

    private final LuaFunction LOC_NEW = new Loc_new();
    private final LuaFunction LOC_INDEX = new Loc_index();
    private static final LuaFunction LOC_NEW_INDEX = new Loc_newindex();

    private final LuaTable FUNCTIONS = new LuaTable();

    public LocationLib(Instance instance) {
        super(instance);

        FUNCTIONS.set("clone", new Loc_clone());
        FUNCTIONS.set("negate", new Loc_neg());
        FUNCTIONS.set("add", new Loc_add());
        FUNCTIONS.set("sub", new Loc_sub());
        FUNCTIONS.set("mul", new Loc_mul());
        FUNCTIONS.set("div", new Loc_div());
        FUNCTIONS.set("toHighest", new Loc_toHighest());
        FUNCTIONS.set("length", new Loc_len());
        FUNCTIONS.set("toString", new Loc_tostring());
    }

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        LuaTable loc = new LuaTable();
        loc.set("new", LOC_NEW);

        // Copy all functions to library
        for (Varargs n = FUNCTIONS.next(LuaConstant.NIL); !n.isnil(1); n = FUNCTIONS.next(n.arg1())) {
            loc.set(n.arg(1), n.arg(2));
        }

        env.set("Location", loc);

        return env;
    }

    @Override
    public boolean accepts(Object o) {
        return o instanceof Location;
    }

    @Override
    public LuaValue getUserdataOf(Object o) {
        if (!accepts(o))
            return LuaConstant.NIL;

        Location location = (Location) o;

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, LOC_INDEX);
        meta.set(LuaConstant.MetaTag.NEWINDEX, LOC_NEW_INDEX);
        meta.set(LuaConstant.MetaTag.TOSTRING, FUNCTIONS.get("toString"));
        meta.set(LuaConstant.MetaTag.ADD, FUNCTIONS.get("add"));
        meta.set(LuaConstant.MetaTag.SUB, FUNCTIONS.get("sub"));
        meta.set(LuaConstant.MetaTag.MUL, FUNCTIONS.get("mul"));
        meta.set(LuaConstant.MetaTag.DIV, FUNCTIONS.get("div"));
        meta.set(LuaConstant.MetaTag.LEN, FUNCTIONS.get("length"));
        meta.set(LuaConstant.MetaTag.UNM, FUNCTIONS.get("neg"));

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(location, meta);
    }

    // Location::new(world, vec)
    // Location::new(world, vec, yaw, pitch)
    // Location::new(world, x, y, z)
    // Location::new(world, x, y, z, yaw, pitch)
    private class Loc_new extends VarArgFunction {

        private Location create(World world, Vector3 vec) {
            return new Location(world, vec.x, vec.y, vec.z);
        }

        private Location create(World world, Vector3 vec, float yaw, float pitch) {
            return new Location(world, vec.x, vec.y, vec.z, yaw, pitch);
        }

        @Override
        public Varargs invoke(Varargs args) {
            return switch (args.narg()) {
                case 0, 1, 5 -> argumentError(args.narg() + 1, "value expected");
                case 2 -> getUserdataOf(create(LuaApi.toWorld(args.checkjstring(1)), args.arg(2).checkuserdata(Vector3.class)));
                case 4 -> args.isuserdata(2)
                    ? getUserdataOf(create(LuaApi.toWorld(args.checkjstring(1)), args.arg(2).checkuserdata(Vector3.class), (float) args.checkdouble(3), (float) args.checkdouble(4)))
                    : getUserdataOf(new Location(LuaApi.toWorld(args.checkjstring(1)), args.checkdouble(2), args.checkdouble(3), args.checkdouble(4)));
                default -> getUserdataOf(new Location(LuaApi.toWorld(args.checkjstring(1)), args.checkdouble(2), args.checkdouble(3), args.checkdouble(4), (float) args.checkdouble(5), (float) args.checkdouble(6)));
            };
        }
    }

    // Location::clone(loc) -> new Location
    private class Loc_clone extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            return getUserdataOf(arg.checkuserdata(Location.class).clone());
        }
    }

    private class Loc_index extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue key) {
            Location loc = arg1.checkuserdata(Location.class);

            if (!key.isstring())
                return LuaConstant.NIL;

            return switch (key.tojstring()) {
                case "world" -> loc.getWorld() == null ? LuaConstant.NIL : valueOf(loc.getWorld().getName());
                case "x" -> valueOf(loc.x());
                case "y" -> valueOf(loc.y());
                case "z" -> valueOf(loc.z());
                case "blockX" -> valueOf(loc.getBlockX());
                case "blockY" -> valueOf(loc.getBlockY());
                case "blockZ" -> valueOf(loc.getBlockZ());
                case "position" ->  instance.getUserdataOf(loc.toVector());
                case "direction" -> instance.getUserdataOf(loc.getDirection());
                case "yaw" -> valueOf(loc.getYaw());
                case "pitch" -> valueOf(loc.getPitch());
                default -> FUNCTIONS.get(key);
            };
        }
    }

    private static class Loc_newindex extends ThreeArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue key, LuaValue value) {
            Location loc = arg1.checkuserdata(Location.class);
            if (!key.isstring())
                return LuaConstant.NIL;

            switch (key.tojstring()) {
                case "position" -> {
                    Vector3 vec = value.checkuserdata(Vector3.class);
                    loc.set(vec.x, vec.y, vec.z);
                }
                case "direction" -> loc.setDirection(Vector3Lib.toVector(value.checkuserdata(Vector3.class)));
                case "yaw" -> loc.setYaw((float) value.checkdouble());
                case "pitch" -> loc.setPitch((float) value.checkdouble());
            }

            return LuaConstant.NIL;
        }
    }

    // Location::neg(loc) -> new location
    private class Loc_neg extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            return getUserdataOf(arg.checkuserdata(Location.class).clone().multiply(-1));
        }
    }

    // Location::add(loc,vec) -> new location
    // Location::add(loc,loc) -> new location
    private class Loc_add extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Location loc = arg1.checkuserdata(Location.class).clone();
            Object obj = arg2.checkuserdata();

            if (obj instanceof Vector3 vec)
                loc.add(vec.x, vec.y, vec.z);
            else if (obj instanceof Location loc2)
                loc.add(loc2);
            else
                return arg1.add(arg2);

            return getUserdataOf(loc);
        }
    }

    // Location::sub(loc,vec) -> new location
    // Location::sub(loc,loc) -> new location
    private class Loc_sub extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Location loc = arg1.checkuserdata(Location.class).clone();
            Object obj = arg2.checkuserdata();

            if (obj instanceof Vector3 vec)
                loc.subtract(vec.x, vec.y, vec.z);
            else if (obj instanceof Location loc2)
                loc.subtract(loc2);
            else
                return arg1.sub(arg2);

            return getUserdataOf(loc);
        }
    }

    // Location::mul(loc,scalar) -> new location
    private class Loc_mul extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return getUserdataOf(arg1.checkuserdata(Location.class).clone().multiply(arg2.checkdouble()));
        }
    }

    // Location::div(loc,scalar) -> new location
    private class Loc_div extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return getUserdataOf(arg1.checkuserdata(Location.class).clone().multiply(1.0/arg2.checkdouble()));

        }
    }

    // Location::toHighest(loc) -> new location
    private class Loc_toHighest extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            return getUserdataOf(arg.checkuserdata(Location.class).toHighestLocation());
        }
    }

    // Location::len(loc) -> double
    private static class Loc_len extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            return valueOf(arg.checkuserdata(Location.class).length());
        }
    }

    // Location::toString(loc) -> string
    private static class Loc_tostring extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            Location loc = arg.checkuserdata(Location.class);
            World world = loc.getWorld();
            return valueOf("<" + (world == null ? "NIL" : world.getName()) + "|<" + loc.x() + "," + loc.y() + ","
                + loc.z() + ">|[" + loc.getYaw() + "," + loc.getPitch() + "]>");
        }
    }
}