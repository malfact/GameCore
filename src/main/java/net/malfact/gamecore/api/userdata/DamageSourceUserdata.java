package net.malfact.gamecore.api.userdata;

import net.malfact.gamecore.script.Instance;
import org.bukkit.damage.DamageSource;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

@SuppressWarnings("UnstableApiUsage")
public class DamageSourceUserdata extends InstancedUserdataProvider {

    public DamageSourceUserdata(Instance instance) {
        super(instance);
    }

    @Override
    public boolean accepts(Object o) {
        return o instanceof DamageSource;
    }

    @Override
    public LuaValue getUserdataOf(Object o) {
        if (!accepts(o))
            return LuaConstant.NIL;

        DamageSource source = (DamageSource) o;

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, new Index());
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(source, meta);
    }

    private class Index extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue userdata, LuaValue key) {
            if (!key.isstring())
                return LuaConstant.NIL;

            DamageSource source = userdata.checkuserdata(DamageSource.class);

            return switch (key.tojstring()) {
                case "damageType" -> valueOf(source.getDamageType().key().asMinimalString());
                case "causingEntity" -> instance.getUserdataOf(source.getCausingEntity());
                case "directEntity" -> instance.getUserdataOf(source.getDirectEntity());
                case "damageLocation" -> instance.getUserdataOf(source.getDamageLocation());
                case "sourceLocation" -> instance.getUserdataOf(source.getSourceLocation());
                case "isIndirect" -> valueOf(source.isIndirect());
                case "foodExhaustion" -> valueOf(source.getFoodExhaustion());
                case "scalesWithDifficult" -> valueOf(source.scalesWithDifficulty());
                default -> LuaConstant.NIL;
            };
        }
    }
}
