package net.malfact.gamecore.api.inventory;

import net.malfact.gamecore.script.Instance;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

@SuppressWarnings("unused")
public class InventoryHandler<I extends Inventory> {
    private final Class<I> inventoryClass;

    public InventoryHandler(Class<I> inventoryClass) {
        this.inventoryClass = inventoryClass;
    }

    public final LuaValue get(Instance instance, LuaValue inventory, LuaValue key) {
        if (!inventory.isuserdata(inventoryClass))
            return null;

        var inv = inventory.touserdata(inventoryClass);

        var function = getFunction(inv, key);
        if (!function.isnil())
            return function;

        if (key.isint())
            return instance.getValueOf(get(inventory.touserdata(inventoryClass), key.toint()));
        else if (key.isstring()) {
            return instance.getValueOf(get(inventory.touserdata(inventoryClass), key.tojstring()));
        }

        return null;
    }

    public final void set(LuaValue inventory, LuaValue key, ItemStack item) {
        if (!inventory.isuserdata(inventoryClass))
            return;

        if (key.isint())
            set(inventory.touserdata(inventoryClass), key.toint() - 1, item);
        else if (key.isstring())
            set(inventory.touserdata(inventoryClass), key.tojstring(), item);
    }

    public final void clear(LuaValue inventory) {
        if (!inventory.isuserdata(inventoryClass))
            return;

        clear(inventory.touserdata(inventoryClass));
    }

    public LuaValue getFunction(I inventory, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        if (key.tojstring().equals("clear"))
            return clear;

        return LuaConstant.NIL;
    }

    public Object get(I inventory, String key) {
         return null;
    }

    public Object get(I inventory, int index) {
        if (index < 0 || index >= inventory.getSize())
            return null;

        return inventory.getItem(index);
    }

    public void set(I inventory, String key, ItemStack item) {
    }

    public void set(I inventory, int index, ItemStack item) {
        if (index < 0 || index >= inventory.getSize())
            return;

        inventory.setItem(index, item);
    }

    public void clear(I inventory) {
        inventory.clear();
    }

    public String getType() {
        return "Inventory";
    }

    private static final LuaFunction clear = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue arg) {
            InventoryHandler<?> handler = arg.getmetatable().get("HANDLER").checkuserdata(InventoryHandler.class);
            handler.clear(arg);
            return LuaConstant.NIL;
        }
    };
}
