package net.malfact.gamecore.api.entity;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.malfact.gamecore.api.world.LocationLib;
import net.malfact.gamecore.api.world.Vector3;
import net.malfact.gamecore.api.world.Vector3Lib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class BaseEntityLib extends EntityLib.EntityIndexLib {

    private final LuaFunction SPAWN = new SpawnEntity();
    private final LuaFunction REMOVE = new RemoveEntity();

    protected void remove(Entity entity) {
        entity.remove();
    }

    protected boolean spawn(Entity entity) {
        return entity.spawnAt(entity.getLocation());
    }

    @Override
    public boolean canHandle(Entity entity) {
        return true;
    }

    @Override
    public LuaValue __index(LuaValue data, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        Entity entity = data.checkuserdata(Entity.class);

        return switch (key.tojstring()) {
            case "location" -> LocationLib.getValueOf(entity.getLocation());
            case "velocity" -> Vector3Lib.getValueOf(entity.isOnGround() ? entity.getVelocity().setY(0) : entity.getVelocity());
            case "glowing" -> valueOf(entity.isGlowing());
            case "invulnerable" -> valueOf(entity.isInvulnerable());
            case "silent" -> valueOf(entity.isSilent());
            case "gravity" -> valueOf(entity.hasGravity());
            case "sneaking" -> valueOf(entity.isSneaking());
            case "invisible" -> valueOf(entity.isInvisible());
            case "noPhysics" -> valueOf(entity.hasNoPhysics());
            case "persistent" -> valueOf(entity.isPersistent());
            case "customName" -> valueOf(entity.customName().toString());
            case "customNameVisible" -> valueOf(entity.isCustomNameVisible());

            case "type" -> valueOf(entity.getType().getKey().asMinimalString());
            case "uuid" -> valueOf(entity.getUniqueId().toString());
            case "height" -> valueOf(entity.getHeight());
            case "width" -> valueOf(entity.getWidth());
            case "onGround" -> valueOf(entity.isOnGround());
            case "inWater" -> valueOf(entity.isInWater());
            case "underWater" -> valueOf(entity.isUnderWater());
            case "inLava" -> valueOf(entity.isInLava());

            case "spawn" -> SPAWN;
            case "remove" -> REMOVE;

            default -> LuaConstant.NIL;
        };
    }

    @Override
    public LuaValue __newindex(LuaValue data, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return LuaConstant.NIL;

        Entity entity = data.checkuserdata(Entity.class);

        switch (key.tojstring()) {
            case "location" -> {
                if (value.isnil())
                    break;
                Object obj = value.checkuserdata();
                if (obj instanceof Vector3 vec)
                    entity.teleport(entity.getLocation().set(vec.x, vec.y, vec.z));
                else if (obj instanceof Location loc)
                    entity.teleport(loc);
            }
            case "velocity" -> entity.setVelocity(Vector3Lib.toVector(value.checkuserdata(Vector3.class)));
            case "glowing" -> entity.setGlowing(value.checkboolean());
            case "silent" -> entity.setSilent(value.checkboolean());
            case "gravity" -> entity.setGravity(value.checkboolean());
            case "sneaking" -> entity.setSneaking(value.checkboolean());
            case "invisible" -> entity.setInvisible(value.checkboolean());
            case "noPhysics" -> entity.setNoPhysics(value.checkboolean());
            case "persistent" -> entity.setPersistent(value.checkboolean());
            case "customName" -> entity.customName(value.isnil() ? null : MiniMessage.miniMessage().deserialize(value.checkjstring()));
            case "customNameVisible" -> entity.setCustomNameVisible(value.checkboolean());
        }

        return LuaConstant.NIL;
    }

    private class SpawnEntity extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue data) {
            return valueOf(spawn(data.checkuserdata(Entity.class)));
        }
    }

    protected class RemoveEntity extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue data) {
            remove(data.checkuserdata(Entity.class));
            return LuaConstant.NIL;
        }
    }
}
