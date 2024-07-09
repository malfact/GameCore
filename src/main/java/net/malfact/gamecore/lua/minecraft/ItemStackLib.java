package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.*;

public class ItemStackLib implements LuaLib, TypeHandler<ItemStack> {

    private static final LuaFunction func_new =         LuaUtil.toVarargFunction(ItemStackLib::createItemStack);
    private static final LuaFunction func_index =       LuaUtil.toFunction(ItemStackLib::onIndex);
    private static final LuaFunction func_newindex =    LuaUtil.toFunction(ItemStackLib::onNewIndex);
    private static final LuaFunction func_toString =    LuaUtil.toFunction(ItemStackLib::onToString);
    private static final LuaFunction func_clone =       LuaUtil.toFunction(ItemStackLib::onClone);
    private static final LuaFunction func_getEnchant =  LuaUtil.toFunction(ItemStackLib::getEnchant);
    private static final LuaFunction func_addEnchant =  LuaUtil.toFunction(ItemStackLib::addEnchant);
    private static final LuaFunction func_removeEnchant = LuaUtil.toFunction(ItemStackLib::removeEnchant);

    @Override
    public void load(LuaValue env) {
        LuaTable lib = new LuaTable();
        lib.set("new", func_new);
        env.set("ItemStack", lib);
    }

    /* --- --- Constructor --- --- */

    private static LuaValue createItemStack(Varargs args) {
        var material = LuaUtil.checkMaterial(args.arg1());
        var amount = Math.max(1,args.optint(2, 1));
        return userdataOf(new ItemStack(material, amount));
    }

    /* --- --- Instance Methods --- --- */

    private static LuaValue onClone(LuaValue arg) {
        var item = arg.checkuserdata(ItemStack.class);
        return userdataOf(item.clone());
    }

    private static LuaValue getEnchant(LuaValue arg1, LuaValue arg2) {
        var item = arg1.checkuserdata(ItemStack.class);
        var enchant = LuaUtil.checkEnchantment(arg2);

        return item.containsEnchantment(enchant)
            ? LuaValue.valueOf(item.getEnchantmentLevel(enchant))
            : LuaConstant.NIL;
    }

    private static LuaValue addEnchant(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        var item = arg1.checkuserdata(ItemStack.class);
        var enchant = LuaUtil.checkEnchantment(arg2);
        var level = arg3.checkint();

        item.addUnsafeEnchantment(enchant, Math.max(0, level-1));

        return LuaConstant.NIL;
    }

    private static void removeEnchant(LuaValue arg1, LuaValue arg2) {
        var item = arg1.checkuserdata(ItemStack.class);

        if (arg2.isnil())
            item.removeEnchantments();
        else
            item.removeEnchantment(LuaUtil.checkEnchantment(arg2));
    }

    /* --- --- MetaMethods --- --- */

    private static LuaValue onIndex(LuaValue self, LuaValue key) {
        ItemStack item = self.checkuserdata(ItemStack.class);
        return switch (key.tojstring()) {
            case "type" ->          LuaApi.userdataOf(item.getType());
            case "amount" ->        LuaValue.valueOf(item.getAmount());
            case "isEmpty" ->       LuaValue.valueOf(item.isEmpty());
            case "itemMeta",
                 "meta" ->          LuaApi.userdataOf(item.getItemMeta());

            case "clone" ->         func_clone;
            case "getEnchant" ->    func_getEnchant;
            case "addEnchant" ->    func_addEnchant;
            case "removeEnchant" -> func_removeEnchant;
            default -> LuaConstant.NIL;
        };
    }

    private static void onNewIndex(LuaValue self, LuaValue key, LuaValue value) {
        ItemStack item = self.checkuserdata(ItemStack.class);

        switch (key.tojstring()) {
            case "amount" -> item.setAmount(value.checkint());
            case "itemMeta",
                 "meta" -> item.setItemMeta(value.isnil() ? null : value.checkuserdata(ItemMeta.class));
        }
    }

    private static LuaValue onToString(LuaValue self) {
        var item = self.checkuserdata(ItemStack.class);
        var count = item.getAmount() > 1 ? "@" + item.getAmount() : "";
        return LuaValue.valueOf("ItemStack<" + LuaUtil.getKey(item.getType()) + ">" + count);
    }

    // ----- ----- Type Handler ----- -----

    @Override
    public Class<ItemStack> getTypeClass() {
        return ItemStack.class;
    }

    @Override
    public LuaValue getUserdataOf(ItemStack item) {
        return userdataOf(item);
    }

    public static LuaValue userdataOf(ItemStack item) {
        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX,     func_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX,  func_newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING,  func_toString);

        meta.set("__userdata_type__", "item_stack");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(item, meta);
    }
}
