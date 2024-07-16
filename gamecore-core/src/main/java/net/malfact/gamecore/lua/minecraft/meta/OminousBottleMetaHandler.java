package net.malfact.gamecore.lua.minecraft.meta;

import org.bukkit.inventory.meta.OminousBottleMeta;
import org.luaj.vm2.LuaValue;

public class OminousBottleMetaHandler extends ItemMetaHandler<OminousBottleMeta> {

    public OminousBottleMetaHandler() {
        super(OminousBottleMeta.class);
    }

    @Override
    public LuaValue get(OminousBottleMeta meta, String key) {
        if (key.equals("amplifier")) {
            return meta.hasAmplifier() ? LuaValue.valueOf(meta.getAmplifier()) : NIL;
        }
        return null;
    }

    @Override
    public boolean set(OminousBottleMeta meta, String key, LuaValue value) {
        if (key.equals("amplifier")) {
            meta.setAmplifier(value.checkint());
            return true;
        }

        return false;
    }
}
