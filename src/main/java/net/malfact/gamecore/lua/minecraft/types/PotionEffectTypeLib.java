package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public final class PotionEffectTypeLib extends TypeLib<PotionEffectType> {

    public PotionEffectTypeLib() {
        super(PotionEffectType.class,"PotionEffectType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var type = LuaUtil.toPotionEffectType(key);
        if (type == null)
            return NIL;

        return getUserdataOf(type);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue t, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        PotionEffectType type = t.checkuserdata(PotionEffectType.class);

        return switch (key.tojstring()) {
            case "isInstant" -> LuaValue.valueOf(type.isInstant());
            case "category" -> LuaValue.valueOf(type.getCategory().toString());
            case "color" -> LuaValue.valueOf(type.getColor().toString());
//                case "attributes"
            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(PotionEffectType.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        PotionEffectType a = LuaUtil.toPotionEffectType(arg1);
        PotionEffectType b = LuaUtil.toPotionEffectType(arg2);

        return a == b;
    }

    @Override
    protected String getUserdataType() {
        return "potion_effect_type";
    }
}
