package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.api.AttributeLib;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.script.Instance;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class LivingEntityHandler extends EntityHandler {

    @Override
    public boolean acceptsEntity(Entity entity) {
        return (entity instanceof LivingEntity);
    }

    @Override
    public @NotNull LuaValue onIndex(Instance instance, Entity e, LuaValue key) {
        if (!key.isstring())
            return NIL;

        LivingEntity entity = checkEntity(instance, e, LivingEntity.class);

        return switch (key.tojstring()) {
            // Get & Set
            case "ai" ->                    LuaValue.valueOf(entity.hasAI());
            case "air" ->                   LuaValue.valueOf(entity.getRemainingAir());
            case "maxAir" ->                LuaValue.valueOf(entity.getMaximumAir());
            case "lastDamage" ->            LuaValue.valueOf(entity.getLastDamage());
            case "noDamageTicks" ->         LuaValue.valueOf(entity.getNoDamageTicks());
            case "noActionTicks" ->         LuaValue.valueOf(entity.getNoActionTicks());
            case "canPickupItems" ->        LuaValue.valueOf(entity.getCanPickupItems());
            case "collidable" ->            LuaValue.valueOf(entity.isCollidable());
            case "jumping" ->               LuaValue.valueOf(entity.isJumping());
            case "bodyYaw" ->               LuaValue.valueOf(entity.getBodyYaw());
            case "health" ->                LuaValue.valueOf(entity.getHealth());

            // Get Only
            case "eyeHeight" ->             instance.getValueOf(entity.getEyeHeight());
            case "rawEyeHeight" ->          instance.getValueOf(entity.getEyeHeight(true));
            case "eyeLocation" ->           instance.getValueOf(entity.getEyeLocation());
            case "leashed" ->               LuaValue.valueOf(entity.isLeashed());
            case "swimming" ->              LuaValue.valueOf(entity.isSwimming());
            case "sleeping" ->              LuaValue.valueOf(entity.isSleeping());
            case "climbing" ->              LuaValue.valueOf(entity.isClimbing());
            case "upwardsMovement" ->       instance.getValueOf(entity.getUpwardsMovement());
            case "sidewaysMovement" ->      instance.getValueOf(entity.getSidewaysMovement());
            case "forwardsMovement" ->      instance.getValueOf(entity.getForwardsMovement());
            case "canBreathUnderwater" ->   instance.getValueOf(entity.canBreatheUnderwater());

            // Functions
            case "addPotionEffect" -> new AddPotionEffect();
            case "hasPotionEffect" -> new HasPotionEffect();
            case "clearPotionEffect" -> new ClearPotionEffect();
            case "getAttribute" -> new GetAttribute();

            default -> NIL;
        };
    }

    @Override
    public boolean onNewIndex(Instance instance, Entity e, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return false;

        LivingEntity entity = checkEntity(instance, e, LivingEntity.class);

        switch (key.tojstring()) {
            case "ai" ->                entity.setAI(value.checkboolean());
            case "air" ->               entity.setRemainingAir(value.checkint());
            case "maxAir" ->            entity.setMaximumAir(value.checkint());
            case "lastDamage" ->        entity.setLastDamage(value.checkdouble());
            case "noDamageTicks" ->     entity.setNoDamageTicks(value.checkint());
            case "noActionTicks" ->     entity.setNoActionTicks(value.checkint());
            case "canPickupItems" ->    entity.setCanPickupItems(value.checkboolean());
            case "collidable" ->        entity.setCollidable(value.checkboolean());
            case "jumping" ->           entity.setJumping(value.checkboolean());
            case "bodyYaw" ->           entity.setBodyYaw((float) value.checkdouble());
            case "health" ->            entity.setHealth(value.checkdouble());
            default ->                  {return false;}
        }

        return true;
    }

    private static class AddPotionEffect extends VarArgFunction {

        @Override
        public Varargs invoke(Varargs args) {
            if (args.narg() < 1)
                argumentError(1, "value");

            LivingEntity entity = args.arg1().checkuserdata(LivingEntity.class);
            PotionEffectType effectType = LuaApi.checkPotionEffectType(args.arg(2));
            int duration = args.arg(3).checkint();
            int amplifier = Math.max(0, args.arg(4).checkint()-1);
            boolean ambient = args.arg(5).optboolean(true);
            boolean particles = args.arg(6).optboolean(false);

            return valueOf(entity.addPotionEffect(new PotionEffect(effectType, duration, amplifier, ambient, particles)));
        }
    }

    private static class HasPotionEffect extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue effect) {
            LivingEntity entity = data.checkuserdata(LivingEntity.class);
            PotionEffectType effectType = LuaApi.checkPotionEffectType(effect);
            return valueOf(entity.hasPotionEffect(effectType));
        }
    }

    private static class ClearPotionEffect extends LibFunction {

        @Override
        public LuaValue call(LuaValue data) {
            return valueOf(data.checkuserdata(LivingEntity.class).clearActivePotionEffects());
        }

        @Override
        public LuaValue call(LuaValue data, LuaValue effect) {
            data.checkuserdata(LivingEntity.class).removePotionEffect(LuaApi.checkPotionEffectType(effect));
            return LuaConstant.NIL;
        }
    }

    private static class GetAttribute extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue attributeName) {
            LivingEntity entity = data.checkuserdata(LivingEntity.class);

            Attribute attribute = LuaApi.toAttribute(attributeName.checkjstring());

            AttributeInstance instance = entity.getAttribute(attribute);
            return instance == null ? LuaConstant.NIL : AttributeLib.userdataOf(instance);
        }
    }
}
