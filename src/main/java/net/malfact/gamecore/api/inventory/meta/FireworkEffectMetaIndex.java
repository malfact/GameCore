package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.FireworkEffectMeta;

public class FireworkEffectMetaIndex extends ItemMetaLib.Index<FireworkEffectMeta> {
    FireworkEffectMetaIndex() {
        super(FireworkEffectMeta.class);
    }

    @Override
    public Object get(FireworkEffectMeta meta, String key) {
        return null;
    }
}
