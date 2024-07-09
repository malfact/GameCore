package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.luaj.vm2.LuaValue;

import static net.malfact.gamecore.api.LuaUtil.valueOf;

public class TropicalFishBucketMetaHandler extends ItemMetaHandler<TropicalFishBucketMeta> {

    public TropicalFishBucketMetaHandler() {
        super(TropicalFishBucketMeta.class);
    }

    @Override
    public LuaValue get(TropicalFishBucketMeta meta, String key) {
        return switch (key) {
            case "patternColor" -> valueOf(meta.getPatternColor());
            case "bodyColor" -> valueOf(meta.getBodyColor());
            case "pattern" -> valueOf(meta.getPattern());
            case "variant" -> LuaValue.valueOf(meta.hasVariant());
            default -> null;
        };
    }

    @Override
    public boolean set(TropicalFishBucketMeta meta, String key, LuaValue value) {
        switch (key) {
            case "patternColor" -> meta.setPatternColor(LuaUtil.checkEnum(value, DyeColor.class));
            case "bodyColor" -> meta.setBodyColor(LuaUtil.checkEnum(value, DyeColor.class));
            case "pattern" -> meta.setPattern(LuaUtil.checkEnum(value, TropicalFish.Pattern.class));
            default -> {
                return false;
            }
        }

        return true;
    }
}
