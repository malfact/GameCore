package net.malfact.gamecore.api.world;

import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.script.Instance;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Vector3Lib extends InstancedLib implements UserdataProvider {

    private final LuaFunction VEC3_NEW = new Vector3_new();
    private final LuaFunction VEC3_RANDOM = new Vector3_random();
    private final LuaFunction VEC3_INDEX = new Vector3_index();

    private final LuaFunction VEC3_MAX = new TwoVec3Function("max", Vector3::max, this::getUserdataOf);
    private final LuaFunction VEC3_MIN = new TwoVec3Function("min", Vector3::max, this::getUserdataOf);

    private final LuaTable FUNCTIONS = new LuaTable();

    // Requires Library Functions
    public final LuaValue VEC3_ZERO =   getUserdataOf(Vector3.Zero);
    public final LuaValue VEC3_UP =     getUserdataOf(Vector3.Up);
    public final LuaValue VEC3_DOWN =   getUserdataOf(Vector3.Down);

    public Vector3Lib(Instance instance) {
        super(instance);

        final NamedFunction[] functions = new NamedFunction[] {
            new OneVec3Function("clone",            Vector3::copy,          this::getUserdataOf),
            new OneVec3Function("length",           Vector3::length,        LuaValue::valueOf),
            new OneVec3Function("lengthSquared",    Vector3::lengthSquared, LuaValue::valueOf),
            new OneVec3Function("neg",              Vector3::negate,        this::getUserdataOf),
            new OneVec3Function("normalize",        Vector3::normalize,     this::getUserdataOf),
            new OneVec3Function("toString",         Vector3::toString,      LuaValue::valueOf),
            new OneVec3Function("floor",            Vector3::floor,         this::getUserdataOf),
            new OneVec3Function("ceil",             Vector3::ceil,          this::getUserdataOf),
            new TwoVec3Function("distance",         Vector3::distance,      LuaValue::valueOf),
            new TwoVec3Function("distanceSquared",  Vector3::distanceSquared, LuaValue::valueOf),
            new TwoVec3Function("mid",              Vector3::midpoint,      this::getUserdataOf),
            new TwoVec3Function("cross",            Vector3::crossProduct,  this::getUserdataOf),
            new TwoVec3Function("angle",            Vector3::angle,         LuaValue::valueOf),
            new TwoVec3Function("dot",              Vector3::dot,           LuaValue::valueOf),
            new TwoVec3Function("add",              Vector3::add,           this::getUserdataOf),
            new TwoVec3Function("sub",              Vector3::subtract,      this::getUserdataOf),
            new Vector3_mul(),
            new Vector3_div()
        };

        for (var f : functions) {
            FUNCTIONS.set(f.getName(), f.getFunction());
        }
    }

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        LuaTable vec3 = new LuaTable();
        vec3.set("new", VEC3_NEW);
        vec3.set("random", VEC3_RANDOM);
        vec3.set("min", VEC3_MIN);
        vec3.set("max", VEC3_MAX);
        vec3.set("Zero", VEC3_ZERO);
        vec3.set("Up", VEC3_UP);
        vec3.set("Down", VEC3_DOWN);

        // Copy all functions to library
        for (Varargs n = FUNCTIONS.next(LuaConstant.NIL); !n.isnil(1); n = FUNCTIONS.next(n.arg1())) {
            vec3.set(n.arg(1), n.arg(2));
        }

        env.set("Vector3", vec3);

        return env;
    }

    public static Vector toVector(@NotNull Vector3 vec) {
        return new Vector(vec.x, vec.y, vec.z);
    }

    public static Location toLocation(@NotNull Vector3 vec, World world) {
        return new Location(world, vec.x, vec.y, vec.z);
    }

    private static Vector3 checkVector3(LuaValue luaValue) {
        return luaValue.checkuserdata(Vector3.class);
    }

    @Override
    public boolean accepts(Object o) {
        return (o instanceof Vector3) || (o instanceof Vector);
    }

    @Override
    public LuaValue getUserdataOf(Object o) {
        Vector3 vec;
        if (o instanceof Vector v)
            vec = new Vector3(v.getX(), v.getY(), v.getZ());
        else
            vec = (Vector3) o;

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, VEC3_INDEX);
        meta.set(LuaConstant.MetaTag.TOSTRING, FUNCTIONS.get("toString"));

        meta.set(LuaConstant.MetaTag.ADD, FUNCTIONS.get("add"));
        meta.set(LuaConstant.MetaTag.SUB, FUNCTIONS.get("sub"));
        meta.set(LuaConstant.MetaTag.MUL, FUNCTIONS.get("mul"));
        meta.set(LuaConstant.MetaTag.DIV, FUNCTIONS.get("div"));
        meta.set(LuaConstant.MetaTag.LEN, FUNCTIONS.get("length"));
        meta.set(LuaConstant.MetaTag.UNM, FUNCTIONS.get("neg"));
        meta.set(LuaConstant.MetaTag.EQ, FUNCTIONS.get("equals"));

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(vec, meta);
    }

    // Vector3::new(x, y, z) -> Vector3
    private class Vector3_new extends LibFunction {

        @Override
        public LuaValue call() {
            return VEC3_ZERO;
        }

        @Override
        public LuaValue call(LuaValue a) {
            return argumentError(2,"value expected");
        }

        @Override
        public LuaValue call(LuaValue a, LuaValue b) {
            return argumentError(3,"value expected");
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            return getUserdataOf(new Vector3(x.checkdouble(), y.checkdouble(), z.checkdouble()));
        }
    }

    // Vector3::random() -> Vector3
    private class Vector3_random extends ZeroArgFunction {

        @Override
        public LuaValue call() {
            return getUserdataOf(Vector3.random());
        }
    }

    // __index(tbl, key) -> value
    private class Vector3_index extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue key) {
            Vector3 vec = checkVector3(arg1);

            if (!key.isstring())
                return LuaConstant.NIL;

            return switch (key.tojstring()) {
                case "x" -> valueOf(vec.x);
                case "y" -> valueOf(vec.y);
                case "z" -> valueOf(vec.z);
                case "blockX" -> valueOf(vec.blockX());
                case "blockY" -> valueOf(vec.blockY());
                case "blockZ" -> valueOf(vec.blockZ());
                default -> FUNCTIONS.get(key);
            };
        }
    }

    private interface NamedFunction {

        String getName();
        LuaFunction getFunction();
    }

    private class Vector3_mul extends TwoArgFunction implements NamedFunction  {

        private Vector3_mul() {
            this.name = "mul";
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Vector3 vec = checkVector3(arg1);
            if (arg2.isnumber())
                return getUserdataOf(vec.multiply(arg2.checkdouble()));
            else
                return getUserdataOf(vec.multiply(checkVector3(arg2)));
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public LuaFunction getFunction() {
            return this;
        }
    }

    private class Vector3_div extends TwoArgFunction implements NamedFunction  {

        private Vector3_div() {
            this.name = "div";
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Vector3 vec = checkVector3(arg1);
            if (arg2.isnumber())
                return getUserdataOf(vec.divide(arg2.checkdouble()));
            else
                return getUserdataOf(vec.divide(checkVector3(arg2)));
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public LuaFunction getFunction() {
            return this;
        }
    }

    private static class OneVec3Function extends OneArgFunction implements NamedFunction {

        private final Function<Vector3, LuaValue> result;

        private <A> OneVec3Function(String name, Function<Vector3,A> b, Function<A,LuaValue> c) {
            this.name = name;
            result = b.andThen(c);
        }

        @Override
        public LuaValue call(LuaValue arg) {
            return result.apply(arg.checkuserdata(Vector3.class));
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public LuaFunction getFunction() {
            return this;
        }
    }

    private static class TwoVec3Function extends TwoArgFunction implements NamedFunction {

        private final BiFunction<Vector3, Vector3, LuaValue> result;

        private <A> TwoVec3Function(String name, BiFunction<Vector3, Vector3, A> a, Function<A, LuaValue>b) {
            this.name = name;
            result = a.andThen(b);
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return result.apply(arg1.checkuserdata(Vector3.class), arg2.checkuserdata(Vector3.class));
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public LuaFunction getFunction() {
            return this;
        }
    }
}
