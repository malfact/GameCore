package net.malfact.gamecore.api;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.malfact.gamecore.api.inventory.ItemStackLib;
import net.malfact.gamecore.api.inventory.meta.ItemMetaLib;
import net.malfact.gamecore.api.world.LocationLib;
import net.malfact.gamecore.api.world.Vector3;
import net.malfact.gamecore.api.world.Vector3Lib;
import net.malfact.gamecore.script.ScriptManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.*;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

public final class LuaApi {

    /**
     * Takes an object and wraps it in the appropriate LuaValue. <br>
     * <p>
     *     If the object is already a LuaValue, then the LuaValue will be returned. <br>
     *     If the object is {@code null}, then {@code nil} will be returned. <br>
     *     If there is no appropriate LuaValue to wrap the object, then {@code nil} will be returned.
     * </p>
     * <p><i>
     *     <b>Note:</b>
     *     Objects of type {@code Enum<?>} will always be converted into its lowercase name, unless they
     *     are also of type {@code Keyed}, in which their minimal string will be returned.
     * </i></p>
     * @param o the object to wrap
     * @return a new {@code LuaValue}
     */
    @NotNull
    @Deprecated
    public static LuaValue getValueOf(Object o) {
        if (o == null)
            return LuaConstant.NIL;

        // Checks type libs for type
        var typeLib = ScriptManager.getTypeLib(o.getClass());
        if (typeLib != null)
            return typeLib.getUserdataOf(o);

        return switch (o) {
            // Lua Api
            case LuaValue value -> value;
            // Primitives
            case Boolean b ->           LuaValue.valueOf(b);
            case Integer n ->           LuaValue.valueOf(n);
            case Number n ->            LuaValue.valueOf(n.doubleValue());
            // Objects
            case byte[] n ->            LuaValue.valueOf(n);
            case String s ->            LuaValue.valueOf(s);
            // Bukkit Api
            case Location v ->          LocationLib.userdataOf(v);
            case Vector3 v ->           LocationLib.userdataOf(v);
            case Vector v ->            LocationLib.userdataOf(v);
            case ItemMeta v ->          ItemMetaLib.userdataOf(v);
            case ItemStack v ->         ItemStackLib.userdataOf(v);
            case AttributeInstance v -> AttributeLib.userdataOf(v);
            case AttributeModifier v -> AttributeLib.userdataOf(v);

            // -- Keyed & Enum
            case Keyed v ->             LuaValue.valueOf(v.key().asMinimalString());
            case Enum<?> e ->           LuaValue.valueOf(e.toString().toLowerCase(Locale.ROOT));
            default -> LuaConstant.NIL;
        };

    }

    public static LuaValue getValueOf(Enum<?> e) {
        return LuaValue.valueOf(e.toString().toLowerCase(Locale.ROOT));
    }

    public static NamespacedKey toNamespacedKey(String value) {
        return toNamespacedKey(value, true);
    }

    public static NamespacedKey toNamespacedKey(String value, boolean error) {
        NamespacedKey key = NamespacedKey.fromString(value);
        if (key == null && error)
            throw new LuaError("bad argument (NamespacedKey expected, got nil)");

        return key;
    }

    public static <T extends Keyed> T toRegistryEntry(String value, RegistryKey<T> registryKey) {
        Registry<T> registry;
        try {
            registry = RegistryAccess.registryAccess().getRegistry(registryKey);
        } catch (NoSuchElementException ignored) {
            throw new LuaError("bad argument (Registry expected, got nil)");
        }

        NamespacedKey key = toNamespacedKey(value);

        T item = registry.get(key);
        if (item == null) {
            throw new LuaError("bad argument (Keyed expected, got nil from " + value + ")");
        }

        return item;
    }

    /* ---- ---- [ Enchantments    ] ---- ---- */

    @Nullable
    public static Enchantment toEnchantment(@NotNull LuaValue value) {
        if (value.isuserdata(Enchantment.class))
            return value.touserdata(Enchantment.class);
        else if (value.isstring()) {
            var key = toNamespacedKey(value.tojstring(), false);
            if (key == null)
                return null;

            return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);
        }

