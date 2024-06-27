package net.malfact.gamecore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class TestListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        e.getWhoClicked().sendMessage(
            "Clicked slot: " + e.getSlot() + " | " + e.getRawSlot() + " ("
            + (inventory == null ? "NONE" : inventory.getType()) + ")"
        );
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inventory = e.getInventory();
        e.getPlayer().sendMessage("Opened " + inventory.getType() + " size: " + inventory.getSize());
    }
}
