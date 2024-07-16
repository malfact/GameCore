package net.malfact.gamecore.lua.minecraft.inventory;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.game.Game;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class InventoryHandler<I extends Inventory> implements TypeHandler<I> {

    public static final InventoryHandler<Inventory> DEFAULT = new InventoryHandler<>(Inventory.class);

    private final LuaFunction func_index =      LuaUtil.toFunction(this::onIndex);
    private final LuaFunction func_newindex =   LuaUtil.toFunction(this::onNewIndex);
    private final LuaFunction func_tostring =   LuaUtil.toFunction(this::onToString);
    private final LuaFunction func_clear =      LuaUtil.toFunction(this::onClear);
    private final LuaFunction func_addItem =    LuaUtil.toVarargFunction(this::addItem);
    private final LuaFunction func_removeItem = LuaUtil.toVarargFunction(this::removeItem);
    private final LuaFunction func_contains =   LuaUtil.toFunction(this::contains);
    private final LuaFunction func_first =      LuaUtil.toFunction(this::first);
    private final LuaFunction func_firstEmpty = LuaUtil.toFunction(this::firstEmpty);
    private final LuaFunction func_remove =     LuaUtil.toFunction(this::remove);
    private final LuaFunction func_close =      LuaUtil.toFunction(this::close);

    protected final Class<I> inventoryClass;

    public InventoryHandler(Class<I> inventoryClass) {
        this.inventoryClass = inventoryClass;
    }

    protected final ItemStack toItemStack(LuaValue value) {
        if (value.isnil())
            return null;

        return value.checkuserdata(ItemStack.class);
    }

    protected final LuaValue valueOf(ItemStack item) {
        if (item == null || item.isEmpty())
            return LuaConstant.NIL;

        return LuaApi.userdataOf(item);
    }

    /**
     * Gets the value from {@code key} of {@code self}.
     * @param instance the instance
     * @param self the inventory
     * @param key the key
     * @return the value from the key
     */
    protected LuaValue get(Game instance, I self, String key) {
        return switch (key) {
            case "size" ->              LuaApi.valueOf(size(self));
            case "maxStackSize" ->      LuaApi.valueOf(self.getMaxStackSize());
            case "contents" ->          toTable(self.getContents());
            case "storageContents" ->   toTable(self.getStorageContents());
            case "isEmpty" ->           LuaApi.valueOf(self.isEmpty());
            case "type" ->              LuaApi.userdataOf(self.getType());
//            case "viewers" ->           toTable(self.getViewers(), instance);
            case "location" ->          LuaApi.userdataOf(self.getLocation());

            case "addItem" ->           func_addItem;
            case "removeItem" ->        func_removeItem;
            case "contains" ->          func_contains;
            case "first" ->             func_first;
            case "firstEmpty" ->        func_firstEmpty;
            case "clear" ->             func_clear;
            case "remove" ->            func_remove;
            case "close" ->             func_close;

            default -> LuaConstant.NIL;
        };
    }

    /**
     * Gets the value from {@code index} of {@code self}.
     * <br>
     * <i>{@code index} will always be in range.</i>
     * @param instance the instance
     * @param self the inventory
     * @param index the slot index; in range [0,self.getSize())
     * @return the value from the index
     */
    protected LuaValue get(Game instance, I self, int index) {
        ItemStack item = self.getItem(index);
        if (item == null || item.isEmpty())
            return LuaConstant.NIL;

        return LuaApi.userdataOf(item);
    }

    /**
     * Sets the ItemStack in slot {@code key} of {@code self} to {@code item}.
     *
     * @param instance the instance
     * @param self     the inventory
     * @param key      the key
     * @param value    the item
     */
    protected void set(Game instance, I self, String key, LuaValue value) {}

    /**
     * Sets the ItemStack in slot {@code index} of {@code self} to {@code item}.
     * <br>
     * <i>{@code index} will always be in range.</i>
     *
     * @param instance the instance
     * @param self     the inventory
     * @param index    the slot index; in range [0,self.getSize())
     * @param value    the item
     */
    protected void set(Game instance, I self, int index, LuaValue value) {
        self.setItem(index, toItemStack(value));
    }

    protected void clear(I self) {
        self.clear();
    }

    protected void clear(I self, int index) {
        self.clear(index);
    }

    protected String toString(I self) {
        return self.getType().toString().toLowerCase(Locale.ROOT) + "_inventory";
    }

    protected int size(I self) {
        return self.getSize();
    }

    @Override
    public final LuaValue getUserdataOf(I object, Game instance) {
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX,    func_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX, func_newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING, func_tostring);

        meta.set("instance", new LuaUserdata(instance));
        meta.set("__userdata_type__", "inventory");

        return new LuaUserdata(object, meta);
    }

    // ----- ----- Userdata Functions ----- -----

    // __index(self, key)
    // Index outside [1, self.size] returns nil
    private LuaValue onIndex(LuaValue arg1, LuaValue arg2) {
        I self = arg1.checkuserdata(inventoryClass);
//        Game instance = arg1.getmetatable().get("instance").checkuserdata(Game.class);

        if (arg2.isint()) {
            int index = arg2.toint();
            if (index < 1 || index > size(self))
                return LuaConstant.NIL;

            return get(null, self, index - 1);
        } else if (arg2.isstring())
            return get(null, self, arg2.tojstring());
        else
            return LuaConstant.NIL;
    }

    // __newindex(self, key, value)
    // Index outside [1, self.size] is ignored
    private void onNewIndex(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        I self = arg1.checkuserdata(inventoryClass);
//        Game instance = arg1.getmetatable().get("instance").checkuserdata(Game.class);

        if (arg2.isint()) {
            int index = arg2.toint();
            if (index < 1 || index > size(self))
                return;

            set(null, self, index - 1, arg3);
        } else if (arg2.isstring())
            set(null, self, arg2.tojstring(), arg3);
    }

    // __tostring(self)
    private LuaValue onToString(LuaValue arg) {
        return LuaValue.valueOf(toString(arg.checkuserdata(inventoryClass)));
    }

    // clear(self [, index])
    private void onClear(LuaValue arg1, LuaValue arg2) {
        I self = arg1.checkuserdata(inventoryClass);
        if (!arg2.isint())
            clear(self);
        else
            clear(self, arg2.toint());
    }

    // addItem(self, ...) -> table | nil
    private Varargs addItem(Varargs args) {
        var self = args.arg1().checkuserdata(Inventory.class);

        if (args.narg() < 2)
            LuaUtil.argumentError(2, "value");

        ItemStack[] items = toArray(args, 2);

        var returned = self.addItem(items);
        if (returned.isEmpty())
            return LuaConstant.NIL;

        return toTable(returned.values());
    }

    // removeItem(self, ...) -> table | nil
    private Varargs removeItem(Varargs args) {
        var self = args.arg1().checkuserdata(Inventory.class);

        if (args.narg() < 2)
            LuaUtil.argumentError(2, "value");

        ItemStack[] items = toArray(args, 2);

        var returned = self.removeItem(items);
        if (returned.isEmpty())
            return LuaConstant.NIL;

        return toTable(returned.values());
    }

    // contains(self, item|material [, amount]) -> bool
    private LuaValue contains(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        var self = arg1.checkuserdata(Inventory.class);
        ItemStack item = arg2.touserdata(ItemStack.class);

        boolean value;

        if (item == null && !arg3.isint())
            value = self.contains(LuaUtil.checkMaterial(arg2));
        else if (item == null)
            value = self.contains(LuaUtil.checkMaterial(arg2), LuaUtil.toMinRange(arg3, 1));
        else if (!arg3.isint())
            value = self.contains(item);
        else
            value = self.contains(item, LuaUtil.toMinRange(arg3, 1));

        return LuaValue.valueOf(value);
    }

    // first(self, item|material) -> int
    private LuaValue first(LuaValue arg1, LuaValue arg2) {
        var self = arg1.checkuserdata(Inventory.class);
        ItemStack item = arg2.touserdata(ItemStack.class);
        if (item == null)
            return LuaValue.valueOf(self.first(LuaUtil.checkMaterial(arg2)) + 1);
        else
            return LuaValue.valueOf(self.first(item) + 1);
    }

    // firstEmpty(self) -> int
    private LuaValue firstEmpty(LuaValue arg) {
        var self = arg.checkuserdata(Inventory.class);
        return LuaValue.valueOf(self.firstEmpty() + 1);
    }

    // remove(self, item|material)
    private void remove(LuaValue arg1, LuaValue arg2) {
        var self = arg1.checkuserdata(Inventory.class);
        ItemStack item = arg2.touserdata(ItemStack.class);
        if (item == null)
            self.remove(LuaUtil.checkMaterial(arg2));
        else
            self.remove(item);
    }

    // close(self)
    private void close(LuaValue arg1) {
        arg1.checkuserdata(Inventory.class).close();
    }

    // ----- ----- Helper Methods ----- -----

    private LuaValue toTable(Collection<?> list) {
        LuaTable table = new LuaTable();
        int i = 1;
        for (var obj : list) {
            table.set(i, LuaApi.userdataOf(obj));
        }

        return table;
    }

    private LuaValue toTable(ItemStack[] items) {
        LuaTable table = new LuaTable();
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null)
                continue;

            table.set(i + 1, LuaApi.userdataOf(items[i]));
        }
        return table;
    }

    private ItemStack[] toArray(Varargs args, int start) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = start; i <= args.narg(); i++) {
            LuaValue arg = args.arg(i);
            if (!arg.isuserdata(ItemStack.class))
                LuaUtil.argumentError(i, "ItemStack", arg);

            items.add(args.arg(i).touserdata(ItemStack.class));
        }
        return items.toArray(new ItemStack[0]);
    }
}
