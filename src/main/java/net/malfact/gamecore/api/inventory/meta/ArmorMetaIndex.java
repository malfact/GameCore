package net.malfact.gamecore.api.inventory.meta;

import io.papermc.paper.registry.RegistryKey;
import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

class ArmorMetaIndex extends ItemMetaLib.Index<ArmorMeta> {
    ArmorMetaIndex() {
        super(ArmorMeta.class);
    }

    @Override
    public Object get(ArmorMeta meta, String key) {
        if (key.equals("trim"))
            return meta.hasTrim() ? fromTrim(meta.getTrim()) : null;

        return null;
    }

    @Override
    public void set(ArmorMeta meta, String key, LuaValue value) {
        if (key.equals("trim"))
            meta.setTrim(toTrim(value.checktable()));
    }

    private static LuaTable fromTrim(ArmorTrim trim) {
        LuaTable table = new LuaTable();
        table.set("material", trim.getMaterial().key().asMinimalString());
        table.set("pattern", trim.getPattern().key().asMinimalString());
        return table;
    }

    private static ArmorTrim toTrim(LuaTable table) {
        TrimMaterial material = LuaApi.toRegistryEntry(table.get("material").checkjstring(), RegistryKey.TRIM_MATERIAL);
        TrimPattern pattern = LuaApi.toRegistryEntry(table.get("pattern").checkjstring(), RegistryKey.TRIM_PATTERN);

        return new ArmorTrim(material, pattern);
    }
}
