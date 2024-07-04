package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.luaj.vm2.LuaValue;

class SkullMetaIndex extends ItemMetaLib.Index<SkullMeta> {
    SkullMetaIndex() {
        super(SkullMeta.class);
    }

    @Override
    public Object get(SkullMeta meta, String key) {
        return switch (key) {
            case "noteBlockSound" -> meta.getNoteBlockSound().asMinimalString();
            case "owningPlayer" -> meta.getOwningPlayer().getName();
            default -> null;
        };
    }

    @Override
    public void set(SkullMeta meta, String key, LuaValue value) {
        switch (key) {
            case "noteBlockSound" -> meta.setNoteBlockSound(LuaApi.toNamespacedKey(value.checkjstring()));
            case "owningPlayer" -> meta.setOwningPlayer(Bukkit.getOfflinePlayer(value.checkjstring()));
        }
    }
}
