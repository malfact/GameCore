package net.malfact.gamecore.api.entity;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.entity.handler.*;
import net.malfact.gamecore.api.event.EventLib;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.script.Instance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.ArrayList;
import java.util.List;

public final class EntityLib extends EventLib implements UserdataProvider {

    private static final List<EntityHandler> HANDLERS = new ArrayList<>();
    private static final EntityHandler DEFAULT_HANDLER = new BaseEntityHandler();

    static {
        registerHandler(new LivingEntityHandler());
        registerHandler(new PlayerEntityHandler());
        registerHandler(new DisplayEntityHandler());
        registerHandler(new TextDisplayEntityHandler());
        registerHandler(new ItemEntityHandler());
    }

    /**
     * Register a new Entity Handler
     * Implement {@link EntityHandler#acceptsEntity(Entity)} to determine which entities are allowed.
     * @param handler the handler
     */
    public static void registerHandler(EntityHandler handler) {
        if (HANDLERS.contains(handler))
            return;

        HANDLERS.add(handler);
    }

    private static final LuaFunction entityToString = new EntityToString();
    private static final LuaFunction entityEquals = new EntityEquals();

    private final LuaFunction entityIndex = new EntityIndex();
    private final LuaFunction entityNewIndex = new EntityNewIndex();


    public EntityLib(Instance instance) {
        super(instance, "entity");
    }

    @Override
    protected LuaValue createLib(LuaValue moduleName, LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("new", new EntityConstructor());
        env.set("Entity", lib);

        return lib;
    }

    @Override
    protected @Nullable LuaValue onIndex(LuaValue key) {
        // ToDo: EntityType
        return null;
    }

    @Override
    public boolean accepts(Object o) {
        return o instanceof Entity;
    }

    @Override
    public LuaValue getUserdataOf(Object o) {
        if (!accepts(o))
            return LuaConstant.NIL;

        Entity entity = (Entity) o;

        if (!instance.getGame().hasEntity(entity))
            return LuaValue.valueOf("{{" + entity.getName() + "}}");

        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX, entityIndex);
        meta.set(LuaConstant.MetaTag.NEWINDEX, entityNewIndex);
        meta.set(LuaConstant.MetaTag.TOSTRING, entityToString);
        meta.set(LuaConstant.MetaTag.EQ, entityEquals);

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(entity, meta);
    }

    private class EntityConstructor extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            EntityType type = LuaApi.checkEntityType(arg1);

            // Only allow spawnable entities
            if (!type.isSpawnable())
                return LuaConstant.NIL;

            // Allow for NIL, Vector3, or Location
            Location location;
            if (!arg2.isnil())
                location = LuaApi.checkLocation(arg2);
            else
                location = new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0);

            // Only allow enabled entities
            if (!type.isEnabledByFeature(location.getWorld()))
                return LuaConstant.NIL;

            Entity entity = location.getWorld().createEntity(location, type.getEntityClass());
            instance.getGame().registerEntity(entity);

            return getUserdataOf(entity);
        }
    }

    private class EntityIndex extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue userdata, LuaValue key) {
            LuaValue value = null;
            Entity entity = userdata.checkuserdata(Entity.class);
            for (var handler : HANDLERS) {
                if (!handler.acceptsEntity(entity))
                    continue;

                value = handler.onIndex(instance, entity, key);

                // "null" is explicit for returning "nil"
                if (value == null)
                    return LuaConstant.NIL;

                // If a value was found, then return.
                // Otherwise, try the next handler.
                if (!value.isnil())
                    return value;
            }

            // If none of the handlers worked, try the default.
            return DEFAULT_HANDLER.onIndex(instance, entity, key);
        }
    }

    private class EntityNewIndex extends ThreeArgFunction {

        @Override
        public LuaValue call(LuaValue userdata, LuaValue key, LuaValue value) {
            Entity entity = userdata.checkuserdata(Entity.class);
            for (var handler : HANDLERS) {
                if (!handler.acceptsEntity(entity))
                    continue;

                // If a value was set, then return.
                // Otherwise, try the next handler.
                if (handler.onNewIndex(instance, entity, key, value))
                    return LuaConstant.NIL;
            }

            // If none of the handlers worked, try the default.
            DEFAULT_HANDLER.onNewIndex(instance, entity, key, value);
            return LuaConstant.NIL;
        }
    }

    private static class EntityToString extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            return valueOf("<" + arg.checkuserdata(Entity.class).getType().getKey().asMinimalString() + ">");
        }
    }

    private static class EntityEquals extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            // If either arg1 XOR arg2 is nil then false
            boolean arg1Nil = arg1.isnil();
            boolean arg2Nil = arg2.isnil();
            if (arg1Nil != arg2Nil)
                return LuaConstant.FALSE;

            // If either arg1 XOR arg2 is not an entity then false;
            boolean arg1IsEntity = arg1.isuserdata(Entity.class);
            boolean arg2IsEntity = arg2.isuserdata(Entity.class);
            if (arg1IsEntity != arg2IsEntity)
                return LuaConstant.FALSE;

            Entity a = arg1.touserdata(Entity.class);
            Entity b = arg2.touserdata(Entity.class);
            if (a.getType() != b.getType())
                return LuaConstant.FALSE;

            return valueOf(a.getUniqueId().equals(b.getUniqueId()));
        }
    }

    private class LightEntityIndex extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue userdata, LuaValue key) {
            if (key.isstring())
                return LuaConstant.NIL;

            Entity entity = userdata.checkuserdata(Entity.class);

            return switch (key.tojstring()) {
                case "name" ->      valueOf(entity.getName());
                case "uuid" ->      valueOf(entity.getUniqueId().toString());
                case "type" ->      instance.getValueOf(entity.getType());
                case "location" ->  instance.getValueOf(entity.getLocation());
                case "valid" ->     valueOf(entity.isValid());
                default ->          LuaConstant.NIL;
            };
        }
    }
}