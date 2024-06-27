package net.malfact.gamecore.api.inventory;

import org.bukkit.inventory.Inventory;

public class LuaContainerInventory extends LuaInventory<Inventory> {

    public LuaContainerInventory(Inventory inventory) {
        super(inventory);
    }

    @Override
    protected Object getItem(String key) {
        return null;
    }

    @Override
    protected void setItem(String key, LuaItemStack itemStack) {

    }
}
