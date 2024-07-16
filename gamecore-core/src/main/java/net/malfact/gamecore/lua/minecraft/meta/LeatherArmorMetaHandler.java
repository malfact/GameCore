package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.luaj.vm2.LuaValue;

public class LeatherArmorMetaHandler extends ItemMetaHandler<LeatherArmorMeta> {

    public LeatherArmorMetaHandler() {
        super(LeatherArmorMeta.class);
    }

    @Override
    public LuaValue get(LeatherArmorMeta meta, String key) {
        return switch (key) {
            case "dyed" -> LuaValue.valueOf(meta.isDyed());
            case "color" -> LuaValue.valueOf(meta.getColor().toString());
            default -> null;
        };
    }

    @Override
    public boolean set(LeatherArmorMeta meta, String key, LuaValue value) {
        if (key.equals("color")) {
            meta.setColor(toColor(value));
            return true;
        }

        return false;
    }

    private static Color toColor(LuaValue value) {
        var dyeColor = LuaUtil.checkEnum(value, DyeColor.class);

        return dyeColor.getColor();
    }
}
