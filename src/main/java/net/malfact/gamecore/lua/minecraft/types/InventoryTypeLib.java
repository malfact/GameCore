package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.event.inventory.InventoryType;
import org.luaj.vm2.LuaValue;

import java.util.Locale;

public final class InventoryTypeLib extends TypeLib<InventoryType> {

    public InventoryTypeLib() {
        super(InventoryType.class, "InventoryType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var inventoryType = LuaUtil.toEnum(key, InventoryType.class);
        if (inventoryType == null)
            return NIL;

        return getUserdataOf(inventoryType);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue type, LuaValue key) {
        if (!key.isstring())
            return NIL;

        InventoryType inventoryType = type.checkuserdata(InventoryType.class);

        return switch (key.tojstring()) {
            case "defaultSize" -> LuaValue.valueOf(inventoryType.getDefaultSize());
            case "defaultTitle" -> //noinspection deprecation
                LuaValue.valueOf(inventoryType.getDefaultTitle());
            case "isCreatable" -> LuaValue.valueOf(inventoryType.isCreatable());

            default -> NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(InventoryType.class).toString().toLowerCase(Locale.ROOT);
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {

        return LuaUtil.toEnum(arg1, InventoryType.class) == LuaUtil.toEnum(arg2, InventoryType.class);
    }

    @Override
    protected String getUserdataType() {
        return "inventory_type";
    }
}
