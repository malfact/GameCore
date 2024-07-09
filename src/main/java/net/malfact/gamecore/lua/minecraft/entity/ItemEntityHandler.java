package net.malfact.gamecore.lua.minecraft.entity;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.game.Game;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaValue;

public class ItemEntityHandler extends EntityHandler<Item> {

    public ItemEntityHandler() {
        super(Item.class);
    }

    @Override
    protected LuaValue get(Game instance, Item item, String key) {
        return switch (key) {
            case "itemStack" ->         LuaApi.userdataOf(item.getItemStack());
            case "pickupDelay" ->       LuaApi.valueOf(item.getPickupDelay());
            case "unlimitedLifetime" -> LuaApi.valueOf(item.isUnlimitedLifetime());
            case "owner" ->             LuaApi.valueOf(item.getOwner().toString());
            case "thrower" ->           LuaApi.valueOf(item.getThrower().toString());
            case "canMobPickup" ->      LuaApi.valueOf(item.canMobPickup());
            case "canPlayerPickup" ->   LuaApi.valueOf(item.canPlayerPickup());
            case "willAge" ->           LuaApi.valueOf(item.willAge());
            case "health" ->            LuaApi.valueOf(item.getHealth());
            default -> super.get(instance, item, key);
        };
    }

    @Override
    protected void set(Game instance, Item item, String key, LuaValue value) {
        switch (key) {
            case "itemStack" -> item.setItemStack(value.checkuserdata(ItemStack.class));
            case "pickupDelay" -> item.setPickupDelay(value.checkint());
            case "unlimitedLifetime" -> item.setUnlimitedLifetime(value.checkboolean());
            case "owner" -> item.setOwner(value.checkuserdata(Entity.class).getUniqueId());
            case "thrower" -> item.setThrower(value.checkuserdata(Entity.class).getUniqueId());
            case "canMobPickup" -> item.setCanMobPickup(value.checkboolean());
            case "canPlayerPickup" -> item.setCanPlayerPickup(value.checkboolean());
            case "willAge" -> item.setWillAge(value.checkboolean());
            case "health" -> item.setHealth(value.checkint());
            default -> super.set(instance, item, key, value);
        };
    }

    @Override
    protected String type() {
        return "item";
    }
}
