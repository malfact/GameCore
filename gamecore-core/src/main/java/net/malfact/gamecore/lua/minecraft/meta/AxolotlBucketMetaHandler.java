package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.luaj.vm2.LuaValue;

public class AxolotlBucketMetaHandler extends ItemMetaHandler<AxolotlBucketMeta> {

    public AxolotlBucketMetaHandler() {
        super(AxolotlBucketMeta.class);
    }

    @Override
    public LuaValue get(AxolotlBucketMeta meta, String key) {
        if (key.equals("variant"))
            return meta.hasVariant() ? LuaUtil.valueOf(meta.getVariant()) : NIL;
        return null;
    }

    @Override
    public boolean set(AxolotlBucketMeta meta, String key, LuaValue value) {
        if (key.equals("variant")) {
            meta.setVariant(LuaUtil.checkEnum(value, Axolotl.Variant.class));
            return true;
        } else
            return false;
    }
}
