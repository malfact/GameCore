package net.malfact.gamecore.lua.minecraft.entity;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;

public class LivingEntityHandler<T extends LivingEntity> extends EntityHandler<T> {

    public LivingEntityHandler(Class<T> entityClass) {
        super(entityClass);
    }

    private final LuaFunction addPotionEffect = LuaUtil.toVarargFunction(this::addPotionEffect);
    private final LuaFunction hasPotionEffect = LuaUtil.toFunction(this::hasPotionEffect);
    private final LuaFunction clearPotionEffect = LuaUtil.toVarargFunction(this::clearPotionEffect);

    @Override
    protected LuaValue get(Game instance, T entity, String key) {
        return switch (key) {
            // Get & Set
            case "ai" ->                    LuaApi.valueOf(entity.hasAI());
            case "air" ->                   LuaApi.valueOf(entity.getRemainingAir());
            case "maxAir" ->                LuaApi.valueOf(entity.getMaximumAir());
            case "lastDamage" ->            LuaApi.valueOf(entity.getLastDamage());
            case "noDamageTicks" ->         LuaApi.valueOf(entity.getNoDamageTicks());
            case "noActionTicks" ->         LuaApi.valueOf(entity.getNoActionTicks());
            case "canPickupItems" ->        LuaApi.valueOf(entity.getCanPickupItems());
            case "collidable" ->            LuaApi.valueOf(entity.isCollidable());
            case "jumping" ->               LuaApi.valueOf(entity.isJumping());
            case "bodyYaw" ->               LuaApi.valueOf(entity.getBodyYaw());
            case "health" ->                LuaApi.valueOf(entity.getHealth());

            // Get Only
            case "eyeHeight" ->             LuaApi.userdataOf(entity.getEyeHeight());
            case "rawEyeHeight" ->          LuaApi.userdataOf(entity.getEyeHeight(true));
            case "eyeLocation" ->           LuaApi.userdataOf(entity.getEyeLocation());
            case "leashed" ->               LuaApi.valueOf(entity.isLeashed());
            case "swimming" ->              LuaApi.valueOf(entity.isSwimming());
            case "sleeping" ->              LuaApi.valueOf(entity.isSleeping());
            case "climbing" ->              LuaApi.valueOf(entity.isClimbing());
            case "upwardsMovement" ->       LuaApi.userdataOf(entity.getUpwardsMovement());
            case "sidewaysMovement" ->      LuaApi.userdataOf(entity.getSidewaysMovement());
            case "forwardsMovement" ->      LuaApi.userdataOf(entity.getForwardsMovement());
            case "canBreathUnderwater" ->   LuaApi.userdataOf(entity.canBreatheUnderwater());

            // Functions
            case "addPotionEffect" -> addPotionEffect;
            case "hasPotionEffect" -> hasPotionEffect;
            case "clearPotionEffect" -> clearPotionEffect;
            case "getAttribute" -> new GetAttribute();

            default -> super.get(instance, entity, key);
        };
    }

    @Override
    protected void set(Game instance, T entity, String key, LuaValue value) {
        switch (key) {
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
            default ->                  super.set(instance, entity, key, value);
        }
    }

    @Override
    protected String toString(T entity) {
        return "living_entity<" + entity.getType().getKey().asMinimalString() + ">";
    }

    private LuaValue addPotionEffect(Varargs args) {
        if (args.narg() < 1)
            throw new LuaError("bad argument #1 (value expected, got nil");

        LivingEntity entity = args.arg1().checkuserdata(LivingEntity.class);
        PotionEffectType effectType = LuaUtil.checkPotionEffectType(args.arg(2));
        int duration = args.arg(3).checkint();
        int amplifier = Math.max(0, args.arg(4).checkint()-1);
        boolean ambient = args.arg(5).optboolean(true);
        boolean particles = args.arg(6).optboolean(false);

        return LuaValue.valueOf(entity.addPotionEffect(new PotionEffect(effectType, duration, amplifier, ambient, particles)));
    }

    private LuaValue hasPotionEffect(LuaValue data, LuaValue effect) {
        LivingEntity entity = data.checkuserdata(LivingEntity.class);
        PotionEffectType effectType = LuaUtil.checkPotionEffectType(effect);
        return LuaValue.valueOf(entity.hasPotionEffect(effectType));
    }

    private LuaValue clearPotionEffect(Varargs args) {
        if (args.narg() == 0)
            throw new LuaError("bad argument #1 (value expected, got nil)");
        if (args.narg() == 1)
            return LuaValue.valueOf(args.arg1().checkuserdata(LivingEntity.class).clearActivePotionEffects());

        args.arg1().checkuserdata(LivingEntity.class).removePotionEffect(LuaUtil.checkPotionEffectType(args.arg(2)));
        return LuaConstant.NIL;
    }

    private static class GetAttribute extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue attributeName) {
            LivingEntity entity = data.checkuserdata(LivingEntity.class);

            Attribute attribute = LuaUtil.toAttribute(attributeName.checkjstring());

            AttributeInstance attributeInstance = entity.getAttribute(attribute);
            return attributeInstance == null
                ? LuaConstant.NIL
                : LuaApi.userdataOf(attributeInstance);
        }
    }
}
