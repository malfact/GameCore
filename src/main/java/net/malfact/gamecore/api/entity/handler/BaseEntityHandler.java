package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.world.Vector3;
import net.malfact.gamecore.api.world.Vector3Lib;
import net.malfact.gamecore.script.Instance;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class BaseEntityHandler extends EntityHandler {

    @Override
    public boolean acceptsEntity(Entity entity) {
        return true;
    }

    @Override
    public @NotNull LuaValue onIndex(Instance instance, Entity entity, LuaValue key) {
        if (!key.isstring())
            return NIL;

        return switch (key.tojstring()) {
            case "location" ->          instance.getValueOf(entity.getLocation());
            case "velocity" ->          instance.getValueOf(entity.getVelocity());
            case "uuid" ->              instance.getValueOf(entity.getUniqueId().toString());

            case "persistent" ->        instance.getValueOf(entity.isPersistent());
            case "invisible" ->         instance.getValueOf(entity.isInvisible());
            case "invulnerable" ->      instance.getValueOf(entity.isInvulnerable());
            case "silent" ->            instance.getValueOf(entity.isSilent());
            case "noPhysics" ->         instance.getValueOf(entity.hasNoPhysics());
            case "gravity" ->           instance.getValueOf(entity.hasGravity());

            case "glowing" ->           instance.getValueOf(entity.isGlowing());
            case "sneaking" ->          instance.getValueOf(entity.isSneaking());
            case "customName" ->        instance.getValueOf(entity.customName().toString());
            case "customNameVisible" -> instance.getValueOf(entity.isCustomNameVisible());

            case "type" ->              instance.getValueOf(entity.getType().getKey().asMinimalString());
            case "onGround" ->          instance.getValueOf(entity.isOnGround());
            case "underWater" ->        instance.getValueOf(entity.isUnderWater());
            case "inWater" ->           instance.getValueOf(entity.isInWater());
            case "inLava" ->            instance.getValueOf(entity.isInLava());

            case "spawn" ->             new EntitySpawn(instance);
            case "remove" ->            new EntityRemove(instance);

            default -> NIL;
        };
    }

    @Override
    public boolean onNewIndex(Instance instance, Entity entity, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return false;

        switch (key.tojstring()) {
            case "location" ->          entity.teleport(LuaApi.checkLocation(value));
            case "velocity" ->          entity.setVelocity(Vector3Lib.toVector(value.checkuserdata(Vector3.class)));

            case "persistent" ->        entity.setPersistent(value.checkboolean());
            case "invisible" ->         entity.setInvisible(value.checkboolean());
            case "invulnerable" ->      entity.setInvulnerable(value.checkboolean());
            case "silent" ->            entity.setSilent(value.checkboolean());
            case "noPhysics" ->         entity.setNoPhysics(value.checkboolean());
            case "gravity" ->           entity.setGravity(value.checkboolean());

            case "glowing" ->           entity.setGlowing(value.checkboolean());
            case "sneaking" ->          entity.setSneaking(value.checkboolean());
            case "customName" ->        entity.customName(LuaApi.toComponent(value));
            case "customNameVisible" -> entity.setCustomNameVisible(value.checkboolean());
            default ->                  {return false;}
        }

        return true;
    }

    public static class EntitySpawn extends TwoArgFunction {

        private final Instance instance;

        private EntitySpawn(Instance instance) {
            this.instance = instance;
        }

        @Override
        public LuaValue call(LuaValue userdata, LuaValue arg) {
            Entity entity = userdata.checkuserdata(Entity.class);
            if (entity instanceof Player)
                return NIL;

            if (!instance.getGame().hasEntity(entity))
                return NIL;

            if (!instance.getGame().isRunning())
                return LuaConstant.FALSE;

            Location location = arg.optuserdata(Location.class, entity.getLocation());
            return valueOf(entity.spawnAt(location));
        }
    }

    public static class EntityRemove extends OneArgFunction {
        private final Instance instance;

        private EntityRemove(Instance instance) {
            this.instance = instance;
        }

        @Override
        public LuaValue call(LuaValue userdata) {
            Entity entity = userdata.checkuserdata(Entity.class);
            if (entity instanceof Player)
                return NIL;

            if (!instance.getGame().hasEntity(entity))
                return NIL;

            if (!instance.getGame().isActive())
                return LuaConstant.FALSE;

            entity.remove();

            return LuaConstant.TRUE;
        }
    }
}
