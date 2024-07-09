package net.malfact.gamecore.lua.minecraft.meta;

import net.kyori.adventure.text.Component;
import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

import static org.luaj.vm2.LuaValue.valueOf;

public class ItemMetaHandler<T extends ItemMeta> {

    protected static final LuaValue NIL = LuaConstant.NIL;

    private final Class<T> clazz;

    public ItemMetaHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Should return a value or NIL if a known key was found, otherwise null
     */
    public LuaValue get(T meta, String key) {
        return switch (key) {
            case "unbreakable" -> valueOf(meta.isUnbreakable());
            case "itemName" -> meta.hasItemName() ? valueOf(LuaUtil.fromComponent(meta.itemName())) : NIL;
            case "displayName" -> meta.hasDisplayName() ? valueOf(LuaUtil.fromComponent(meta.displayName())) : NIL;
            case "maxStackSize" -> meta.hasMaxStackSize() ? valueOf(meta.hasMaxStackSize()) : NIL;
            case "enchantmentGlintOverride" ->  valueOf(meta.hasEnchantmentGlintOverride());
            case "fireResistant" -> valueOf(meta.isFireResistant());
            case "rarity" -> meta.hasRarity() ? LuaUtil.valueOf(meta.getRarity()): NIL;
            case "lore" -> fromLore(meta.lore());
            case "asComponentString" -> valueOf(meta.getAsComponentString());
            default -> NIL;
        };
    }

    /**
     * Should return true if a known key was found, otherwise false.
     */
    public boolean set(T meta, String key, LuaValue value) {
        switch (key) {
            case "unbreakable" -> meta.setUnbreakable(value.checkboolean());
            case "itemName" -> meta.itemName(LuaUtil.toComponent(value.checkjstring()));
            case "displayName" -> meta.displayName(LuaUtil.toComponent(value.checkjstring()));
            case "maxStackSize" -> meta.setMaxStackSize(value.checkint());
            case "enchantmentGlintOverride" -> meta.setEnchantmentGlintOverride(value.checkboolean());
            case "fireResistant" -> meta.setFireResistant(value.checkboolean());
            case "rarity" -> meta.setRarity(LuaUtil.checkEnum(value, ItemRarity.class));
            case "lore" -> meta.lore(value.isnil() ? null : toLore(value.checktable()));
            default -> {
                return false;
            }
        }

        return true;
    }

    public final LuaValue getRaw(ItemMeta meta, String key) {
        if (!clazz.isInstance(meta))
            return LuaConstant.NIL;

        return get(clazz.cast(meta), key);
    }

    public final boolean setRaw(ItemMeta meta, String key, LuaValue value) {
        if (!clazz.isInstance(meta))
            return false;

        return set(clazz.cast(meta), key, value);
    }

    @Deprecated
    public final Object safeGet(ItemMeta meta, String key) {
        if (clazz.isInstance(meta))
            return get(clazz.cast(meta), key);

        return null;
    }

    @Deprecated
    public final void safeSet(ItemMeta meta, String key, LuaValue value) {
        if (clazz.isInstance(meta))
            set(clazz.cast(meta), key, value);
    }

    private static LuaTable fromLore(List<Component> lore) {
        LuaTable table = new LuaTable();
        if (lore == null)
            return table;

        int i = 1;
        for (var component : lore) {
            table.set(i++, LuaUtil.fromComponent(component));
        }

        return table;
    }

    private static List<Component> toLore(LuaTable table) {
        if (table.length() == 0)
            return null;

        List<Component> lore = new ArrayList<>();
        for (var n = table.next(LuaConstant.NIL); !n.arg1().isnil(); n = table.next(n.arg1())) {
            lore.add(LuaUtil.toComponent(n.arg(2).tojstring()));
        }
        return lore;
    }
}
