package net.malfact.gamecore.api.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryHandler extends InventoryHandler<PlayerInventory> {

    public PlayerInventoryHandler() {
        super(PlayerInventory.class);
    }

    @Override
    public Object get(PlayerInventory inventory, String key) {
        return switch (key) {
            case "mainhand" -> inventory.getItemInMainHand();
            case "offhand" -> inventory.getItemInOffHand();
            //case "hotbar" -> InventoryLib.getUserdataOf(inventory, HotbarHandler);
            //case "armor" -> InventoryLib.getUserdataOf(inventory, ArmorHandler);
            default -> null;
        };
    }

    @Override
    public void set(PlayerInventory inventory, String key, ItemStack item) {
        switch (key) {
            case "mainhand" -> inventory.setItemInMainHand(item);
            case "offhand" -> inventory.setItemInOffHand(item);
        }
    }

    private static final InventoryHandler<PlayerInventory> HotbarHandler = new InventoryHandler<>(PlayerInventory.class) {

        @Override
        public Object get(PlayerInventory inventory, int index) {
            if (index < 0 || index >= 9)
                return null;

            return inventory.getItem(index);
        }

        @Override
        public String getType() {
            return "HotBar";
        }
    };

    private static final InventoryHandler<PlayerInventory> ArmorHandler = new InventoryHandler<>(PlayerInventory.class) {

        @Override
        public Object get(PlayerInventory inventory, String key) {
            return switch (key) {
                case "helmet",
                     "head" -> inventory.getHelmet();
                case "chestplate",
                     "chest" -> inventory.getChestplate();
                case "leggings",
                     "legs" -> inventory.getLeggings();
                case "boots",
                     "feet" -> inventory.getBoots();
                default -> super.get(inventory, key);
            };
        }

        @Override
        public Object get(PlayerInventory inventory, int index) {
            if (index < 0 || index >= 4)
                return null;

            return inventory.getItem(index + 36);
        }

        @Override
        public void set(PlayerInventory inventory, String key, ItemStack item) {
            switch (key) {
                case "helmet",
                     "head" -> inventory.setHelmet(item);
                case "chestplate",
                     "chest" -> inventory.setChestplate(item);
                case "leggings",
                     "legs" -> inventory.setLeggings(item);
                case "boots",
                     "feet" -> inventory.setBoots(item);
                default -> super.set(inventory, key, item);
            }
        }

        @Override
        public void set(PlayerInventory inventory, int index, ItemStack item) {
            if (index < 0 || index >= 4)
                return;

            inventory.setItem(index + 36, item);
        }

        @Override
        public String getType() {
            return "Armor";
        }
    };
}
