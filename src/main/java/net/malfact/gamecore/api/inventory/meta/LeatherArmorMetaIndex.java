package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.luaj.vm2.LuaValue;

public class LeatherArmorMetaIndex extends ItemMetaLib.Index<LeatherArmorMeta> {
    LeatherArmorMetaIndex() {
        super(LeatherArmorMeta.class);
    }

    @Override
    public Object get(LeatherArmorMeta meta, String key) {
        return switch (key) {
            case "dyed" -> meta.isDyed();
            case "color" -> meta.getColor().toString();
            default -> null;
        };
    }

    @Override
    public void set(LeatherArmorMeta meta, String key, LuaValue value) {
        if (key.equals("color"))
            meta.setColor(toColor(value.checkjstring()));

    }

    private static Color toColor(String string) {
        var dyeColor = LuaApi.toEnum(string, DyeColor.class);

        return dyeColor.getColor();
    }
}
