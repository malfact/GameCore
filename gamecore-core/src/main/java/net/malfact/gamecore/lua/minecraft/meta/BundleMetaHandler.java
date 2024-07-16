package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.GameCore;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class BundleMetaHandler extends ItemMetaHandler<BundleMeta> {

    public BundleMetaHandler() {
        super(BundleMeta.class);
    }

    @Override
    public LuaValue get(BundleMeta meta, String key) {
        return switch (key) {
            case "hasItems" -> LuaValue.valueOf(meta.hasItems());
            case "items" -> meta.hasItems() ? fromItems(meta.getItems()) : NIL;
            default -> null;
        };
    }

    @Override
    public boolean set(BundleMeta meta, String key, LuaValue value) {
        if (key.equals("items")) {
            meta.setItems(value.isnil() ? null : toItems(value.checktable()));
            return true;
        }
        return false;
    }

    private static LuaTable fromItems(List<ItemStack> items) {
        LuaTable table = new LuaTable();
        int i = 1;
        for (var item : items) {
            table.set(i++, GameCore.luaApi().getUserdataOf(item));
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
