package net.malfact.gamecore.api.types;

import net.malfact.gamecore.api.LuaApi;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class EnchantTypeLib extends TypeLib {

    public EnchantTypeLib() {
        super("Enchantment");
    }

    @Override
    protected void onLibBuild(LuaTable lib) {
        lib.set("conflictsWith", enchantment_conflictsWith);
        lib.set("canEnchantItem", enchantment_canEnchantItem);
        lib.set("displayName", enchantment_displayName);
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var enchant = LuaApi.toEnchantment(key);
        if (enchant == null)
            return NIL;

        return userdataOf(enchant);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue type, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        Enchantment enchantment = type.checkuserdata(Enchantment.class);

        return switch (key.tojstring()) {
            case "maxLevel" -> valueOf(enchantment.getMaxLevel() + 1);
            case "startLevel" -> valueOf(enchantment.getStartLevel() + 1);
            case "isCursed" -> valueOf(enchantment.isCursed());
            case "isTradeable" -> valueOf(enchantment.isTradeable());
            case "isDiscoverable" -> valueOf(enchantment.isDiscoverable());
            case "anvilCost" -> valueOf(enchantment.getAnvilCost());

            case "conflictsWith" -> enchantment_conflictsWith;
            case "canEnchantItem" -> enchantment_canEnchantItem;
            case "displayName" -> enchantment_displayName;

            default -> LuaConstant.NIL;
        };
    }

    @Override
    protected String onTypeToString(LuaValue value) {
        return value.checkuserdata(Enchantment.class).getKey().asMinimalString();
    }

    @Override
    protected boolean onTypeEquals(LuaValue arg1, LuaValue arg2) {
        Enchantment a = LuaApi.toEnchantment(arg1);
        Enchantment b = LuaApi.toEnchantment(arg2);

        if (a == null && b == null)
            return true;
        else if (a == null || b == null)
            return false;

        return a.equals(b);
    }

    /* --- --- Instance Methods --- --- */

    private static final LuaFunction enchantment_conflictsWith = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var a = LuaApi.toEnchantment(arg1);
            var b = LuaApi.toEnchantment(arg2);

            if (a == null || b == null)
                return LuaConstant.FALSE;

            return valueOf(a.conflictsWith(b));
        }
    };

    private static final LuaFunction enchantment_canEnchantItem = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var enchant = LuaApi.toEnchantment(arg1);

            if (enchant == null)
                return argumentError(1, "Enchantment expected, got " + arg2.getType());

            return valueOf(enchant.canEnchantItem(arg2.checkuserdata(ItemStack.class)));
        }
    };

    private static final LuaFunction enchantment_displayName = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var enchant = LuaApi.toEnchantment(arg1);

            if (enchant == null)
                return argumentError(1, "Enchantment expected, got " + arg2.getType());

            var level = Math.clamp(arg2.checkint()-1, enchant.getStartLevel(), enchant.getMaxLevel());
            return valueOf(enchant.displayName(level).toString());
        }
    };
}
