package net.malfact.gamecore.lua.minecraft.entity;

import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import org.bukkit.DyeColor;
import org.bukkit.entity.TextDisplay;
import org.luaj.vm2.LuaValue;

public class TextDisplayHandler extends DisplayHandler<TextDisplay> {

    public TextDisplayHandler() {
        super(TextDisplay.class);
    }

    @Override
    protected LuaValue get(Game instance, TextDisplay display, String key) {
        return switch (key) {
            case "text" ->              LuaValue.valueOf(LuaUtil.fromComponent(display.text()));
            case "lineWidth" ->         LuaValue.valueOf(display.getLineWidth());
            case "backgroundColor" ->   LuaValue.valueOf(display.getBackgroundColor().toString());
            case "textOpacity" ->       LuaValue.valueOf(display.getTextOpacity());
            case "shadowed" ->          LuaValue.valueOf(display.isShadowed());
            case "seeThrough" ->        LuaValue.valueOf(display.isSeeThrough());
            case "defaultBackground" -> LuaValue.valueOf(display.isDefaultBackground());
            case "alignment" ->         LuaUtil.valueOf(display.getAlignment());

            default -> super.get(instance, display, key);
        };
    }

    @Override
    protected void set(Game instance, TextDisplay display, String key, LuaValue value) {
        switch (key) {
            case "text" ->              display.text(value.isnil() ? null : LuaUtil.toComponent(value.tojstring()));
            case "lineWidth" ->         display.setLineWidth(value.checkint());
            case "backgroundColor" ->   display.setBackgroundColor(LuaUtil.toEnum(value, DyeColor.class).getColor());
            case "textOpacity" ->       display.setTextOpacity(value.checknumber().tobyte());
            case "shadowed" ->          display.setShadowed(value.checkboolean());
            case "seeThrough" ->        display.setSeeThrough(value.checkboolean());
            case "defaultBackground" -> display.setDefaultBackground(value.checkboolean());
            case "alignment" ->         display.setAlignment(LuaUtil.toEnum(value, TextDisplay.TextAlignment.class));

            default -> super.set(instance, display, key, value);
        }
    }

    @Override
    protected String type() {
        return "text_display";
    }
}
