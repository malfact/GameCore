package net.malfact.gamecore.lua.minecraft.entity;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.Vector3Lib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class EntityHandler<T extends Entity> implements TypeHandler<T> {

    private final LuaFunction func_indexUnregistered =  LuaUtil.toFunction(this::onIndexUnregistered);
    private final LuaFunction func_tostring =           LuaUtil.toFunction(this::onToString);
    private final LuaFunction func_equals =             LuaUtil.toFunction(this::onEquals);

    private final Class<T> entityClass;

    public EntityHandler(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public LuaValue getUserdataOf(T entity) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX,     func_indexUnregistered);
        meta.set(LuaConstant.MetaTag.TOSTRING,  func_tostring);
        meta.set(LuaConstant.MetaTag.EQ,        func_equals);

        meta.set("__userdata_type__", "unregistered<" + type() + ">");

        return new LuaUserdata(entity, meta);
    }

    @Override
    public LuaValue getUserdataOf(T entity, Game instance) {
        if (!instance.hasEntity(entity))
            return getUserdataOf(entity);

        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX,     new InstancedGet<>(this, instance));
        meta.set(LuaConstant.MetaTag.NEWINDEX,  new InstancedSet<>(this, instance));
        meta.set(LuaConstant.MetaTag.TOSTRING,  func_tostring);
        meta.set(LuaConstant.MetaTag.EQ,        func_equals);

        meta.set("__userdata_type__", type());

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(entity, meta);
    }

    /**
     * Get the {@code value} of the {@code entity} registered with {@code instance}.
     * @param instance the instance
     * @param entity the entity
     * @param key the key of the value
     * @return the {@code value} or {@code nil}
     */
    protected LuaValue get(Game instance, T entity, String key) {
        return switch (key) {
            case "registered" ->        LuaConstant.TRUE;
            case "name" ->              LuaApi.valueOf(entity.name());
            case "location" ->          LuaApi.userdataOf(entity.getLocation());
            case "velocity" ->          LuaApi.userdataOf(entity.getVelocity());
            case "uuid" ->              LuaApi.valueOf(entity.getUniqueId().toString());

            case "persistent" ->        LuaApi.valueOf(entity.isPersistent());
            case "invisible" ->         LuaApi.valueOf(entity.isInvisible());
            case "invulnerable" ->      LuaApi.valueOf(entity.isInvulnerable());
            case "silent" ->            LuaApi.valueOf(entity.isSilent());
            case "noPhysics" ->         LuaApi.valueOf(entity.hasNoPhysics());
            case "gravity" ->           LuaApi.valueOf(entity.hasGravity());

            case "glowing" ->           LuaApi.valueOf(entity.isGlowing());
            case "sneaking" ->          LuaApi.valueOf(entity.isSneaking());
            case "customName" ->        LuaApi.valueOf(entity.customName());
            case "customNameVisible" -> LuaApi.valueOf(entity.isCustomNameVisible());

            case "type" ->              LuaApi.userdataOf(entity.getType());
            case "onGround" ->          LuaApi.valueOf(entity.isOnGround());
            case "underWater" ->        LuaApi.valueOf(entity.isUnderWater());
            case "inWater" ->           LuaApi.valueOf(entity.isInWater());
            case "inLava" ->            LuaApi.valueOf(entity.isInLava());

            case "spawn" ->             new EntitySpawn(instance);
            case "remove" ->            new EntityRemove(instance);

            default -> LuaConstant.NIL;
        };
    }

    /**
     * Set the {@code value} of the {@code entity} registered with {@code instance}.
     * @param instance the instance
     * @param entity the entity
     * @param key the key of the value
     * @param value the value or {@code nil}
     */
    protected void set(Game instance, T entity, String key, LuaValue value) {
        switch (key) {
            case "location" ->          entity.teleport(LuaUtil.checkLocation(value));
            case "velocity" ->          entity.setVelocity(Vector3Lib.toVector(value.checkuserdata(Vector3.class)));

            case "persistent" ->        entity.setPersistent(value.checkboolean());
            case "invisible" ->         entity.setInvisible(value.checkboolean());
            case "invulnerable" ->      entity.setInvulnerable(value.checkboolean());
            case "silent" ->            entity.setSilent(value.checkboolean());
            case "noPhysics" ->         entity.setNoPhysics(value.checkboolean());
            case "gravity" ->           entity.setGravity(value.checkboolean());

            case "glowing" ->           entity.setGlowing(value.checkboolean());
            case "sneaking" ->          entity.setSneaking(value.checkboolean());
            case "customName" ->        entity.customName(LuaUtil.toComponent(value));
            case "customNameVisible" -> entity.setCustomNameVisible(value.checkboolean());
        }
    }

    /**
     * Get the {@code value} of the {@code entity} not registered with an instance.
     * @param entity the entity
     * @param key the key of the value
     * @return the {@code value} or {@code nil}
     */
    protected LuaValue getUnregistered(T entity, String key) {
        return switch (key) {
            case "registered" -> LuaConstant.FALSE;
            case "name" -> LuaApi.valueOf(entity.name());
            case "uuid" -> LuaApi.valueOf(entity.getUniqueId().toString());
            case "type" -> LuaApi.userdataOf(entity.getType());

            default -> LuaConstant.NIL;
        };
    }

    /**
     * Get the {@code entity} converted to a {@code string}.
     * @param entity the entity
     * @return the {@code string} form of the entity.
     */
    protected String toString(T entity) {
        return entity.getType().getKey().asMinimalString();
    }

    /**
     * The type of the entity for use with meta tag {@code __userdata_type__}.
     * @return the {@code __userdata_type__} string
     */
    protected String type() {
        return "entity";
    }

    // __eq
    private LuaValue onEquals(LuaValue self, LuaValue other) {
        T entity = self.checkuserdata(entityClass);
        if (other.isnil() || !other.isuserdata(entityClass))
            return LuaConstant.FALSE;

        return LuaValue.valueOf(entity.getUniqueId().equals(other.touserdata(entityClass).getUniqueId()));
    }

    // __tostring
    private LuaValue onToString(LuaValue self) {
        return LuaValue.valueOf(toString(self.checkuserdata(entityClass)));
    }

    // unregistered: __index
    private LuaValue onIndexUnregistered(LuaValue arg1, LuaValue arg2) {
        T entity = arg1.checkuserdata(entityClass);
        if (!arg2.isstring())
            return LuaConstant.NIL;

        return getUnregistered(entity, arg2.tojstring());
    }

    // registered: __index
    private static class InstancedGet<T extends Entity> extends TwoArgFunction {
        private final Game instance;
        private final EntityHandler<T> handler;

        InstancedGet(EntityHandler<T> handler, Game instance) {
            this.handler = handler;
            this.instance = instance;
        }

        @Override
        public LuaValue call(LuaValue self, LuaValue key) {
            T entity = self.checkuserdata(handler.entityClass);
            if (!key.isstring())
                return LuaConstant.NIL;

            return handler.get(instance, entity, key.tojstring());
        }
    }

    // registered: __newindex
    private static class InstancedSet<T extends Entity> extends ThreeArgFunction {

        private final Game instance;
        private final EntityHandler<T> handler;

        InstancedSet(EntityHandler<T> handler, Game instance) {
            this.handler = handler;
            this.instance = instance;
        }

        @Override
        public LuaValue call(LuaValue self, LuaValue key, LuaValue value) {
            T entity = self.checkuserdata(handler.entityClass);
            if (!key.isstring())
                return LuaConstant.NIL;

            handler.set(instance, entity, key.tojstring(), value);
            return LuaConstant.NIL;
        }
    }

    private static class EntitySpawn extends TwoArgFunction {

        private final Game instance;

        private EntitySpawn(Game instance) {
            this.instance = instance;
        }

        @Override
        public LuaValue call(LuaValue userdata, LuaValue arg) {
            Entity entity = userdata.checkuserdata(Entity.class);
            if (entity instanceof Player)
                return LuaConstant.NIL;

            if (!instance.hasEntity(entity))
                return LuaConstant.NIL;

            if (!instance.isRunning())
                return LuaConstant.FALSE;

            Location location = arg.optuserdata(Location.class, entity.getLocation());
            return valueOf(entity.spawnAt(location));
        }
    }

    private static class EntityRemove extends OneArgFunction {
        private final Game instance;

        private EntityRemove(Game instance) {
            this.instance = instance;
        }

        @Override
        public LuaValue call(LuaValue userdata) {
            Entity entity = userdata.checkuserdata(Entity.class);
            if (entity instanceof Player player)
                return valueOf(GameCore.gameManager().leaveGame(player));

            if (!instance.isActive())
                return LuaConstant.FALSE;

            if (!instance.hasEntity(entity))
                return LuaConstant.NIL;


            instance.unregisterEntity(entity);
            entity.remove();

            return LuaConstant.TRUE;
        }
    }
}
