package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class CrossbowMetaHandler extends ItemMetaHandler<CrossbowMeta> {

    public CrossbowMetaHandler() {
        super(CrossbowMeta.class);
    }

    @Override
    public LuaValue get(CrossbowMeta meta, String key) {
        if (key.equals("chargedProjectiles"))
            return meta.hasChargedProjectiles() ? fromItems(meta.getChargedProjectiles()) : NIL;

        return null;
    }

    @Override
    public boolean set(CrossbowMeta meta, String key, LuaValue value) {
        if (key.equals("chargedProjectiles")) {
            meta.setChargedProjectiles(value.isnil() ? null : toItems(value.checktable()));
            return true;
        }

        return false;
    }

    private static LuaTable fromItems(List<ItemStack> items) {
        LuaTable table = new LuaTable();
        int i = 1;
        for (var item : items) {
            table.set(i++, LuaApi.userdataOf(item));
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
