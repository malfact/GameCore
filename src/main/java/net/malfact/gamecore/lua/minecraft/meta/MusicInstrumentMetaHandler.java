package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.luaj.vm2.LuaValue;

public class MusicInstrumentMetaHandler extends ItemMetaHandler<MusicInstrumentMeta> {

    public MusicInstrumentMetaHandler() {
        super(MusicInstrumentMeta.class);
    }

    @Override
    public LuaValue get(MusicInstrumentMeta meta, String key) {
        if (key.equals("instrument"))
            return LuaUtil.valueOf(meta.getInstrument());

        return null;
    }

    @Override
    public boolean set(MusicInstrumentMeta meta, String key, LuaValue value) {
        if (key.equals("instrument")) {
            meta.setInstrument(LuaUtil.toInstrument(value.checkjstring()));
            return true;
        }

        return false;
    }
}
