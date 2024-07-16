package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.luaj.vm2.LuaValue;

public class SkullMetaHandler extends ItemMetaHandler<SkullMeta> {

    public SkullMetaHandler() {
        super(SkullMeta.class);
    }

    @Override
    public LuaValue get(SkullMeta meta, String key) {
        return switch (key) {
            case "noteBlockSound" -> LuaUtil.valueOf(meta.getNoteBlockSound());
            case "owningPlayer" -> LuaValue.valueOf(meta.getOwningPlayer().getName());
            default -> null;
        };
    }

    @Override
    public boolean set(SkullMeta meta, String key, LuaValue value) {
        switch (key) {
            case "noteBlockSound" -> meta.setNoteBlockSound(LuaUtil.toNamespacedKey(value.checkjstring()));
            case "owningPlayer" -> meta.setOwningPlayer(Bukkit.getOfflinePlayer(value.checkjstring()));
            default -> {
                return false;
            }
        }

        return true;
    }
}
