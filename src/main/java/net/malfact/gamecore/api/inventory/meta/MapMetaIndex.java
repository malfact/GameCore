package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.MapMeta;

public class MapMetaIndex extends ItemMetaLib.Index<MapMeta> {
    MapMetaIndex() {
        super(MapMeta.class);
    }

    @Override
    public Object get(MapMeta meta, String key) {
        return null;
    }
}
