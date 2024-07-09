package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class WritableBookMetaHandler extends ItemMetaHandler<WritableBookMeta> {

    public WritableBookMetaHandler() {
        super(WritableBookMeta.class);
    }

    @Override
    public LuaValue get(WritableBookMeta meta, String key) {
        return switch (key) {
            case "pageCount" -> LuaValue.valueOf(meta.getPageCount());
            case "getPage" -> getPage;
            case "setPage" -> setPage;
            case "addPage" -> addPage;
            default -> null;
        };
    }

    private static final LuaFunction getPage = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var meta = arg1.checkuserdata(BookMeta.class);
            var page = arg2.checkint();
            if (page < 1 || page >= meta.getPageCount())
                return LuaConstant.NIL;

            return LuaUtil.valueOf(meta.page(page));
        }
    };

    private static final LuaFunction setPage = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            var meta = arg1.checkuserdata(BookMeta.class);
            var page = arg2.checkint();
            if (page < 1 || page >= meta.getPageCount())
                return LuaConstant.NIL;
            var content = LuaUtil.toComponent(arg3.checkjstring());

            meta.page(page, content);
            return LuaConstant.NIL;
        }
    };

    private static final LuaFunction addPage = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var meta = arg1.checkuserdata(BookMeta.class);
            var content = LuaUtil.toComponent(arg2.checkjstring());

            meta.addPages(content);
            return LuaConstant.NIL;
        }
    };
}
