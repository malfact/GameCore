package net.malfact.gamecore.api.inventory;

import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.script.Instance;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class ItemStackLib extends InstancedLib implements UserdataProvider {

    public ItemStackLib(Instance instance) {
        super(instance);
    }

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        LuaTable lib = new LuaTable();
        lib.set("new", ItemStack_new);
        env.set("ItemStack", lib);
        return env;
    }

    /* --- --- Constructor --- --- */

    private final LuaFunction ItemStack_new = new VarArgFunction() {
        @Override
        public Varargs invoke(Varargs args) {
            var material = LuaApi.checkMaterial(args.arg1());
            var amount = Math.max(1,args.optint(2, 1));
            return getUserdataOf(new ItemStack(material, amount));
        }
    };

    /* --- --- Instance Methods --- --- */

    private final LuaFunction ItemStack_clone = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue userdata) {
            var item = userdata.checkuserdata(ItemStack.class);
            return getUserdataOf(item.clone());
        }
    };

    private static final LuaFunction ItemStack_getEnchant = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var item = arg1.checkuserdata(ItemStack.class);
            var enchant = LuaApi.checkEnchantment(arg2);

            return item.containsEnchantment(enchant) ? valueOf(item.getEnchantmentLevel(enchant)) : LuaConstant.NIL;
        }
    };

    private static final LuaFunction ItemStack_addEnchant = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            var item = arg1.checkuserdata(ItemStack.class);
            var enchant = LuaApi.checkEnchantment(arg2);
            var level = arg3.checkint();

            item.addUnsafeEnchantment(enchant, Math.max(0, level-1));

            return LuaConstant.NIL;
        }
    };

    private static final LuaFunction ItemStack_removeEnchant = new VarArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var item = arg1.checkuserdata(ItemStack.class);

            if (arg2.isnil())
                item.removeEnchantments();
            else
                item.removeEnchantment(LuaApi.checkEnchantment(arg2));

            return LuaConstant.NIL;
        }
    };

    /* --- --- MetaMethods --- --- */

    private final LuaFunction ItemStack_index = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue userdata, LuaValue key) {
            ItemStack item = userdata.checkuserdata(ItemStack.class);

            return switch (key.tojstring()) {
                case "type" -> LuaApi.getValueOf(item.getType());
                case "amount" -> valueOf(item.getAmount());
                case "isEmpty" -> valueOf(item.isEmpty());
                case "itemMeta",
                     "meta" -> instance.getUserdataOf(item.getItemMeta());

                case "clone" -> ItemStack_clone;
                case "getEnchant" -> ItemStack_getEnchant;
                case "addEnchant" -> ItemStack_addEnchant;
                case "removeEnchant" -> ItemStack_removeEnchant;
                default -> LuaConstant.NIL;
            };
        }
    };

    private static final LuaFunction ItemStack_newindex = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue userdata, LuaValue key, LuaValue value) {
            ItemStack item = userdata.checkuserdata(ItemStack.class);

            switch (key.tojstring()) {
                case "amount" -> item.setAmount(value.checkint());
                case "itemMeta",
                     "meta" -> item.setItemMeta(value.isnil() ? null : value.checkuserdata(ItemMeta.class));
            }

            return LuaConstant.NIL;
        }
    };

    private static final LuaFunction ItemStack_tostring = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue userdata) {
            var item = userdata.checkuserdata(ItemStack.class);
            var count = item.getAmount() > 1 ? "@" + item.getAmount() : "";
            return valueOf("ItemStack<" + LuaApi.getKey(item.getType()) + ">" + count);
        }
    };

    @Override
    public boolean accepts(Object o) {
        return o instanceof ItemStack;
    }

    @Override
    public LuaValue getUserdataOf(Object o) {
        if (!accepts(o))
            return LuaConstant.NIL;

        ItemStack item = (ItemStack) o;

        LuaTable meta = new LuaTable();

        meta.set(LuaConstant.MetaTag.INDEX, ItemStack_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX, ItemStack_newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING, ItemStack_tostring);
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(item, meta);
    }
}
