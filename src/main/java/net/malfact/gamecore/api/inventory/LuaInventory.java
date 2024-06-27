package net.malfact.gamecore.api.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.coercion.CoerceJavaToLua;

@SuppressWarnings("unused")
public abstract class LuaInventory<T extends Inventory> extends LuaUserdata {

    public static LuaInventory<?> of(Inventory inventory) {
        if (inventory instanceof PlayerInventory playerInventory)
            return new LuaPlayerInventory(playerInventory);

        return new LuaContainerInventory(inventory);
    }

    protected final T inventory;
    protected final int offset;
    protected final int length;
    private final LuaValue clearKey = valueOf("clear");
    private final LuaFunction clearFunction;

    public LuaInventory(T inventory) {
        this(inventory, 0);
    }

    public LuaInventory(T inventory, int offset) {
        super(inventory);
        this.inventory = inventory;
        int size = inventory.getSize();
        this.offset = Math.clamp(offset, 0, size-1);
        this.length = size - offset;
        this.clearFunction = new ClearFunction(this);
    }

    public LuaInventory(T inventory, int offset, int length) {
        super(inventory);
        this.inventory = inventory;
        int size = inventory.getSize();
        this.offset = Math.clamp(offset, 0, size-1);
        this.length = Math.clamp(length, 1, size-offset);
        this.clearFunction = new ClearFunction(this);
    }

    protected void clear() {
        System.out.println("MAIN CLEAR");
        inventory.clear();
    }

    protected Object getItem(int index) {
        if (index < 1 || index > length())
            return null;

        return new LuaItemStack(inventory.getItem(index - 1 + offset));
    }

    protected abstract Object getItem(String key);

    protected void setItem(int index, LuaItemStack itemStack) {
        if (index < 1 || index > length())
            return;

        inventory.setItem(index - 1 + offset, itemStack.getItemStack());
    }

    protected abstract void setItem(String key, LuaItemStack itemStack);

    @Override
    public int length() {
        return length;
    }

    @Override
    public final LuaValue len() {
        return valueOf(length());
    }

    @Override
    public final LuaValue get(LuaValue key) {
        if (key.equals(clearKey))
            return clearFunction;

        if (key.isint()) {
            return CoerceJavaToLua.coerce(getItem(key.toint()));
        } else if (key.isstring()) {
            return CoerceJavaToLua.coerce(getItem(key.tojstring()));
        }

        return super.get(key);
    }

    @Override
    public final LuaValue get(int index) {
        return super.get(index);
    }

    @Override
    public final LuaValue get(String key) {
        return super.get(key);
    }

    @Override
    public final void set(LuaValue key, LuaValue value) {
        if (key.equals(clearKey))
            return;

        if (value instanceof LuaItemStack itemStack) {
            if (key.isint()) {
                setItem(key.toint(), itemStack);
                return;
            } else if (key.isstring()) {
                setItem(key.tojstring(), itemStack);
                return;
            }
        }

        super.set(key, value);
    }

    @Override
    public final void set(int index, LuaValue value) {
        super.set(index, value);
    }

    @Override
    public final void set(String key, LuaValue value) {
        super.set(key, value);
    }

    private static class ClearFunction extends ZeroArgFunction {

        private final LuaInventory<?> inventory;

        private ClearFunction(LuaInventory<?> inventory) {
            this.inventory = inventory;
        }

        @Override
        public LuaValue call() {
            inventory.clear();
            return LuaConstant.NIL;
        }
    }
}
