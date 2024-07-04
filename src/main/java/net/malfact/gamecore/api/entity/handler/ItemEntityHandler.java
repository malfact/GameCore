package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.script.Instance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

public class ItemEntityHandler extends EntityHandler {
    @Override
    public boolean acceptsEntity(Entity entity) {
        return entity instanceof Item;
    }

    @Override
    public @NotNull LuaValue onIndex(Instance instance, Entity entity, LuaValue key) {
        if (!key.isstring())
            return NIL;

        Item item = checkEntity(instance, entity, Item.class);

        return switch (key.tojstring()) {
            case "itemStack" ->         instance.getValueOf(item.getItemStack());
            case "pickupDelay" ->       instance.getValueOf(item.getPickupDelay());
            case "unlimitedLifetime" -> instance.getValueOf(item.isUnlimitedLifetime());
            case "owner" ->             instance.getValueOf(item.getOwner().toString());
            case "thrower" ->           instance.getValueOf(item.getThrower().toString());
            case "canMobPickup" ->      instance.getValueOf(item.canMobPickup());
            case "willAge" ->           instance.getValueOf(item.willAge());
            case "health" ->            instance.getValueOf(item.getHealth());
            default -> NIL;
        };
    }

    @Override
    public boolean onNewIndex(Instance instance, Entity entity, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return false;

        Item item = checkEntity(instance, entity, Item.class);

        switch (key.tojstring()) {
            case "itemStack" -> item.setItemStack(value.checkuserdata(ItemStack.class));
            case "pickupDelay" -> item.setPickupDelay(value.checkint());
            case "unlimitedLifetime" -> item.setUnlimitedLifetime(value.checkboolean());
            case "owner" -> item.setOwner(value.checkuserdata(Entity.class).getUniqueId());
            case "thrower" -> item.setThrower(value.checkuserdata(Entity.class).getUniqueId());
            case "canMobPickup" -> item.setCanMobPickup(value.checkboolean());
            case "willAge" -> item.setWillAge(value.checkboolean());
            case "health" -> item.setHealth(value.checkint());
            default -> {return false;}
        };

        return true;
    }
}
