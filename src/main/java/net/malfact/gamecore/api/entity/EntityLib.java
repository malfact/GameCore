package net.malfact.gamecore.api.entity;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public final class EntityLib extends TwoArgFunction {

    private static LuaTable library;

    @Override
    public LuaValue call(LuaValue name, LuaValue env) {
        if (library == null) {
            library = new LuaTable();
            library.set("new", new Constructor());
        }
        env.set("Entity", library);
        return env;
    }

    private static final EntityIndexLib[] libs = new EntityIndexLib[] {
        new LivingEntityLib(),
        new BaseEntityLib(),
    };

    public static LuaValue getValueOf(Entity entity) {
        if (entity instanceof Player)
            return LuaConstant.NIL;

        LuaTable meta = new LuaTable();

        for (EntityIndexLib lib : libs) {
            if (!lib.canHandle(entity))
                continue;

            meta.set(LuaConstant.MetaTag.INDEX, lib.index);
            meta.set(LuaConstant.MetaTag.NEWINDEX, lib.newindex);
            break;
        }


        return new LuaUserdata(entity, LuaApi.readOnly(meta));
    }

    private static class Constructor extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue entityValue, LuaValue location) {
            EntityType entityType = LuaApi.toEntityType(entityValue.checkjstring());
            Location loc = location.checkuserdata(Location.class);
            return getValueOf(loc.getWorld().createEntity(loc, entityType.getEntityClass()));
        }
    }

    public static abstract class EntityIndexLib extends LuaFunction {

        public abstract boolean canHandle(Entity entity);
        public abstract LuaValue __index(LuaValue data, LuaValue key);
        public abstract LuaValue __newindex(LuaValue data, LuaValue key, LuaValue value);

        public final LuaFunction index = new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue data, LuaValue key) {
                return __index(data, key);
            }
        };

        public final LuaFunction newindex = new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue data, LuaValue key, LuaValue value) {
                return __newindex(data, key, value);
            }
        };
    }
}
