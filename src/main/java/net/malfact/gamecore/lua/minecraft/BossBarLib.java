package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.luaj.vm2.*;

public class BossBarLib implements LuaLib, TypeHandler<BossBar> {

    private final LuaFunction func_new = LuaUtil.toVarargFunction(this::createBossBar);
    private final LuaFunction func_index = LuaUtil.toFunction(this::onIndex);
    private final LuaFunction func_newindex = LuaUtil.toFunction(this::onNewIndex);
    private final LuaFunction func_tostring = LuaUtil.toFunction(this::onToString);

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();
        lib.set("new", func_new);

        env.set("BossBar", lib);
    }

    @Override
    public Class<BossBar> getTypeClass() {
        return BossBar.class;
    }

    @Override
    public LuaValue getUserdataOf(BossBar bossbar) {
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX, func_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX, func_newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING, func_tostring);
        meta.set("__userdata_type__", "vector3");
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(bossbar, meta);
    }

    private Varargs createBossBar(Varargs args) {
        String title = args.optjstring(1, null);
        BarColor color = LuaUtil.toEnum(args.arg(2), BarColor.class);
        BarStyle style = LuaUtil.toEnum(args.arg(3), BarStyle.class);
        if (color == null)
            color = BarColor.RED;
        if (style == null)
            style = BarStyle.SOLID;

        return getUserdataOf(Bukkit.createBossBar(title, color, style));
    }

    private LuaValue onIndex(LuaValue self, LuaValue key) {
        BossBar bossBar = self.checkuserdata(BossBar.class);
        if (!key.isstring())
            return LuaConstant.NIL;

        return switch (key.tojstring()) {
            case "title" -> LuaValue.valueOf(bossBar.getTitle());
            case "color" -> LuaApi.valueOf(bossBar.getColor());
            case "style" -> LuaApi.valueOf(bossBar.getStyle());
            case "progress" -> LuaApi.valueOf(bossBar.getProgress());
            case "visible" -> LuaApi.valueOf(bossBar.isVisible());
            default -> LuaConstant.NIL;
        };
    }

    private void onNewIndex(LuaValue self, LuaValue key, LuaValue value) {
        BossBar bossBar = self.checkuserdata(BossBar.class);
        if (!key.isstring())
            return;

        switch (key.tojstring()) {
            case "title" -> bossBar.setTitle(value.checkjstring());
            case "color" -> bossBar.setColor(LuaUtil.checkEnum(value, BarColor.class));
            case "style" -> bossBar.setStyle(LuaUtil.checkEnum(value, BarStyle.class));
            case "progress" -> bossBar.setProgress(LuaUtil.toRange(value, 0, 1.0));
            case "visible" -> bossBar.setVisible(value.checkboolean());
        }
    }

    private LuaValue onToString() {
        return LuaValue.valueOf("BossBar");
    }
}
