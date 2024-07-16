package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.Location;
import org.bukkit.inventory.meta.CompassMeta;
import org.luaj.vm2.LuaValue;

public class CompassMetaHandler extends ItemMetaHandler<CompassMeta> {

    public CompassMetaHandler() {
        super(CompassMeta.class);
    }

    @Override
    public LuaValue get(CompassMeta meta, String key) {
        return switch (key) {
            case "lodestone" ->         meta.hasLodestone() ? LuaApi.userdataOf(meta.getLodestone()) : NIL;
            case "lodestoneTracked" ->  LuaValue.valueOf(meta.isLodestoneTracked());
            default -> null;
        };
    }

    @Override
    public boolean set(CompassMeta meta, String key, LuaValue value) {
        switch (key) {
            case "lodestone" ->         meta.setLodestone(value.checkuserdata(Location.class));
            case "lodestoneTracked" ->  meta.setLodestoneTracked(value.checkboolean());
            default -> {
                return false;
            }
        }

        return true;
    }
}
