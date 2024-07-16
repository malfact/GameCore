package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.potion.PotionType;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public final class PotionTypeLib extends TypeLib<PotionType> {

    public PotionTypeLib() {
        super(PotionType.class,"PotionType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var type = LuaUtil.toPotionType(key);
        if (type == null)
            return NIL;

        return getUserdataOf(type);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue t, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        PotionType type = t.checkuserdata(PotionType.class);

        return switch (key.tojstring()) {
//            case "effectTypes" -> type.getPotionEffects() //ToDo
            case "isUpgradeable" -> LuaValue.valueOf(type.isUpgradeable());
            case "isExtendable" -> LuaValue.valueOf(type.isExtendable());
            case "maxLevel" -> LuaValue.valueOf(type.getMaxLevel());
            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(PotionType.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        PotionType a = LuaUtil.toPotionType(arg1);
        PotionType b = LuaUtil.toPotionType(arg2);

        return a == b;
    }

    @Override
    protected String getUserdataType() {
        return "potion_type";
    }
}
