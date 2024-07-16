package net.malfact.gamecore.lua;

import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Vector3Lib implements LuaLib, TypeHandler<Vector3> {

    private static final LuaFunction VEC3_INDEX =   LuaUtil.toFunction(Vector3Lib::vector3Index);
    private static final LuaFunction func_new =     LuaUtil.toVarargFunction(Vector3Lib::newVector3);
    private static final LuaFunction func_random =  LuaUtil.toFunction(Vector3Lib::randomVector3);

    private static final LuaTable sharedLib = new LuaTable();

    // Requires Library Functions
    public static final LuaValue VEC3_ZERO =   userdataOf(Vector3.Zero);
    public static final LuaValue VEC3_UP =     userdataOf(Vector3.Up);
    public static final LuaValue VEC3_DOWN =   userdataOf(Vector3.Down);

    static {
        sharedLib.set("clone",              apply(Vector3::copy,            Vector3Lib::userdataOf));
        sharedLib.set("length",             apply(Vector3::length,          LuaValue::valueOf));
        sharedLib.set("lengthSquared",      apply(Vector3::lengthSquared,   LuaValue::valueOf));
        sharedLib.set("neg",                apply(Vector3::negate,          Vector3Lib::userdataOf));
        sharedLib.set("normalize",          apply(Vector3::normalize,       Vector3Lib::userdataOf));
        sharedLib.set("toString",           apply(Vector3::toString,        LuaValue::valueOf));
        sharedLib.set("floor",              apply(Vector3::floor,           Vector3Lib::userdataOf));
        sharedLib.set("ceil",               apply(Vector3::ceil,            Vector3Lib::userdataOf));
        sharedLib.set("distance",           apply(Vector3::distance,        LuaValue::valueOf));
        sharedLib.set("distanceSquared",    apply(Vector3::distanceSquared, LuaValue::valueOf));
        sharedLib.set("mid",                apply(Vector3::midpoint,        Vector3Lib::userdataOf));
        sharedLib.set("cross",              apply(Vector3::crossProduct,    Vector3Lib::userdataOf));
        sharedLib.set("angle",              apply(Vector3::angle,           LuaValue::valueOf));
        sharedLib.set("dot",                apply(Vector3::dot,             LuaValue::valueOf));
        sharedLib.set("add",                apply(Vector3::add,             Vector3Lib::userdataOf));
        sharedLib.set("sub",                apply(Vector3::subtract,        Vector3Lib::userdataOf));
        sharedLib.set("max",                apply(Vector3::max,             Vector3Lib::userdataOf));
        sharedLib.set("min",                apply(Vector3::min,             Vector3Lib::userdataOf));
        sharedLib.set("multiply",           LuaUtil.toFunction(Vector3Lib::vector3Multiply));
        sharedLib.set("divide",             LuaUtil.toFunction(Vector3Lib::vector3Divide));
        sharedLib.set("equals",             LuaUtil.toFunction(Vector3Lib::vector3Equals));
    }

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();
        lib.set("new",      func_new);
        lib.set("random",   func_random);
        lib.set("Zero",     VEC3_ZERO);
        lib.set("Up",       VEC3_UP);
        lib.set("Down",     VEC3_DOWN);

        // Copy all functions to library
        for (Varargs n = sharedLib.next(LuaConstant.NIL); !n.isnil(1); n = sharedLib.next(n.arg1())) {
            lib.set(n.arg(1), n.arg(2));
        }

        env.set("Vector3", lib);
    }

    // Vector3::new()
    // Vector3::new(x, y, z)
    private static Varargs newVector3(Varargs args) {
        if (args.narg() == 0)
            return VEC3_ZERO;

        return userdataOf(
            new Vector3(
                args.checkdouble(1),
                args.checkdouble(2),
                args.checkdouble(3)
            )
        );
    }

    // Vector3::random()
    private static LuaValue randomVector3() {
        return userdataOf(Vector3.random());
    }

    // __index(tbl, key) -> value
    private static LuaValue vector3Index(LuaValue arg1, LuaValue key) {
        Vector3 vec = LuaUtil.checkVector3(arg1);

        if (!key.isstring())
            return LuaConstant.NIL;

        return switch (key.tojstring()) {
            case "x" ->      LuaValue.valueOf(vec.x);
            case "y" ->      LuaValue.valueOf(vec.y);
            case "z" ->      LuaValue.valueOf(vec.z);
            case "blockX" -> LuaValue.valueOf(vec.blockX());
            case "blockY" -> LuaValue.valueOf(vec.blockY());
            case "blockZ" -> LuaValue.valueOf(vec.blockZ());
            default ->       sharedLib.get(key);
        };
    }

    private static LuaValue vector3Equals(LuaValue arg1, LuaValue arg2) {
        if (arg1.isnil() && arg2.isnil())
            return LuaConstant.TRUE;

        if (arg1.isnil() || arg2.isnil())
            return LuaConstant.FALSE;

        if (arg1.getType() != arg2.getType())
            return LuaConstant.FALSE;

        return LuaValue.valueOf(arg1.touserdata().equals(arg2.touserdata()));
    }

    private static LuaValue vector3Multiply(LuaValue arg1, LuaValue arg2) {
        Vector3 vec = LuaUtil.checkVector3(arg1);
        if (arg2.isnumber())
            return userdataOf(vec.multiply(arg2.checkdouble()));
        else
            return userdataOf(vec.multiply(LuaUtil.checkVector3(arg2)));
    }

    private static LuaValue vector3Divide(LuaValue arg1, LuaValue arg2) {
        Vector3 vec = LuaUtil.checkVector3(arg1);
        if (arg2.isnumber())
            return userdataOf(vec.divide(arg2.checkdouble()));
        else
            return userdataOf(vec.divide(LuaUtil.checkVector3(arg2)));
    }

    private static <A> LuaFunction apply(Function<Vector3,A> get, Function<A,LuaValue> wrap) {
        return LuaUtil.toFunction((value) -> {
            return get.andThen(wrap).apply(LuaUtil.checkVector3(value));
        });
    }

    private static <A> LuaFunction apply(BiFunction<Vector3, Vector3, A> get, Function<A, LuaValue> wrap) {
        return LuaUtil.toFunction((arg1, arg2) -> {
           return get.andThen(wrap).apply(LuaUtil.checkVector3(arg1), LuaUtil.checkVector3(arg2));
        });
    }

    @Override
    public LuaValue getUserdataOf(Vector3 vec) {
        return userdataOf(vec);
    }

    public static LuaValue userdataOf(Vector vec) {
        return userdataOf(new Vector3(vec.getX(), vec.getY(), vec.getZ()));
    }

    public static LuaValue userdataOf(Vector3 vec) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX,     VEC3_INDEX);
        meta.set(LuaConstant.MetaTag.TOSTRING,  sharedLib.get("toString"));
        meta.set(LuaConstant.MetaTag.ADD,       sharedLib.get("add"));
        meta.set(LuaConstant.MetaTag.SUB,       sharedLib.get("sub"));
        meta.set(LuaConstant.MetaTag.MUL,       sharedLib.get("multiply"));
        meta.set(LuaConstant.MetaTag.DIV,       sharedLib.get("divide"));
        meta.set(LuaConstant.MetaTag.LEN,       sharedLib.get("length"));
        meta.set(LuaConstant.MetaTag.UNM,       sharedLib.get("neg"));
        meta.set(LuaConstant.MetaTag.EQ,        sharedLib.get("equals"));

        meta.set("__userdata_type__", "vector3");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(vec, meta);
    }

    public static Vector toVector(@NotNull Vector3 vec) {
        return new Vector(vec.x, vec.y, vec.z);
    }

    public static Location toLocation(@NotNull Vector3 vec, World world) {
        return new Location(world, vec.x, vec.y, vec.z);
    }
}
