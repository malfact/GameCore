package net.malfact.gamecore.api.inventory.meta;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.luaj.vm2.LuaValue;

class ArmorStandMetaIndex extends ItemMetaLib.Index<ArmorStandMeta> {
    ArmorStandMetaIndex() {
        super(ArmorStandMeta.class);
    }

    @Override
    public Object get(ArmorStandMeta meta, String key) {
        return switch (key) {
            case "invisible" -> meta.isInvisible();
            case "noBasePlate" -> meta.hasNoBasePlate();
            case "showArms" -> meta.shouldShowArms();
            case "small" -> meta.isSmall();
            case "marker" -> meta.isMarker();
            default -> null;
        };
    }

    @Override
    public void set(ArmorStandMeta meta, String key, LuaValue value) {
        switch (key) {
            case "invisible" -> meta.setInvisible(value.checkboolean());
            case "noBasePlate" -> meta.setNoBasePlate(value.checkboolean());
            case "showArms" -> meta.setShowArms(value.checkboolean());
            case "small" -> meta.setSmall(value.checkboolean());
            case "marker" -> meta.setMarker(value.checkboolean());
        }
    }
}
