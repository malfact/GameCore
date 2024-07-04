package net.malfact.gamecore.api.inventory.meta;

import io.papermc.paper.registry.RegistryKey;
import net.malfact.gamecore.api.LuaApi;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.meta.BannerMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BannerMetaIndex extends ItemMetaLib.Index<BannerMeta> {

    BannerMetaIndex() {
        super(BannerMeta.class);
    }

    @Override
    public Object get(BannerMeta meta, String key) {
        return switch (key) {
            case "patterns" -> meta.numberOfPatterns() > 0 ? fromPatterns(meta.getPatterns()) : null;
            case "number" -> meta.numberOfPatterns();
            default -> null;
        };
    }

    @Override
    public void set(BannerMeta meta, String key, LuaValue value) {
        if (key.equals("patterns"))
            meta.setPatterns(value.isnil() ? Collections.emptyList() : toPatterns(value.checktable()));
    }

    private static LuaTable fromPattern(Pattern pattern) {
        LuaTable table = new LuaTable();
        table.set("color", LuaApi.getValueOf(pattern.getColor()));
        table.set("pattern", pattern.getPattern().key().toString());
        return table;
    }

    private static LuaTable fromPatterns(List<Pattern> patterns) {
        LuaTable table = new LuaTable();
        int i = 1;
        for (var pattern : patterns) {
            table.set(i++, fromPattern(pattern));
        }

        return table;
    }

    private static Pattern toPattern(LuaTable table) {
        DyeColor color = LuaApi.toEnum(table.get("color").checkjstring(), DyeColor.class);
        PatternType pattern = LuaApi.toRegistryEntry(table.get("pattern").checkjstring(), RegistryKey.BANNER_PATTERN);

        return new Pattern(color, pattern);
    }

    private static List<Pattern> toPatterns(LuaTable table) {
        List<Pattern> patterns = new ArrayList<>();
        for (var n = table.next(LuaConstant.NIL); !n.arg1().isnil(); n = table.next(n.arg1())) {
            patterns.add(toPattern(n.arg(2).checktable()));
        }
        return patterns;
    }
}
