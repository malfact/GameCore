package net.malfact.gamecore.lua.minecraft.meta;

import io.papermc.paper.registry.RegistryKey;
import net.malfact.gamecore.api.LuaUtil;
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

public class BannerMetaHandler extends ItemMetaHandler<BannerMeta> {

    public BannerMetaHandler() {
        super(BannerMeta.class);
    }

    @Override
    public LuaValue get(BannerMeta meta, String key) {
        return switch (key) {
            case "patterns" -> meta.numberOfPatterns() > 0 ? fromPatterns(meta.getPatterns()) : NIL;
            case "number" -> LuaValue.valueOf(meta.numberOfPatterns());
            default -> null;
        };
    }

    @Override
    public boolean set(BannerMeta meta, String key, LuaValue value) {
        if (key.equals("patterns")) {
            meta.setPatterns(value.isnil() ? Collections.emptyList() : toPatterns(value.checktable()));
            return true;
        }
        return false;
    }

    private static LuaTable fromPattern(Pattern pattern) {
        LuaTable table = new LuaTable();
        table.set("color", LuaUtil.valueOf(pattern.getColor()));
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
        DyeColor color = LuaUtil.checkEnum(table.get("color"), DyeColor.class);
        PatternType pattern = LuaUtil.toRegistryEntry(table.get("pattern").checkjstring(), RegistryKey.BANNER_PATTERN);

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
