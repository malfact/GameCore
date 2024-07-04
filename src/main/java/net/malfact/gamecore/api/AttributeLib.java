package net.malfact.gamecore.api;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class AttributeLib extends TwoArgFunction {

    private static LuaTable library;
    private static final LuaFunction ADD_MODIFIER = new AttributeAddModifier();
    private static final LuaFunction GET_MODIFIER = new AttributeGetModifier();
    private static final LuaFunction REMOVE_MODIFIER = new AttributeRemoveModifier();
    private static final LuaFunction ATTRIBUTE_META_INDEX = new AttributeIndex();
    private static final LuaFunction MODIFIER_CONSTRUCTOR = new ModifierConstructor();
    private static final LuaFunction MODIFIER_META_INDEX = new ModifierIndex();

    @Override
    public LuaValue call(LuaValue name, LuaValue env) {
        if (library == null) {
            library = new LuaTable();

            library.set("removeModifier", REMOVE_MODIFIER);
            library.set("getModifier", GET_MODIFIER);
            library.set("addModifier", ADD_MODIFIER);

            LuaTable attribute = new LuaTable();
            attribute.set("new", MODIFIER_CONSTRUCTOR);
            library.set("Modifier",attribute);
        }

        env.set("Attribute", library);
        return env;
    }

    public static LuaUserdata userdataOf(AttributeInstance instance) {
        LuaUserdata userdata = new LuaUserdata(instance);

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, ATTRIBUTE_META_INDEX);
        userdata.setmetatable(LuaApi.readOnly(meta));

        return userdata;
    }

    public static LuaUserdata userdataOf(AttributeModifier modifier) {
        LuaUserdata userdata = new LuaUserdata(modifier);

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, MODIFIER_META_INDEX);
        userdata.setmetatable(LuaApi.readOnly(meta));

        return userdata;
    }

    private static class AttributeRemoveModifier extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue self, LuaValue key) {
            self.checkuserdata(AttributeInstance.class).removeModifier(LuaApi.toNamespacedKey(key.checkjstring()));
            return LuaConstant.NIL;
        }
    }

    private static class AttributeGetModifier extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue self, LuaValue key) {
            return AttributeLib.userdataOf(self.checkuserdata(AttributeInstance.class).getModifier(LuaApi.toNamespacedKey(key.checkjstring())));
        }
    }

    private static class AttributeAddModifier extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue self, LuaValue value) {
            self.checkuserdata(AttributeInstance.class).addModifier(value.checkuserdata(AttributeModifier.class));
            return LuaConstant.NIL;
        }
    }

    private static class AttributeIndex extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue userdata, LuaValue key) {
            AttributeInstance instance = userdata.checkuserdata(AttributeInstance.class);

            if (key.isstring())
                return switch (key.tojstring()) {
                    case "defaultValue" -> valueOf(instance.getDefaultValue());
                    case "baseValue" -> valueOf(instance.getBaseValue());
                    case "value" -> valueOf(instance.getValue());
                    case "attribute" -> valueOf(instance.getAttribute().getKey().asMinimalString());
                    case "addModifier" -> ADD_MODIFIER;
                    case "getModifier" -> GET_MODIFIER;
                    case "removeModifier" -> REMOVE_MODIFIER;
                    default -> LuaConstant.NIL;
                };

            return super.get(key);
        }
    }

    private static class ModifierConstructor extends VarArgFunction {
        @SuppressWarnings("UnstableApiUsage")
        @Override
        public Varargs invoke(Varargs args) {
            NamespacedKey key = LuaApi.toNamespacedKey(args.arg1().checkjstring());
            double amount = args.arg(2).checkdouble();
            AttributeModifier.Operation operation = LuaApi.toEnum(args.arg(3).checkjstring(), AttributeModifier.Operation.class);
            EquipmentSlotGroup slotGroup = LuaApi.getEquipmentSlotGroup(args.arg(4).checkjstring());
            if (slotGroup == null)
                slotGroup = EquipmentSlotGroup.ANY;

            return AttributeLib.userdataOf(new AttributeModifier(key, amount, operation, slotGroup));
        }
    }

    private static class ModifierIndex extends TwoArgFunction {

        @SuppressWarnings("UnstableApiUsage")
        @Override
        public LuaValue call(LuaValue self, LuaValue key) {
            AttributeModifier modifier = self.checkuserdata(AttributeModifier.class);

            if (key.isstring()) {
                return switch (key.tojstring()) {
                    case "name" -> valueOf(modifier.getKey().asMinimalString());
                    case "amount" -> valueOf(modifier.getAmount());
                    case "operation" -> valueOf(modifier.getOperation().toString());
                    case "slotGroup" -> valueOf(modifier.getSlotGroup().toString());
                    default -> LuaConstant.NIL;
                };
            }

            return super.get(key);
        }
    }

}
