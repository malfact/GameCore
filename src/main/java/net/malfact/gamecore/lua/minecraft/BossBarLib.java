package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.game.Cleanable;
import net.malfact.gamecore.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.luaj.vm2.*;

public class BossBarLib implements LuaLib {

    public static TypeHandler<BossBar> HANDLER = new Handler();

    private final LuaFunction func_new = LuaUtil.toVarargFunction(this::createBossBar);

    private final Game instance;

    public BossBarLib(Game instance) {
        this.instance = instance;
    }

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();
        lib.set("new", func_new);

        env.set("BossBar", lib);
    }

    private Varargs createBossBar(Varargs args) {
        String title = args.optjstring(1, null);
        BarColor color = LuaUtil.toEnum(args.arg(2), BarColor.class);
        BarStyle style = LuaUtil.toEnum(args.arg(3), BarStyle.class);
        if (color == null)
            color = BarColor.RED;
        if (style == null)
            style = BarStyle.SOLID;

        var bossBar = Bukkit.createBossBar(title, color, style);
        instance.registerCleanable(Cleanable.of(bossBar, BossBar::removeAll));

        return HANDLER.getUserdataOf(bossBar);
    }

    private static class Handler implements TypeHandler<BossBar> {

        private final LuaFunction func_index = LuaUtil.toFunction(this::onIndex);
        private final LuaFunction func_newindex = LuaUtil.toFunction(this::onNewIndex);
        private final LuaFunction func_tostring = LuaUtil.toFunction(this::onToString);
        private final LuaFunction func_addPlayer = LuaUtil.toFunction(this::addPlayer);
        private final LuaFunction func_removePlayer = LuaUtil.toFunction(this::removePlayer);
        private final LuaFunction func_removeAll = LuaUtil.toFunction(this::removeAll);

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
            meta.set("__userdata_type__", "boss_bar");
            meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

            return new LuaUserdata(bossbar, meta);
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

                case "addPlayer" -> func_addPlayer;
                case "removePlayer" -> func_removePlayer;
                case "removeAll" -> func_removeAll;

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

        private void addPlayer(LuaValue self, LuaValue arg) {
            self.checkuserdata(BossBar.class).addPlayer(arg.checkuserdata(Player.class));
        }

        private void removePlayer(LuaValue self, LuaValue arg) {
            self.checkuserdata(BossBar.class).removePlayer(arg.checkuserdata(Player.class));
        }

        private void removeAll(LuaValue self) {
            self.checkuserdata(BossBar.class).removeAll();
        }

        private LuaValue onToString() {
            return LuaValue.valueOf("BossBar");
        }
    }
}
