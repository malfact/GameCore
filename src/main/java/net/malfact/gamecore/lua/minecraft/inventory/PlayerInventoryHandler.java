package net.malfact.gamecore.lua.minecraft.inventory;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.game.Game;
import org.bukkit.inventory.PlayerInventory;
import org.luaj.vm2.LuaValue;

public class PlayerInventoryHandler extends InventoryHandler<PlayerInventory> {

    private static final PlayerInventoryHandler INSTANCE = new PlayerInventoryHandler();

    public static PlayerInventoryHandler Get() {
        return INSTANCE;
    }

    public PlayerInventoryHandler() {
        super(PlayerInventory.class);
    }

    @Override
    protected LuaValue get(Game instance, PlayerInventory self, String key) {
        return switch (key) {
            case "mainHand" ->      LuaApi.userdataOf(self.getItemInMainHand());
            case "offhand" ->       LuaApi.userdataOf(self.getItemInOffHand());
            case "heldItemSlot" ->  LuaApi.valueOf(self.getHeldItemSlot() + 1);
            case "head",
                 "helmet" ->        LuaApi.userdataOf(self.getHelmet());
            case "chest",
                 "chestplate" ->    LuaApi.userdataOf(self.getChestplate());
            case "legs",
                 "leggings" ->      LuaApi.userdataOf(self.getLeggings());
            case "feet",
                 "boots" ->         LuaApi.userdataOf(self.getBoots());

            default -> super.get(instance, self, key);
        };
    }

    @Override
    protected void set(Game instance, PlayerInventory self, String key, LuaValue value) {
        switch (key) {
            case "mainHand" ->      self.setItemInMainHand(toItemStack(value));
            case "offHand" ->       self.setItemInOffHand(toItemStack(value));
            case "heldItemSlot" ->  self.setHeldItemSlot((value.toint()-1) % 9);
            case "head",
                 "helmet" ->        self.setHelmet(toItemStack(value));
            case "chest",
                 "chestplate" ->    self.setChestplate(toItemStack(value));
            case "legs",
                 "leggings" ->      self.setLeggings(toItemStack(value));
            case "feet",
                 "boots" ->         self.setBoots(toItemStack(value));

            default -> super.set(instance, self, key, value);
        }
    }
}
