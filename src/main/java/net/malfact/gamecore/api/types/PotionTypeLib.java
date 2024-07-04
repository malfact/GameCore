package net.malfact.gamecore.api.types;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.potion.PotionType;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public class PotionTypeLib extends TypeLib {

    public PotionTypeLib() {
        super("PotionType");
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var type = LuaApi.toPotionType(key);
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
//                case "effectTypes" -> // ToDo
            case "isUpgradeable" -> valueOf(type.isUpgradeable());
            case "isExtendable" -> valueOf(type.isExtendable());
            case "maxLevel" -> valueOf(type.getMaxLevel());
            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(PotionType.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        PotionType a = LuaApi.toPotionType(arg1);
        PotionType b = LuaApi.toPotionType(arg2);

        return a == b;
    }
}
