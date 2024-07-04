package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.luaj.vm2.LuaValue;

class AxolotlBucketMetaIndex extends ItemMetaLib.Index<AxolotlBucketMeta> {

    AxolotlBucketMetaIndex() {
        super(AxolotlBucketMeta.class);
    }

    @Override
    public Object get(AxolotlBucketMeta meta, String key) {
        if (key.equals("variant"))
            return meta.hasVariant() ? meta.getVariant() : null;
        return null;
    }

    @Override
    public void set(AxolotlBucketMeta meta, String key, LuaValue value) {
        if (key.equals("variant"))
            meta.setVariant(LuaApi.toEnum(value.checkjstring(), Axolotl.Variant.class));
    }
}
