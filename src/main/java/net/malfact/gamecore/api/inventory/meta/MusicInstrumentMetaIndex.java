package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.luaj.vm2.LuaValue;

class MusicInstrumentMetaIndex extends ItemMetaLib.Index<MusicInstrumentMeta> {
    MusicInstrumentMetaIndex() {
        super(MusicInstrumentMeta.class);
    }

    @Override
    public Object get(MusicInstrumentMeta meta, String key) {
        if (key.equals("instrument"))
            return meta.getInstrument().key().asMinimalString();

        return null;
    }

    @Override
    public void set(MusicInstrumentMeta meta, String key, LuaValue value) {
        if (key.equals("instrument"))
            meta.setInstrument(LuaApi.toInstrument(value.checkjstring()));
    }
}
