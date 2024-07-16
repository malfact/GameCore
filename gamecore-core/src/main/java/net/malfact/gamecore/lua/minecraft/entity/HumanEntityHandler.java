package net.malfact.gamecore.lua.minecraft.entity;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

public class HumanEntityHandler<T extends HumanEntity> extends LivingEntityHandler<T> {

    private final LuaFunction funcHasCooldown = LuaUtil.toFunction(this::hasCooldown);
    private final LuaFunction funcSetCooldown = LuaUtil.toFunction(this::setCooldown);
    private final LuaFunction funcSleep = LuaUtil.toFunction(this::sleep);
    private final LuaFunction funcWakeup = LuaUtil.toFunction(this::wakeup);
    private final LuaFunction funcDropItem = LuaUtil.toFunction(this::dropItem);

    public HumanEntityHandler(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected LuaValue get(Game instance, T entity, String key) {
        return switch (key) {
            // Get & Set
            case "openInventory" ->         LuaApi.userdataOf(entity.getOpenInventory().getTopInventory());
            case "gamemode" ->              LuaApi.valueOf(entity.getGameMode().toString());
            case "exhaustion" ->            LuaApi.valueOf(entity.getExhaustion());
            case "saturation" ->            LuaApi.valueOf(entity.getSaturation());
            case "foodLevel" ->             LuaApi.valueOf(entity.getFoodLevel());
            case "saturatedRegenRate" ->    LuaApi.valueOf(entity.getSaturatedRegenRate());
            case "unsaturatedRegenRate" ->  LuaApi.valueOf(entity.getUnsaturatedRegenRate());
            case "starvationRate" ->        LuaApi.valueOf(entity.getStarvationRate());
            case "lastDeathLocation" ->     LuaApi.userdataOf(entity.getLastDeathLocation());

            // Get Only
            case "inventory" ->             LuaApi.userdataOf(entity.getInventory());
            case "enderchest" ->            LuaApi.userdataOf(entity.getEnderChest());
            case "itemOnCursor" ->          LuaApi.userdataOf(entity.getItemOnCursor());
            case "isDeeplySleeping" ->      LuaApi.valueOf(entity.isDeeplySleeping());
            case "sleepTicks" ->            LuaApi.valueOf(entity.getSleepTicks());
            case "bedLocation" ->           entity.isSleeping() ? LuaApi.userdataOf(entity.getBedLocation()) : LuaConstant.NIL;
            case "isBlocking" ->            LuaApi.valueOf(entity.isBlocking());
            case "expToLevel" ->            LuaApi.valueOf(entity.getExpToLevel());
            case "attackCooldown" ->        LuaApi.valueOf(entity.getAttackCooldown());

            // Functions
            case "hasCooldown" -> funcHasCooldown;
            case "setCooldown" -> funcSetCooldown;
            case "sleep" -> funcSleep;
            case "wakeup" -> funcWakeup;
            case "dropItem" -> funcDropItem;

            default -> super.get(instance, entity, key);
        };
    }

    @Override
    protected void set(Game instance, T entity, String key, LuaValue value) {
        switch (key) {
            case "openInventory" ->         entity.openInventory(value.checkuserdata(Inventory.class));
            case "gamemode" ->              entity.setGameMode(LuaUtil.checkEnum(value, GameMode.class));
            case "exhaustion" ->            entity.setExhaustion((float) value.checkdouble());
            case "saturation" ->            entity.setSaturation((float) value.checkdouble());
            case "foodLevel" ->             entity.setFoodLevel(value.checkint());
            case "saturatedRegenRate" ->    entity.setSaturatedRegenRate(value.checkint());
            case "unsaturatedRegenRate" ->  entity.setUnsaturatedRegenRate(value.checkint());
            case "starvationRate" ->        entity.setStarvationRate(value.checkint());
            case "lastDeathLocation" ->     entity.setLastDeathLocation(value.checkuserdata(Location.class));
            default -> super.set(instance, entity, key, value);
        }
    }

    @Override
    protected String type() {
        return "human_entity";
    }

    private LuaValue hasCooldown(LuaValue arg1, LuaValue arg2) {
        HumanEntity entity = arg1.checkuserdata(HumanEntity.class);
        Material material = LuaUtil.checkMaterial(arg2);

        return LuaValue.valueOf(entity.hasCooldown(material));
    }

    private void setCooldown(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        HumanEntity entity = arg1.checkuserdata(HumanEntity.class);
        Material material = LuaUtil.checkMaterial(arg2);
        int ticks = arg3.checkint();

        entity.setCooldown(material, ticks);
    }

    private LuaValue sleep(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        HumanEntity entity = arg1.checkuserdata(HumanEntity.class);
        Location location = LuaUtil.checkLocation(arg2);
        boolean force = arg3.optboolean(false);

        return LuaValue.valueOf(entity.sleep(location, force));
    }

    private void wakeup(LuaValue arg1, LuaValue arg2) {
        HumanEntity entity = arg1.checkuserdata(HumanEntity.class);
        boolean setSpawnLocation = arg2.optboolean(false);

        if (!entity.isSleeping())
            return;

        entity.wakeup(setSpawnLocation);
    }

    private LuaValue dropItem(LuaValue arg1, LuaValue arg2) {
        HumanEntity entity = arg1.checkuserdata(HumanEntity.class);
        boolean dropAll = arg2.optboolean(false);

        return LuaValue.valueOf(entity.dropItem(dropAll));
    }
}
