package net.malfact.gamecore.lua.minecraft.meta;

import net.malfact.gamecore.api.LuaUtil;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class EnchantmentStorageMetaHandler extends ItemMetaHandler<EnchantmentStorageMeta> {

    public EnchantmentStorageMetaHandler() {
        super(EnchantmentStorageMeta.class);
    }

    @Override
    public LuaValue get(EnchantmentStorageMeta meta, String key) {
        return switch (key) {
            case "hasEnchant" -> hasEnchant;
            case "getEnchant" -> getEnchant;
            case "addEnchant" -> addEnchant;
            case "removeEnchant" -> removeEnchant;
            default -> null;
        };
    }

    private static final LuaFunction hasEnchant = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var meta = arg1.checkuserdata(EnchantmentStorageMeta.class);
            if (arg2.isnil())
                return valueOf(meta.hasStoredEnchants());

            var enchant = LuaUtil.checkEnchantment(arg2);
            return valueOf(meta.hasStoredEnchant(enchant));
        }
    };

    private static final LuaFunction getEnchant = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            var meta = arg1.checkuserdata(EnchantmentStorageMeta.class);
            var enchant = LuaUtil.checkEnchantment(arg2);

            return meta.hasStoredEnchant(enchant) ? valueOf( meta.getStoredEnchantLevel(enchant)) : LuaConstant.NIL;
        }
    };

    private static final LuaFunction addEnchant = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            var meta = arg1.checkuserdata(EnchantmentStorageMeta.class);
            var enchant = LuaUtil.checkEnchantment(arg2);
            var level = arg3.optint(1) - 1;
            return valueOf(meta.addStoredEnchant(enchant, level, true));
        }
    };

    private static final LuaFunction removeEnchant = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            var meta = arg1.checkuserdata(EnchantmentStorageMeta.class);
            var enchant = LuaUtil.checkEnchantment(arg2);
            return valueOf(meta.removeStoredEnchant(enchant));
        }
    };
}
