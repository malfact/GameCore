package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.OminousBottleMeta;
import org.luaj.vm2.LuaValue;

public class OminousBottleMetaIndex extends ItemMetaLib.Index<OminousBottleMeta> {
    OminousBottleMetaIndex() {
        super(OminousBottleMeta.class);
    }

    @Override
    public Object get(OminousBottleMeta meta, String key) {
        if (key.equals("amplifier"))
            return meta.hasAmplifier() ? meta.getAmplifier() : null;

        return null;
    }

    @Override
    public void set(OminousBottleMeta meta, String key, LuaValue value) {
        if (key.equals("amplifier"))
            meta.setAmplifier(value.checkint());
    }
}
