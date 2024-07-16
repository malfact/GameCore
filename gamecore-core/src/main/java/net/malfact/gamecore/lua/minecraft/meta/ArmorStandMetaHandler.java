package net.malfact.gamecore.lua.minecraft.meta;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.luaj.vm2.LuaValue;

import static org.luaj.vm2.LuaValue.valueOf;

public class ArmorStandMetaHandler extends ItemMetaHandler<ArmorStandMeta> {
    public ArmorStandMetaHandler() {
        super(ArmorStandMeta.class);
    }

    @Override
    public LuaValue get(ArmorStandMeta meta, String key) {
        return switch (key) {
            case "invisible" -> valueOf(meta.isInvisible());
            case "noBasePlate" -> valueOf(meta.hasNoBasePlate());
            case "showArms" -> valueOf(meta.shouldShowArms());
            case "small" -> valueOf(meta.isSmall());
            case "marker" -> valueOf(meta.isMarker());
            default -> null;
        };
    }

    @Override
    public boolean set(ArmorStandMeta meta, String key, LuaValue value) {
        switch (key) {
            case "invisible" -> meta.setInvisible(value.checkboolean());
            case "noBasePlate" -> meta.setNoBasePlate(value.checkboolean());
            case "showArms" -> meta.setShowArms(value.checkboolean());
            case "small" -> meta.setSmall(value.checkboolean());
            case "marker" -> meta.setMarker(value.checkboolean());
            default -> {
                return false;
            }
        }

        return true;
    }
}
