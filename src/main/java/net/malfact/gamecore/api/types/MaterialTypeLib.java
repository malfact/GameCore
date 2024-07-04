package net.malfact.gamecore.api.types;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.Material;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public class MaterialTypeLib extends TypeLib {

    public MaterialTypeLib() {
        super("Material");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var material = LuaApi.toMaterial(key);
        if (material == null)
            return NIL;

        return getUserdataOf(material);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue type, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        Material material = type.checkuserdata(Material.class);

        return switch (key.tojstring()) {
            case "maxStackSize" -> valueOf(material.getMaxStackSize());
            case "maxDurability" -> valueOf(material.getMaxDurability());
            case "isBlock" -> valueOf(material.isBlock());
            case "isRecord" -> valueOf(material.isRecord());
            case "isSolid" -> valueOf(material.isSolid());
            case "isAir" -> valueOf(material.isAir());
            case "isFlammable" -> valueOf(material.isFlammable());
            case "isBurnable" -> valueOf(material.isBurnable());
            case "isFuel" -> valueOf(material.isFuel());
            case "isOccluding" -> valueOf(material.isOccluding());
            case "hasGravity" -> valueOf(material.hasGravity());
            case "isItem" -> valueOf(material.isItem());
            case "hardness" -> valueOf(material.getHardness());
            case "slipperiness" -> valueOf(material.getSlipperiness());
            case "isCompostable" -> valueOf(material.isCompostable());
            case "compostChance" -> material.isCompostable() ? valueOf(material.getCompostChance()) : LuaConstant.NIL;

            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(Material.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        Material a = LuaApi.toMaterial(arg1);
        Material b = LuaApi.toMaterial(arg2);

        return a == b;
    }
}
