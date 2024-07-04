package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.PotionMeta;

public class PotionMetaIndex extends ItemMetaLib.Index<PotionMeta> {
    PotionMetaIndex() {
        super(PotionMeta.class);
    }

    @Override
    public Object get(PotionMeta meta, String key) {
        return switch (key) {
            case "baseType" -> meta.hasBasePotionType() ? meta.getBasePotionType() : null;
            case "hasCustomEffects" -> meta.hasCustomEffects();
            // Todo More Potion Stuff
            default -> null;
        };
    }
}
