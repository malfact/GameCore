package net.malfact.gamecore.api.types;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.attribute.Attribute;
import org.luaj.vm2.LuaValue;

public class AttributeTypeLib extends TypeLib {

    public AttributeTypeLib() {
        super("Attribute");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var attribute = LuaApi.toAttribute(key);
        if (attribute == null)
            return NIL;

        return getUserdataOf(attribute);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue type, LuaValue key) {
        if (!key.isstring())
            return NIL;

        Attribute attribute = type.checkuserdata(Attribute.class);

        if (key.tojstring().equals("key"))
            return valueOf(attribute.getKey().asMinimalString());

        return NIL;
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(Attribute.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        Attribute a = arg1.touserdata(Attribute.class);
        Attribute b = arg2.touserdata(Attribute.class);
        return a == b;
    }
}
