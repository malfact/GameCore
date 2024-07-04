package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.luaj.vm2.LuaValue;

import java.util.Locale;

class TropicalFishBucketMetaIndex extends ItemMetaLib.Index<TropicalFishBucketMeta> {

    TropicalFishBucketMetaIndex() {
        super(TropicalFishBucketMeta.class);
    }

    @Override
    public Object get(TropicalFishBucketMeta meta, String key) {
        return switch (key) {
            case "patternColor" -> meta.getPatternColor().toString().toLowerCase(Locale.ROOT);
            case "bodyColor" -> meta.getBodyColor().toString().toLowerCase(Locale.ROOT);
            case "pattern" -> meta.getPattern().toString().toLowerCase(Locale.ROOT);
            case "variant" -> meta.hasVariant();
            default -> null;
        };
    }

    @Override
    public void set(TropicalFishBucketMeta meta, String key, LuaValue value) {
        switch (key) {
            case "patternColor" -> meta.setPatternColor(LuaApi.toEnum(value.checkjstring(), DyeColor.class));
            case "bodyColor" -> meta.setBodyColor(LuaApi.toEnum(value.checkjstring(), DyeColor.class));
            case "pattern" -> meta.setPattern(LuaApi.toEnum(value.checkjstring(), TropicalFish.Pattern.class));
        }
    }
}
