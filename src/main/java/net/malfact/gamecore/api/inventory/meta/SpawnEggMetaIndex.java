package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.SpawnEggMeta;

public class SpawnEggMetaIndex extends ItemMetaLib.Index<SpawnEggMeta> {
    SpawnEggMetaIndex() {
        super(SpawnEggMeta.class);
    }

    @Override
    public Object get(SpawnEggMeta meta, String key) {
        return null;
    }
}
