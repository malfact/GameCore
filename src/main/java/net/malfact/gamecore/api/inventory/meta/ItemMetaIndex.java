package net.malfact.gamecore.api.inventory.meta;

import net.kyori.adventure.text.Component;
import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class ItemMetaIndex extends ItemMetaLib.Index<ItemMeta> {
    ItemMetaIndex() {
        super(ItemMeta.class);
    }

    @Override
    public Object get(ItemMeta meta, String key) {
        return switch (key) {
            case "unbreakable" -> meta.isUnbreakable();
            case "itemName" -> meta.hasItemName() ? LuaApi.fromComponent(meta.itemName()) : null;
            case "displayName" -> meta.hasDisplayName() ? LuaApi.fromComponent(meta.displayName()) : null;
            case "maxStackSize" -> meta.hasMaxStackSize() ? meta.hasMaxStackSize() : null;
            case "enchantmentGlintOverride" -> meta.hasEnchantmentGlintOverride();
            case "fireResistant" -> meta.isFireResistant();
            case "rarity" -> meta.hasRarity() ? meta.getRarity() : null;
            case "lore" -> fromLore(meta.lore());
            case "asComponentString" -> meta.getAsComponentString();

            default -> null;
        };
    }

    @Override
    public void set(ItemMeta meta, String key, LuaValue value) {
        switch (key) {
            case "unbreakable" -> meta.setUnbreakable(value.checkboolean());
            case "itemName" -> meta.itemName(LuaApi.toComponent(value.checkjstring()));
            case "displayName" -> meta.displayName(LuaApi.toComponent(value.checkjstring()));
            case "maxStackSize" -> meta.setMaxStackSize(value.checkint());
            case "enchantmentGlintOverride" -> meta.setEnchantmentGlintOverride(value.checkboolean());
            case "fireResistant" -> meta.setFireResistant(value.checkboolean());
            case "rarity" -> meta.setRarity(LuaApi.toEnum(value.checkjstring(), ItemRarity.class));
            case "lore" -> meta.lore(value.isnil() ? null : toLore(value.checktable()));
        }
    }

    private static LuaTable fromLore(List<Component> lore) {
        LuaTable table = new LuaTable();
        if (lore == null)
            return table;

        int i = 1;
        for (var component : lore) {
            table.set(i++, LuaApi.fromComponent(component));
        }

        return table;
    }

    private static List<Component> toLore(LuaTable table) {
        if (table.length() == 0)
            return null;

        List<Component> lore = new ArrayList<>();
        for (var n = table.next(LuaConstant.NIL); !n.arg1().isnil(); n = table.next(n.arg1())) {
            lore.add(LuaApi.toComponent(n.arg(2).tojstring()));
        }
        return lore;
    }
}
