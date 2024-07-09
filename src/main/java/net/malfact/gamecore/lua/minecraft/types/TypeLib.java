package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public abstract class TypeLib<T> implements LuaLib, TypeHandler<T> {

    protected static final LuaValue NIL = LuaConstant.NIL;

    /**
     * Passes the library to add functions / values to it.
     * @param lib the library
     */
    protected void onLibBuild(LuaTable lib) {}

    protected abstract LuaValue onLibIndex(LuaValue key);

    protected abstract LuaValue onTypeIndex(LuaValue type, LuaValue key);

    protected abstract String onTypeToString(LuaValue value);

    protected abstract boolean onTypeEquals(LuaValue arg1, LuaValue arg2);

    protected abstract String getUserdataType();

    private final LuaFunction typeIndex = new TypeIndex();
    private final LuaFunction typeEquals = new TypeEquals();
    private final LuaFunction typeToString = new TypeToString();

    private final Class<T> typeClass;
    private final String libName;

    public TypeLib(Class<T> typeClass, String libName) {
        this.typeClass = typeClass;
        this.libName = libName;
    }

    @Override
    public Class<T> getTypeClass() {
        return typeClass;
    }

    @Override
    public final LuaValue getUserdataOf(T o) {
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX, typeIndex);
        meta.set(LuaConstant.MetaTag.TOSTRING, typeToString);
        meta.set(LuaConstant.MetaTag.EQ, typeEquals);

        meta.set("__userdata_type__", getUserdataType());

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(o, meta);
    }

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();

        onLibBuild(lib);

        LuaTable metaTable = new LuaTable();
        metaTable.set(LuaConstant.MetaTag.INDEX,        LuaUtil.toFunction(this::onLibIndex));
        metaTable.set(LuaConstant.MetaTag.METATABLE,    LuaConstant.NIL);

        env.set(libName, lib);
    }

    private class TypeIndex extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return onTypeIndex(arg1, arg2);
        }
    }

    private class TypeEquals extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            if (arg1.isnil() != arg2.isnil())
                return LuaConstant.FALSE;

            return valueOf(onTypeEquals(arg1, arg2));
        }
    }

    private class TypeToString extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            return valueOf(onTypeToString(arg));
        }
    }
}
