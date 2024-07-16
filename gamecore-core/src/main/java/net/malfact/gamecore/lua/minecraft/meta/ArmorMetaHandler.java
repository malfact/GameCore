package net.malfact.gamecore.lua.minecraft.meta;

import io.papermc.paper.registry.RegistryKey;
import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class ArmorMetaHandler extends ItemMetaHandler<ArmorMeta> {
    public ArmorMetaHandler() {
        super(ArmorMeta.class);
    }

    @Override
    public @NotNull LuaValue get(ArmorMeta meta, String key) {
        if (key.equals("trim"))
            return meta.hasTrim() ? fromTrim(meta.getTrim()) : null;

        return null;
    }

    @Override
    public boolean set(ArmorMeta meta, String key, LuaValue value) {
        if (key.equals("trim")) {
            meta.setTrim(toTrim(value.checktable()));
            return true;
        }

        return false;
    }

    private static LuaTable fromTrim(ArmorTrim trim) {
        LuaTable table = new LuaTable();
        table.set("material", trim.getMaterial().key().asMinimalString());
        table.set("pattern", trim.getPattern().key().asMinimalString());
        return table;
    }

    private static ArmorTrim toTrim(LuaTable table) {
        TrimMaterial material = LuaUtil.toRegistryEntry(table.get("material").checkjstring(), RegistryKey.TRIM_MATERIAL);
        TrimPattern pattern = LuaUtil.toRegistryEntry(table.get("pattern").checkjstring(), RegistryKey.TRIM_PATTERN);

        return new ArmorTrim(material, pattern);
    }
}
