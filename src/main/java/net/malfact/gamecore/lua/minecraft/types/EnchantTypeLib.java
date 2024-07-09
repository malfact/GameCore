package net.malfact.gamecore.lua.minecraft.types;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public final class EnchantTypeLib extends TypeLib<Enchantment> {

    public EnchantTypeLib() {
        super(Enchantment.class,"Enchantment");
    }

    @Override
    protected void onLibBuild(LuaTable lib) {
        lib.set("conflictsWith", enchantment_conflictsWith);
        lib.set("canEnchantItem", enchantment_canEnchantItem);
        lib.set("displayName", enchantment_displayName);
    }

    @Override
    protected LuaValue onLibIndex(LuaValue key) {
        var enchant = LuaUtil.toEnchantment(key);
        if (enchant == null)
            return NIL;

        return getUserdataOf(enchant);
    }

    @Override
    protected LuaValue onTypeIndex(LuaValue type, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        Enchantment enchantment = type.checkuserdata(Enchantment.class);

        return switch (key.tojstring()) {
            case "maxLevel" ->          LuaValue.valueOf(enchantment.getMaxLevel() + 1);
            case "startLevel" ->        LuaValue.valueOf(enchantment.getStartLevel() + 1);
            case "isCursed" ->          LuaValue.valueOf(enchantment.isCursed());
            case "isTradeable" ->       LuaValue.valueOf(enchantment.isTradeable());
            case "isDiscoverable" ->    LuaValue.valueOf(enchantment.isDiscoverable());
            case "anvilCost" ->         LuaValue.valueOf(enchantment.getAnvilCost());

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
        Enchantment a = LuaUtil.toEnchantment(arg1);
        Enchantment b = LuaUtil.toEnchantment(arg2);

        if (a == null && b == null)
            return true;
        else if (a == null || b == null)
            return false;

        return a.equals(b);
    }

    @Override
    protected String getUserdataType() {
        return "enchantment";
    }

    /* --- --- Instance Methods --- --- */

    // ToDo: Change to methods

    private static final LuaFunction enchantment_conflictsWith = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var a = LuaUtil.toEnchantment(arg1);
            var b = LuaUtil.toEnchantment(arg2);

            if (a == null || b == null)
                return LuaConstant.FALSE;

            return valueOf(a.conflictsWith(b));
        }
    };

    private static final LuaFunction enchantment_canEnchantItem = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var enchant = LuaUtil.toEnchantment(arg1);

            if (enchant == null)
                return argumentError(1, "Enchantment expected, got " + arg2.getType());

            return valueOf(enchant.canEnchantItem(arg2.checkuserdata(ItemStack.class)));
        }
    };

    private static final LuaFunction enchantment_displayName = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var enchant = LuaUtil.toEnchantment(arg1);

            if (enchant == null)
                return argumentError(1, "Enchantment expected, got " + arg2.getType());

            var level = Math.clamp(arg2.checkint()-1, enchant.getStartLevel(), enchant.getMaxLevel());
            return valueOf(enchant.displayName(level).toString());
        }
    };
}
