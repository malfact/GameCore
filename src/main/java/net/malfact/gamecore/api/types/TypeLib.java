package net.malfact.gamecore.api.types;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public abstract class TypeLib extends TwoArgFunction {

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

    private final LuaFunction typeIndex = new TypeIndex();
    private final LuaFunction typeEquals = new TypeEquals();
    private final LuaFunction typeToString = new TypeToString();

    private final String libName;

    public TypeLib(String libName) {
        this.libName = libName;
    }

    public final LuaValue getUserdataOf(Object o) {
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX, typeIndex);
        meta.set(LuaConstant.MetaTag.TOSTRING, typeToString);
        meta.set(LuaConstant.MetaTag.EQ, typeEquals);
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(o, meta);
    }

    @Override
    public final LuaValue call(LuaValue moduleName, LuaValue env) {
        LuaTable lib = new LuaTable();

        onLibBuild(lib);

        LuaTable metaTable = new LuaTable();
        metaTable.set(LuaConstant.MetaTag.INDEX, new LibIndex());
        metaTable.set(LuaConstant.MetaTag.METATABLE, LuaConstant.NIL);

        env.set(libName, lib);
        return env;
    }

    private class LibIndex extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return onLibIndex(arg2);
        }
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
