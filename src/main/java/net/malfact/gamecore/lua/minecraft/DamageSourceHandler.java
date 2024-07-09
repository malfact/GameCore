package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.damage.DamageSource;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;

@SuppressWarnings("UnstableApiUsage")
public class DamageSourceHandler implements TypeHandler<DamageSource> {

    @Override
    public Class<DamageSource> getTypeClass() {
        return DamageSource.class;
    }

    @Override
    public LuaValue getUserdataOf(DamageSource object) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, LuaUtil.toFunction(DamageSourceHandler::onIndex));

        meta.set("__userdata_type__", "damage_source");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(object, meta);
    }

    private static LuaValue onIndex(LuaValue self, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        DamageSource source = self.checkuserdata(DamageSource.class);

        return switch (key.tojstring()) {
            case "damageType" ->        LuaApi.valueOf(source.getDamageType().key().asMinimalString());
            case "causingEntity" ->     LuaApi.userdataOf(source.getCausingEntity());
            case "directEntity" ->      LuaApi.userdataOf(source.getDirectEntity());
            case "damageLocation" ->    LuaApi.userdataOf(source.getDamageLocation());
            case "sourceLocation" ->    LuaApi.userdataOf(source.getSourceLocation());
            case "isIndirect" ->        LuaApi.valueOf(source.isIndirect());
            case "foodExhaustion" ->    LuaApi.valueOf(source.getFoodExhaustion());
            case "scalesWithDifficult" -> LuaApi.valueOf(source.scalesWithDifficulty());
            default -> LuaConstant.NIL;
        };
    }
}
