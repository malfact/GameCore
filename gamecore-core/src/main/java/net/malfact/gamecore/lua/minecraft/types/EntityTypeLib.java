package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.LuaValue;

public class EntityTypeLib extends TypeLib<EntityType> {

    public EntityTypeLib() {
        super(EntityType.class,"EntityType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var entityType = LuaUtil.toEntityType(key);
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
            case "key" ->           LuaValue.valueOf(entityType.getKey().asMinimalString());
            case "isSpawnable" ->   LuaValue.valueOf(entityType.isSpawnable());
            case "isAlive" ->       LuaValue.valueOf(entityType.isAlive());
            default -> NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(EntityType.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        var type1 = LuaUtil.toEntityType(arg1);
        var type2 = LuaUtil.toEntityType(arg2);

        return type1 == type2;
    }

    @Override
    protected String getUserdataType() {
        return "entity_type";
    }
}
