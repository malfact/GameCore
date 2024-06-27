package net.malfact.gamecore.api.entity;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.attribute.AttributeLib;
import net.malfact.gamecore.api.world.LocationLib;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class LivingEntityLib extends BaseEntityLib {

    private static final LuaFunction ADD_POTION = new AddPotionEffect();
    private static final LuaFunction HAS_POTION = new HasPotionEffect();
    private static final LuaFunction CLEAR_POTION = new ClearPotionEffect();
    private static final LuaFunction GET_ATTRIBUTE = new GetAttribute();

    @Override
    protected void remove(Entity entity) {
        if (entity instanceof LivingEntity livingEntity)
            livingEntity.setHealth(0);

        super.remove(entity);
    }

    @Override
    public boolean canHandle(Entity entity) {
        return entity instanceof LivingEntity;
    }

    @Override
    public LuaValue __index(LuaValue data, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        LivingEntity entity = data.checkuserdata(LivingEntity.class);

        return switch (key.tojstring()) {
            // Get & Set
            case "ai" -> valueOf(entity.hasAI());
            case "air" -> valueOf(entity.getRemainingAir());
            case "maxAir" -> valueOf(entity.getMaximumAir());
            case "lastDamage" -> valueOf(entity.getLastDamage());
            case "noDamageTicks" -> valueOf(entity.getNoDamageTicks());
            case "noActionTicks" -> valueOf(entity.getNoActionTicks());
            case "canPickupItems" -> valueOf(entity.getCanPickupItems());
            case "collidable" -> valueOf(entity.isCollidable());
            case "jumping" -> valueOf(entity.isJumping());
            case "bodyYaw" -> valueOf(entity.getBodyYaw());
            case "health" -> valueOf(entity.getHealth());

            // Get Only
            case "eyeHeight" -> valueOf(entity.getEyeHeight());
            case "rawEyeHeight" -> valueOf(entity.getEyeHeight(true));
            case "eyeLocation" -> LocationLib.getValueOf(entity.getEyeLocation());
            case "leashed" -> valueOf(entity.isLeashed());
            case "swimming" -> valueOf(entity.isSwimming());
            case "sleeping" -> valueOf(entity.isSleeping());
            case "climbing" -> valueOf(entity.isClimbing());
            case "upwardsMovement" -> valueOf(entity.getUpwardsMovement());
            case "sidewaysMovement" -> valueOf(entity.getSidewaysMovement());
            case "forwardsMovement" -> valueOf(entity.getForwardsMovement());
            case "canBreathUnderwater" -> valueOf(entity.canBreatheUnderwater());

            // Functions
            case "addPotionEffect" -> ADD_POTION;
            case "hasPotionEffect" -> HAS_POTION;
            case "clearPotionEffect" -> CLEAR_POTION;
            case "getAttribute" -> GET_ATTRIBUTE;

            // Try Base
            default -> super.__index(data, key);
        };
    }

    @Override
    public LuaValue __newindex(LuaValue data, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return LuaConstant.NIL;

        LivingEntity entity = data.checkuserdata(LivingEntity.class);

        switch (key.tojstring()) {
            case "ai" -> entity.setAI(value.checkboolean());
            case "air" -> entity.setRemainingAir(value.checkint());
            case "maxAir" -> entity.setMaximumAir(value.checkint());
            case "lastDamage" -> entity.setLastDamage(value.checkdouble());
            case "noDamageTicks" -> entity.setNoDamageTicks(value.checkint());
            case "noActionTicks" -> entity.setNoActionTicks(value.checkint());
            case "canPickupItems" -> entity.setCanPickupItems(value.checkboolean());
            case "collidable" -> entity.setCollidable(value.checkboolean());
            case "jumping" -> entity.setJumping(value.checkboolean());
            case "bodyYaw" -> entity.setBodyYaw((float) value.checkdouble());
            case "health" -> entity.setHealth(value.checkdouble());
        }

        return super.__newindex(data, key, value);
    }

    private static class AddPotionEffect extends VarArgFunction {

        @Override
        public Varargs invoke(Varargs args) {
            if (args.narg() < 1)
                argumentError(1, "value");

            LivingEntity entity = args.arg1().checkuserdata(LivingEntity.class);
            PotionEffectType effectType = LuaApi.toPotionEffectType(args.arg(2).checkjstring());
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
            PotionEffectType effectType = LuaApi.toPotionEffectType(effect.checkjstring());
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
            data.checkuserdata(LivingEntity.class).removePotionEffect(LuaApi.toPotionEffectType(effect.checkjstring()));
            return LuaConstant.NIL;
        }
    }

    private static class GetAttribute extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue attributeName) {
            LivingEntity entity = data.checkuserdata(LivingEntity.class);

            Attribute attribute = LuaApi.toAttribute(attributeName.checkjstring());

            AttributeInstance instance = entity.getAttribute(attribute);
            return instance == null ? LuaConstant.NIL : AttributeLib.getValueOf(instance);
        }
    }
}
