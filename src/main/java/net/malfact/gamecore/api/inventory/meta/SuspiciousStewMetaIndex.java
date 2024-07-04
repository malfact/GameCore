package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.luaj.vm2.LuaValue;

public class SuspiciousStewMetaIndex extends ItemMetaLib.Index<SuspiciousStewMeta> {
    SuspiciousStewMetaIndex() {
        super(SuspiciousStewMeta.class);
    }

    @Override
    public Object get(SuspiciousStewMeta meta, String key) {
        return null;
    }

    @Override
    public void set(SuspiciousStewMeta meta, String key, LuaValue value) {

    }
}
