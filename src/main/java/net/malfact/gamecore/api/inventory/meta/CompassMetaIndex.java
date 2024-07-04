package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.Location;
import org.bukkit.inventory.meta.CompassMeta;
import org.luaj.vm2.LuaValue;

class CompassMetaIndex extends ItemMetaLib.Index<CompassMeta> {

    CompassMetaIndex() {
        super(CompassMeta.class);
    }

    @Override
    public Object get(CompassMeta meta, String key) {
        return switch (key) {
            case "lodestone" -> meta.hasLodestone() ? meta.getLodestone() : null;
            case "lodestoneTracked" -> meta.isLodestoneTracked();
            default -> null;
        };
    }

    @Override
    public void set(CompassMeta meta, String key, LuaValue value) {
        switch (key) {
            case "lodestone" -> meta.setLodestone(value.checkuserdata(Location.class));
            case "lodestoneTracked" -> meta.setLodestoneTracked(value.checkboolean());
        };
    }
}
