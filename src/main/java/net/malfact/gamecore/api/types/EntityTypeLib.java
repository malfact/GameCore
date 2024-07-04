package net.malfact.gamecore.api.types;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.LuaValue;

public class EntityTypeLib extends TypeLib {

    public EntityTypeLib() {
        super("EntityType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var entityType = LuaApi.toEntityType(key);
        if (entityType == null)
            return NIL;

        return getUserdataOf(entityType);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue type, LuaValue key) {
        if (!key.isstring())
            return NIL;

        EntityType entityType = type.checkuserdata(EntityType.class);

        return switch (key.tojstring()) {
            case "key" -> valueOf(entityType.getKey().asMinimalString());
            case "isSpawnable" -> valueOf(entityType.isSpawnable());
            case "isAlive" -> valueOf(entityType.isAlive());
            default -> NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(EntityType.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        var type1 = LuaApi.toEntityType(arg1);
        var type2 = LuaApi.toEntityType(arg2);

        return type1 == type2;
    }
}
