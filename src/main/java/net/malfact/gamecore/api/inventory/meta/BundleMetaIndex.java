package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
class BundleMetaIndex extends ItemMetaLib.Index<BundleMeta> {

    BundleMetaIndex() {
        super(BundleMeta.class);
    }

    @Override
    public Object get(BundleMeta meta, String key) {
        return switch (key) {
            case "hasItems" -> meta.hasItems();
            case "items" -> meta.hasItems() ? fromItems(meta.getItems()) : null;
            default -> null;
        };
    }

    @Override
    public void set(BundleMeta meta, String key, LuaValue value) {
        if (key.equals("items"))
            meta.setItems(value.isnil() ? null : toItems(value.checktable()));
    }

    private static LuaTable fromItems(List<ItemStack> items) {
        LuaTable table = new LuaTable();
        int i = 1;
        for (var item : items) {
            table.set(i++, LuaApi.getValueOf(item));
        }

        return table;
    }

    private static List<ItemStack> toItems(LuaTable table) {
        List<ItemStack> items = new ArrayList<>();

        for (var n = table.next(LuaConstant.NIL); !n.arg1().isnil(); n = table.next(n.arg1())) {
            items.add(n.arg(2).checkuserdata(ItemStack.class));
        }

        return items;
    }
}
