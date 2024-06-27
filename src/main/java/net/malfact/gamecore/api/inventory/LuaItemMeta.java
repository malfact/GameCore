package net.malfact.gamecore.api.inventory;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.coercion.CoerceJavaToLua;

import java.util.function.Function;
import java.util.function.Predicate;

public class LuaItemMeta extends LuaUserdata {

    private final ItemMeta itemMeta;

    public LuaItemMeta(ItemMeta itemMeta) {
        super(itemMeta);
        this.itemMeta = itemMeta;
    }

    private static String itemName(ItemMeta meta) {
        return meta.itemName().toString();
    }

    private static String displayName(ItemMeta meta) {
        return meta.displayName().toString();
    }

    @Override
    public LuaValue get(LuaValue key) {
        if (!key.isstring())
            return super.get(key);

        return switch (key.tojstring()) {
            // ItemMeta
            case "unbreakable" -> valueOf(itemMeta.isUnbreakable());
            case "itemName" -> doIf(ItemMeta::hasItemName, LuaItemMeta::itemName);
            case "displayName" -> doIf(ItemMeta::hasDisplayName, LuaItemMeta::displayName);
            case "maxStackSize" -> doIf(ItemMeta::hasMaxStackSize, ItemMeta::getMaxStackSize);
            // Damageable
            case "maxDamage" -> doIf(Damageable::hasMaxDamage, Damageable::getMaxDamage, Damageable.class);
            case "damage" -> doIf(Damageable::hasDamage, Damageable::getDamage, Damageable.class);
            // ArmorStandMeta
            case "invisible" -> doIf(ArmorStandMeta::isInvisible, ArmorStandMeta.class);
            case "noBasePlate" -> doIf(ArmorStandMeta::hasNoBasePlate, ArmorStandMeta.class);
            case "showArms" -> doIf(ArmorStandMeta::shouldShowArms, ArmorStandMeta.class);
            case "small" -> doIf(ArmorStandMeta::isSmall, ArmorStandMeta.class);
            case "marker" -> doIf(ArmorStandMeta::isMarker, ArmorStandMeta.class);
            // ArmorMeta
            // AxolotlBucketMeta
            // BannerMeta
            // BlockDataMeta
            // BlockStateMeta
            // BookMeta
            // BundleMeta
            // CompassMeta
            // CrossbowMeta
            // EnchantmentStorageMeta
            // FireworkEffectMeta
            // FireworkMeta
            // LeatherArmorMeta
            // MapMeta
            // MusicInstrumentMeta
            // OminousBottleMeta
            // PotionMeta
            // Repairable
            // SkullMeta
            // SpawnEggMeta
            // SuspiciousStewMeta
            // TropicalFishBucketMeta
            // WritableBookMeta
            default -> LuaConstant.NIL;
        };
    }

    private <T extends ItemMeta> LuaValue doIf(Function<T, ?> function, Class<T> clazz) {
        if (!clazz.isInstance(itemMeta))
            return LuaConstant.NIL;

        return CoerceJavaToLua.coerce(function.apply(clazz.cast(itemMeta)));
    }

    private LuaValue doIf(Predicate<ItemMeta> predicate, Function<ItemMeta, ?> function) {
        if (predicate.test(itemMeta))
            return CoerceJavaToLua.coerce(function.apply(itemMeta));
        else
            return LuaConstant.NIL;
    }

    private <T extends ItemMeta> LuaValue doIf(Predicate<T> predicate, Function<T, ?> function, Class<T> clazz) {
        if (!clazz.isInstance(itemMeta))
            return LuaConstant.NIL;

        T meta = clazz.cast(itemMeta);

        if (predicate.test(meta))
            return CoerceJavaToLua.coerce(function.apply(meta));
        else
            return LuaConstant.NIL;
    }
}
