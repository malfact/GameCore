package net.malfact.gamecore.api.inventory;

import org.bukkit.inventory.PlayerInventory;

public class LuaPlayerInventory extends LuaInventory<PlayerInventory> {

    private final InventoryHotbar hotbar;
    private final InventoryArmor armor;

    public LuaPlayerInventory(PlayerInventory inventory) {
        super(inventory);
        this.hotbar = new InventoryHotbar(inventory);
        this.armor = new InventoryArmor(inventory);
    }

    @Override
    protected Object getItem(String key) {
        return switch (key) {
            case "mainhand" -> new LuaItemStack(inventory.getItemInMainHand());
            case "offhand" -> new LuaItemStack(inventory.getItemInOffHand());
            case "hotbar" -> hotbar;
            case "armor" -> armor;
            default -> null;
        };

    }

    @Override
    protected void setItem(String key, LuaItemStack itemStack) {
        switch (key) {
            case "mainhand" -> inventory.setItemInMainHand(itemStack.getItemStack());
            case "offhand" -> inventory.setItemInOffHand(itemStack.getItemStack());
        }
    }

    private static class InventoryHotbar extends LuaInventory<PlayerInventory> {

        private InventoryHotbar(PlayerInventory inventory) {
            super(inventory, 0, 9);
        }

        @Override
        protected void clear() {
            for (int i = 0; i < 9; i++) {
                inventory.clear(i);
            }
        }

        @Override
        protected Object getItem(String key) {
            return null;
        }

        @Override
        protected void setItem(String key, LuaItemStack itemStack) {}
    }

    private static class InventoryArmor extends LuaInventory<PlayerInventory> {

        public InventoryArmor(PlayerInventory inventory) {
            super(inventory, 35, 4);
        }

        @Override
        protected void clear() {
            for (int i = 36; i < 40; i++) {
                inventory.clear(i);
            }
        }

        @Override
        protected Object getItem(String key) {
            return switch (key) {
                case "head" -> new LuaItemStack(inventory.getHelmet());
                case "chest" -> new LuaItemStack(inventory.getChestplate());
                case "legs" -> new LuaItemStack(inventory.getLeggings());
                case "feet" -> new LuaItemStack(inventory.getBoots());
                default -> null;
            };
        }

        @Override
        protected void setItem(String key, LuaItemStack itemStack) {
            switch (key) {
                case "head" -> inventory.setHelmet(itemStack.getItemStack());
                case "chest" -> inventory.setChestplate(itemStack.getItemStack());
                case "legs" -> inventory.setLeggings(itemStack.getItemStack());
                case "feet" -> inventory.setBoots(itemStack.getItemStack());
            }
        }
    }
}
