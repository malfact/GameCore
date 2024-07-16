package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.luaj.vm2.*;

public class AttributeLib implements LuaLib, TypeHandler<AttributeInstance> {

    private final LuaFunction func_attributeOnIndex =   LuaUtil.toFunction(this::attributeOnIndex);
    private final LuaFunction func_attributeToString =  LuaUtil.toFunction(this::attributeToString);
    private final LuaFunction func_getModifier =        LuaUtil.toFunction(this::getModifier);
    private final LuaFunction func_addModifier =        LuaUtil.toFunction(this::addModifier);
    private final LuaFunction func_removeModifier =     LuaUtil.toFunction(this::removeModifier);

    @Override
    public LuaValue getUserdataOf(AttributeInstance attribute) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, func_attributeOnIndex);
        meta.set(LuaConstant.MetaTag.TOSTRING, func_attributeToString);

        meta.set("__userdata_type__", "attribute_instance");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(attribute, meta);
    }

    private LuaValue attributeOnIndex(LuaValue self, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        AttributeInstance attribute = self.checkuserdata(AttributeInstance.class);
        
        return switch (key.tojstring()) {
            case "defaultValue" ->      LuaApi.valueOf(attribute.getDefaultValue());
            case "baseValue" ->         LuaApi.valueOf(attribute.getBaseValue());
            case "value" ->             LuaApi.valueOf(attribute.getValue());
            case "attribute" ->         LuaApi.userdataOf(attribute.getAttribute());
            case "getModifier" ->       func_getModifier;
            case "addModifier" ->       func_addModifier;
            case "removeModifier" ->    func_removeModifier;

            default -> LuaConstant.NIL;
        };
    }

    private LuaValue attributeToString(LuaValue self) {
        return LuaValue.valueOf("AttributeInstance");
    }

    private LuaValue getModifier(LuaValue self, LuaValue arg) {
        NamespacedKey key = LuaUtil.toNamespacedKey(arg.checkjstring());
        if (key == null)
            return LuaConstant.NIL;

        return LuaApi.userdataOf(self.checkuserdata(AttributeInstance.class).getModifier(key));
    }

    private void addModifier(LuaValue self, LuaValue arg) {
        self.checkuserdata(AttributeInstance.class).addModifier(arg.checkuserdata(AttributeModifier.class));
    }

    private void removeModifier(LuaValue self, LuaValue arg) {
        if (arg.isuserdata(AttributeModifier.class))
            self.checkuserdata(AttributeInstance.class).removeModifier(arg.touserdata(AttributeModifier.class));
        else
            self.checkuserdata(AttributeInstance.class).removeModifier(LuaUtil.toNamespacedKey(arg.checkjstring()));
    }

    @Override
    public void load(LuaValue env) {}
}
