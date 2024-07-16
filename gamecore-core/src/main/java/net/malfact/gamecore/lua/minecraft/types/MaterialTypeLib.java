package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.Material;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public class MaterialTypeLib extends TypeLib<Material> {

    public MaterialTypeLib() {
        super(Material.class,"Material");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var material = LuaUtil.toMaterial(key);
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
            case "maxStackSize" ->  LuaValue.valueOf(material.getMaxStackSize());
            case "maxDurability" -> LuaValue.valueOf(material.getMaxDurability());
            case "isBlock" ->       LuaValue.valueOf(material.isBlock());
            case "isRecord" ->      LuaValue.valueOf(material.isRecord());
            case "isSolid" ->       LuaValue.valueOf(material.isSolid());
            case "isAir" ->         LuaValue.valueOf(material.isAir());
            case "isFlammable" ->   LuaValue.valueOf(material.isFlammable());
            case "isBurnable" ->    LuaValue.valueOf(material.isBurnable());
            case "isFuel" ->        LuaValue.valueOf(material.isFuel());
            case "isOccluding" ->   LuaValue.valueOf(material.isOccluding());
            case "hasGravity" ->    LuaValue.valueOf(material.hasGravity());
            case "isItem" ->        LuaValue.valueOf(material.isItem());
            case "hardness" ->      LuaValue.valueOf(material.getHardness());
            case "slipperiness" ->  LuaValue.valueOf(material.getSlipperiness());
            case "isCompostable" -> LuaValue.valueOf(material.isCompostable());
            case "compostChance" -> material.isCompostable() ? LuaValue.valueOf(material.getCompostChance()) : NIL;

            default -> NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(Material.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        Material a = LuaUtil.toMaterial(arg1);
        Material b = LuaUtil.toMaterial(arg2);

        return a == b;
    }

    @Override
    protected String getUserdataType() {
        return "material";
    }

}
