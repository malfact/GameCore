package net.malfact.gamecore.lua;

import org.bukkit.Color;
import org.bukkit.DyeColor;

public enum LuaColor {
    WHITE   (Color.WHITE,   DyeColor.WHITE),
    SILVER  (Color.SILVER,  null),
    GRAY    ( Color.GRAY,   DyeColor.GRAY),
    BLACK   (Color.BLACK,   DyeColor.BLACK),
    RED     (Color.RED,     DyeColor.RED),
    MAROON  (Color.MAROON,  null),
    YELLOW  (Color.YELLOW,  DyeColor.YELLOW),
    OLIVE   (Color.OLIVE,   null),
    LIME    (Color.LIME,    DyeColor.LIME),
    GREEN   (Color.GREEN,   DyeColor.GREEN),
    AQUA    (Color.AQUA,    null),
    TEAL    (Color.TEAL,    null),
    BLUE    (Color.BLUE,    DyeColor.BLUE),
    NAVY    (Color.NAVY,    null),
    FUCHSIA (Color.FUCHSIA, null),
    PURPLE  (Color.PURPLE,  DyeColor.PURPLE),
    ORANGE  (Color.ORANGE,  DyeColor.ORANGE),

    MAGENTA     (DyeColor.MAGENTA),
    LIGHT_BLUE  (DyeColor.LIGHT_BLUE),
    PINK        (DyeColor.PINK),
    LIGHT_GRAY  (DyeColor.LIGHT_GRAY),
    CYAN        (DyeColor.CYAN),
    BROWN       (DyeColor.BROWN)
    ;
    final Color color;
    final DyeColor dyeColor;

    LuaColor(DyeColor dyeColor) {
        this(dyeColor.getColor(), dyeColor);
    }

    LuaColor(Color color, DyeColor dyeColor) {
        this.color = color;
        this.dyeColor = dyeColor;
    }
}
