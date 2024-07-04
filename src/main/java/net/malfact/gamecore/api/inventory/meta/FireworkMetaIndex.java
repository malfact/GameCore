package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkMetaIndex extends ItemMetaLib.Index<FireworkMeta> {
    FireworkMetaIndex() {
        super(FireworkMeta.class);
    }

    @Override
    public Object get(FireworkMeta meta, String key) {
        return null;
    }
}
