package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.script.Instance;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

public class TextDisplayEntityHandler extends EntityHandler {

    @Override
    public boolean acceptsEntity(Entity entity) {
        return entity instanceof TextDisplay;
    }

    @Override
    public @NotNull LuaValue onIndex(Instance instance, Entity entity, LuaValue key) {
        if (!key.isstring())
            return NIL;

        TextDisplay display = checkEntity(instance, entity, TextDisplay.class);

        return switch (key.tojstring()) {
            case "text" ->              LuaValue.valueOf(LuaApi.fromComponent(display.text()));
            case "lineWidth" ->         LuaValue.valueOf(display.getLineWidth());
            case "backgroundColor" ->   LuaValue.valueOf(display.getBackgroundColor().toString());
            case "textOpacity" ->       LuaValue.valueOf(display.getTextOpacity());
            case "shadowed" ->          LuaValue.valueOf(display.isShadowed());
            case "seeThrough" ->        LuaValue.valueOf(display.isSeeThrough());
            case "defaultBackground" -> LuaValue.valueOf(display.isDefaultBackground());
            case "alignment" ->         instance.getValueOf(display.getAlignment());
            default -> NIL;
        };
    }

    @Override
    public boolean onNewIndex(Instance instance, Entity entity, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return false;

        TextDisplay display = checkEntity(instance, entity, TextDisplay.class);
        switch (key.tojstring()) {
            case "text" -> display.text(value.isnil() ? null : LuaApi.toComponent(value.tojstring()));
            case "lineWidth" -> display.setLineWidth(value.checkint());
            case "backgroundColor" -> display.setBackgroundColor(LuaApi.toEnum(value.checkjstring(), DyeColor.class).getColor());
            case "textOpacity" -> display.setTextOpacity(value.checknumber().tobyte());
            case "shadowed" -> display.setShadowed(value.checkboolean());
            case "seeThrough" -> display.setSeeThrough(value.checkboolean());
            case "defaultBackground" -> display.setDefaultBackground(value.checkboolean());
            case "alignment" -> display.setAlignment(LuaApi.toEnum(value.checkjstring(), TextDisplay.TextAlignment.class));
            default -> {return false;}
        }

        return true;
    }
}
