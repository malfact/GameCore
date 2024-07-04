package net.malfact.gamecore.api.types;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public class PotionEffectTypeLib extends TypeLib {

    public PotionEffectTypeLib() {
        super("PotionEffectType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var type = LuaApi.toPotionEffectType(key);
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
            case "isInstant" -> valueOf(type.isInstant());
            case "category" -> valueOf(type.getCategory().toString());
            case "color" -> valueOf(type.getColor().toString());
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
        PotionEffectType a = LuaApi.toPotionEffectType(arg1);
        PotionEffectType b = LuaApi.toPotionEffectType(arg2);

        return a == b;
    }
}
