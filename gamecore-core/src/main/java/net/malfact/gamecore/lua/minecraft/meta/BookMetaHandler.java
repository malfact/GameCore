package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.inventory.meta.BookMeta;
import org.luaj.vm2.LuaValue;

import static org.luaj.vm2.LuaValue.valueOf;

public class BookMetaHandler extends ItemMetaHandler<BookMeta> {
    public BookMetaHandler() {
        super(BookMeta.class);
    }

    @Override
    public LuaValue get(BookMeta meta, String key) {
        return switch (key) {
            case "title" -> meta.hasTitle() ? valueOf(LuaUtil.fromComponent(meta.title())) : NIL;
            case "author" -> meta.hasAuthor() ? LuaUtil.valueOf(meta.author()) : NIL;
            case "generation" -> meta.hasGeneration() ? LuaUtil.valueOf(meta.getGeneration()) : NIL;
            default -> null;
        };
    }

    @Override
    public boolean set(BookMeta meta, String key, LuaValue value) {
        switch (key) {
            case "title" -> meta.title(LuaUtil.toComponent(value.checkjstring()));
            case "author" -> meta.author(LuaUtil.toComponent(value.checkjstring()));
            case "generation" -> meta.setGeneration(LuaUtil.checkEnum(value, BookMeta.Generation.class));
            default -> {
                return false;
            }
        }

        return true;
    }
}
