package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.luaj.vm2.*;

public class AttributeModifierLib implements LuaLib, TypeHandler<AttributeModifier> {

    private final LuaFunction func_new =                LuaUtil.toVarargFunction(this::createAttributeModifier);
    private final LuaFunction func_modifierIndex =      LuaUtil.toFunction(this::modifierOnIndex);
    private final LuaFunction func_modifierToString =   LuaUtil.toFunction(this::modifierToString);
    private final LuaFunction func_equals =             LuaUtil.toFunction(this::modifierEquals);

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("new",      func_new);
        lib.set("equals",   func_equals);

        env.set("AttributeModifier", lib);
    }

    @Override
    public Class<AttributeModifier> getTypeClass() {
        return AttributeModifier.class;
    }

    @Override
    public LuaValue getUserdataOf(AttributeModifier modifier) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, func_modifierIndex);
        meta.set(LuaConstant.MetaTag.TOSTRING, func_modifierToString);
        meta.set(LuaConstant.MetaTag.EQ, func_equals);

        meta.set("__userdata_type__", "attribute_modifier");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(modifier, meta);
    }

    @SuppressWarnings("UnstableApiUsage")
    public LuaValue createAttributeModifier(Varargs args) {
        NamespacedKey key = LuaUtil.toNamespacedKey(args.arg1().checkjstring());
        double amount = args.arg(2).checkdouble();
        AttributeModifier.Operation operation = LuaUtil.toEnum(args.arg(3), AttributeModifier.Operation.class);
        EquipmentSlotGroup slotGroup = LuaUtil.toEquipmentSlotGroup(args.arg(4));
        if (slotGroup == null)
            slotGroup = EquipmentSlotGroup.ANY;

        return getUserdataOf(new AttributeModifier(key, amount, operation, slotGroup));
    }

    @SuppressWarnings("UnstableApiUsage")
    private LuaValue modifierOnIndex(LuaValue self, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        AttributeModifier modifier = self.checkuserdata(AttributeModifier.class);

        return switch (key.tojstring()) {
            case "name" ->      LuaValue.valueOf(modifier.getKey().asMinimalString());
            case "amount" ->    LuaValue.valueOf(modifier.getAmount());
            case "operation" -> LuaUtil.valueOf(modifier.getOperation());
            case "slotGroup" -> LuaValue.valueOf(modifier.getSlotGroup().toString());
            default -> LuaConstant.NIL;
        };
    }

    private LuaValue modifierToString(LuaValue self) {
        return LuaValue.valueOf(self.checkuserdata(AttributeModifier.class).toString());
    }

    private LuaValue modifierEquals(LuaValue self, LuaValue other) {
        if (self.isnil() && other.isnil())
            return LuaConstant.TRUE;

        if (!self.isuserdata() || !other.isuserdata())
            return LuaConstant.FALSE;

        return LuaValue.valueOf(self.touserdata().equals(other.touserdata()));
    }
}