        return null;
    }

    @NotNull
    public static Enchantment checkEnchantment(@NotNull LuaValue value) {
        Enchantment enchantment = toEnchantment(value);
        if (enchantment == null)
            throw new LuaError("bad argument (Enchantment expected, got " + value.getType() + ")");

        return enchantment;
    }

    /* ---- ---- [ Materials       ] ---- ---- */

    @Nullable
    public static Material toMaterial(LuaValue value) {
        if (value.isuserdata(Material.class))
            return value.touserdata(Material.class);
        else if (value.isstring()) {
            var key = LuaApi.toNamespacedKey(value.tojstring(), false);
            if (key == null)
                return null;

            return Registry.MATERIAL.get(key);
        }

        return null;
    }

    @NotNull
    public static Material checkMaterial(LuaValue value) {
        Material material = toMaterial(value);
        if (material == null)
            throw new LuaError("bad argument (Material expected, got " + value.getType() + ")");

        return material;
    }

    /* ---- ---- [ Potion Type     ] ---- ---- */

    @Nullable
    public static PotionType toPotionType(LuaValue value) {
        if (value.isuserdata(PotionType.class))
            return value.touserdata(PotionType.class);
        else if (value.isstring()) {
            var key = LuaApi.toNamespacedKey(value.tojstring(), false);
            if (key == null)
                return null;

            return Registry.POTION.get(key);
        }

        return null;
    }

    @NotNull
    public static PotionType checkPotionType(LuaValue value) {
        PotionType type = toPotionType(value);
        if (type == null)
            throw new LuaError("bad argument (PotionType expected, got " + value.getType() + ")");

        return type;
    }

    /* ---- ---- [ Potion Effect Type ]-- ---- */

    @Nullable
    public static PotionEffectType toPotionEffectType(LuaValue value) {
        if (value.isuserdata(PotionEffectType.class))
            return value.touserdata(PotionEffectType.class);
        else if (value.isstring()) {
            var key = LuaApi.toNamespacedKey(value.tojstring(), false);
            if (key == null)
                return null;

            return Registry.POTION_EFFECT_TYPE.get(key);
        }

        return null;
    }

    @NotNull
    public static PotionEffectType checkPotionEffectType(LuaValue value) {
        PotionEffectType type = toPotionEffectType(value);
        if (type == null)
            throw new LuaError("bad argument (PotionEffectType expected, got " + value.getType() + ")");

        return type;
    }

    /* ---- ---- [ Entity Type ]---- ---- ---- */

    @Nullable
    public static EntityType toEntityType(LuaValue value) {
        if (value.isuserdata(EntityType.class))
            return value.touserdata(EntityType.class);
        else if (value.isstring()) {
            var key = toNamespacedKey(value.tojstring(), false);
            if (key == null)
                return null;

            return Registry.ENTITY_TYPE.get(key);
        }

        return null;
    }

    @NotNull
    public static EntityType checkEntityType(LuaValue value) {
        EntityType type = toEntityType(value);
        if (type == null)
            throw new LuaError("bad argument (EntityType expected, got " + value.getType() + ")");

        return type;
    }

    /* ---- ---- [ Attribute Type ]- ---- ---- */

    @Nullable
    public static Attribute toAttribute(LuaValue value) {
        if (value.isuserdata(Attribute.class))
            return value.touserdata(Attribute.class);
        else if (value.isstring()) {
            var key = toNamespacedKey(value.tojstring(), false);
            if (key == null)
                return null;

            return Registry.ATTRIBUTE.get(key);
        }

        return null;
    }

    public static Attribute checkAttribute(LuaValue value) {
        Attribute attribute = toAttribute(value);
        if (attribute == null)
            throw new LuaError("bad argument (Attribute expected, got " + value.getType() + ")");

        return attribute;
    }

    public static Attribute toAttribute(String value) {
        NamespacedKey key = toNamespacedKey(value);

        Attribute attribute = Registry.ATTRIBUTE.get(key);

        if (attribute == null)
            throw new LuaError("bad argument (Attribute expected, got nil from " + value + ")");

        return attribute;
    }

    /* ---- ---- [ Arrays & Lists  ] ---- ---- */

    public static LuaTable toTable(Map<?,?> map) {
        LuaTable table = new LuaTable();
        for (var entry : map.entrySet()) {
            var key = getValueOf(entry.getKey());
            if (!key.isnil()) {
                table.set(key, getValueOf(entry.getValue()));
            }
        }

        return table;
    }

    /* ---- ---- [ Location ]-- ---- ---- ---- */

    @Nullable
    public static Location toLocation(LuaValue value) {
        if (value.isnil())
            return null;

        Object o = value.checkuserdata();
        if (o instanceof Location location)
            return location;
        else if (o instanceof Vector3 vec) {
            return Vector3Lib.toLocation(vec, Bukkit.getWorlds().getFirst());
        }

        return null;
    }

    @NotNull
    public static Location checkLocation(LuaValue value) {
        Location location = toLocation(value);

        if (location == null)
            throw new LuaError("bad argument (Location expected, got " + value.getType()  + ")");

        return location;
    }

    /* ---- ---- [ Component ]- ---- ---- ---- */

    @NotNull
    public static Component toComponent(String value) {
        var c = MiniMessage.miniMessage().deserialize(value);
        return c;
    }

    @Nullable
    public static Component toComponent(LuaValue value) {
        if (value.isnil())
            return null;

        return MiniMessage.miniMessage().deserialize(value.tojstring());
    }

    @NotNull
    public static String fromComponent(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    /* ---- ---- ---- ---- ---- ---- ---- ---- */

    public static MusicInstrument toInstrument(String value) {
        NamespacedKey key = toNamespacedKey(value);
        MusicInstrument instrument = Registry.INSTRUMENT.get(key);
        if (instrument == null)
            throw new LuaError("bad argument (Instrument expected, got nil from " + value + ")");

        return instrument;
    }

    public static Particle toParticle(String value) {
        NamespacedKey key = toNamespacedKey(value);

        Particle particle = Registry.PARTICLE_TYPE.get(key);
        if (particle == null)
            throw new LuaError("bad argument (Particle expected, got nil from " + value + ")");

        return particle;
    }

    public static World toWorld(String value) {
        World world = Bukkit.getWorld(value);
        if (world == null)
            throw new LuaError("bad argument (World expected, got nil from " + value + ")");

        return world;
    }

    public static String getKey(Keyed keyed) {
        if (keyed == null)
            return "none";
        else
            return keyed.key().asMinimalString();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static EquipmentSlotGroup getEquipmentSlotGroup(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "any" -> EquipmentSlotGroup.ANY;
            case "mainhand" -> EquipmentSlotGroup.MAINHAND;
            case "offhand" -> EquipmentSlotGroup.OFFHAND;
            case "hand" -> EquipmentSlotGroup.HAND;
            case "feet" -> EquipmentSlotGroup.FEET;
            case "legs" -> EquipmentSlotGroup.LEGS;
            case "chest" -> EquipmentSlotGroup.CHEST;
            case "head" -> EquipmentSlotGroup.HEAD;
            case "armor" -> EquipmentSlotGroup.ARMOR;
            case "body" -> EquipmentSlotGroup.BODY;
            default -> null;
        };
    }

    public static <T extends Enum<T>> T toEnum(String value, Class<T> enumClass) {
        value = value.toUpperCase(Locale.ROOT);
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException ignored) {
            throw new LuaError("bad argument (Enum<" + enumClass.getTypeName() + "> expected, got nil from " + value + ")");
        }
    }

    public static LuaTable readOnly(LuaTable table) {
        return new ReadOnlyLuaTable(table);
    }

    private static class ReadOnlyLuaTable extends LuaTable {
        public ReadOnlyLuaTable(LuaValue table) {
            presize(table.length(), 0);
            for (Varargs n = table.next(LuaConstant.NIL); !n.arg1().isnil(); n = table.next(n.arg1())) {
                LuaValue key = n.arg1();
                LuaValue value = n.arg(2);
                super.rawset(key, value.istable() ? new ReadOnlyLuaTable(value) : value);
            }
        }

        @Override
        public LuaValue setmetatable(LuaValue metatable) {
            return error("table is read-only");
        }

        @Override
        public void set(LuaValue key, LuaValue value) {
            error("table is read-only");
        }

        @Override
        public void rawset(int key, LuaValue value) {
            error("table is read-only");
        }

        @Override
        public void rawset(LuaValue key, LuaValue value) {
            error("table is read-only");
        }

        @Override
        public LuaValue remove(int pos) {
            return error("table is read-only");
        }
    }
}
