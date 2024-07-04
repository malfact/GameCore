package net.malfact.gamecore.api.inventory.meta;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.meta.BookMeta;
import org.luaj.vm2.LuaValue;

import java.util.Locale;

class BookMetaIndex extends ItemMetaLib.Index<BookMeta> {
    BookMetaIndex() {
        super(BookMeta.class);
    }

    @Override
    public Object get(BookMeta meta, String key) {
        return switch (key) {
            case "title" -> meta.hasTitle() ? LuaApi.fromComponent(meta.title()) : null;
            case "author" -> meta.hasAuthor() ? LuaApi.fromComponent(meta.author()) : null;
            case "generation" -> meta.hasGeneration() ? meta.getGeneration().toString().toLowerCase(Locale.ROOT) : null;
            default -> null;
        };
    }

    @Override
    public void set(BookMeta meta, String key, LuaValue value) {
        switch (key) {
            case "title" -> meta.title(LuaApi.toComponent(value.checkjstring()));
            case "author" -> meta.author(LuaApi.toComponent(value.checkjstring()));
            case "generation" -> meta.setGeneration(LuaApi.toEnum(value.checkjstring(), BookMeta.Generation.class));
        }
    }
}
