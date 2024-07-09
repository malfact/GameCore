package net.malfact.gamecore.api;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.lua.Vector3Lib;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.*;

public abstract class LuaUtil {

    LuaUtil(){}

    protected static LuaValue NIL = LuaConstant.NIL;

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

    public static LuaValue valueOf(NamespacedKey key) {
        if (key == null)
            return LuaConstant.NIL;

        return LuaValue.valueOf(key.asMinimalString());
    }

    public static LuaValue valueOf(Keyed keyed) {
        if (keyed == null)
            return LuaConstant.NIL;

        return LuaValue.valueOf(keyed.getKey().asMinimalString());
    }

    public static LuaValue valueOf(Enum<?> e) {
        if (e == null)
            return LuaConstant.NIL;

        return LuaValue.valueOf(e.toString().toLowerCase(Locale.ROOT));
    }

    public static LuaValue valueOf(Component component) {
        if (component == null)
            return LuaConstant.NIL;

        return LuaValue.valueOf(fromComponent(component));
    }

    // -----
    public static void argumentError(int index, String expected, LuaValue received) {
        LuaValue receivedType = LuaApi.typeOf(received);
        throw new LuaError("bad argument #" + index + " (" + expected + " expected, got " + receivedType.tojstring() + ")");
    }

    public static void argumentError(int index, String expected) {
        throw new LuaError("bad argument #" + index + " (" + expected + " expected, got nil)");
    }

    // -----

    public static int toRange(LuaValue value, int min, int max) {
        return Math.clamp(value.checkint(), min, max);
    }

    public static double toRange(LuaValue value, double min, double max) {
        return Math.clamp(value.checkdouble(), min, max);
    }

    public static int toMinRange(LuaValue value, int min) {
        return Math.max(value.checkint(), min);
    }

    // -----

    public static String toString(LuaValue arg) {
        LuaValue tag = arg.metatag(LuaConstant.MetaTag.TOSTRING);
        if (!tag.isnil())
            return tag.call().tojstring();

        if (arg.isuserdata())
            return "userdata";

        return arg.tojstring();
    }

    public static String toString(Varargs args) {
        if (args.narg() == 0)
            return "";
        if (args.narg() == 1)
            return args.arg1().tojstring();

        StringBuilder builder = new StringBuilder(toString(args.arg1()));

        for (int i = 2; i <= args.narg(); i++) {
            builder.append(" ").append(toString(args.arg(i)));
        }
        return builder.toString();
    }

    /* ---- ---- [ Sounds          ] ---- ---- */
    @Nullable
    public static Sound toSound(@NotNull LuaValue value) {
        if (!value.isstring())
            return null;

        var key = toNamespacedKey(value.tojstring(), false);
        if (key == null)
            return null;

        return Registry.SOUNDS.get(key);
    }

    public static Sound checkSound(@NotNull LuaValue value) {
        Sound sound = toSound(value);
        if (sound == null)
            throw new LuaError("bad argument (Sound expected, got " + value.getType() + ")");

        return sound;
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
            var key = LuaUtil.toNamespacedKey(value.tojstring(), false);
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
            var key = LuaUtil.toNamespacedKey(value.tojstring(), false);
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
            var key = LuaUtil.toNamespacedKey(value.tojstring(), false);
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

    /* ---- ---- [ Vector3 ]--- ---- ---- ---- */

    @Nullable
    public static Vector3 toVector3(LuaValue value) {
        if (value.isnil())
            return null;

        Object o = value.checkuserdata();
        if (o instanceof Vector3 vec)
            return vec;
        else if (o instanceof Location loc) {
            return new Vector3(loc.getX(), loc.getY(), loc.getZ());
        }

        return null;
    }

    @NotNull
    public static Vector3 checkVector3(LuaValue value) {
        Vector3 vec = toVector3(value);

        if (vec == null)
            throw new LuaError("bad argument (Vector3 expected, got " + value.getType()  + ")");

        return vec;
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
    public static EquipmentSlotGroup toEquipmentSlotGroup(LuaValue value) {
        if (!value.isstring())
            return null;
        String name = value.tojstring().toLowerCase(Locale.ROOT);

        return switch (name) {
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

    public static <T extends Enum<T>> T toEnum(LuaValue value, Class<T> enumClass) {
        if (!value.isstring())
            return null;

        String name = value.tojstring().toUpperCase(Locale.ROOT);
        try {
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static <T extends Enum<T>> T checkEnum(LuaValue value, Class<T> enumClass) {
        T e = toEnum(value, enumClass);
        if (e == null)
            throw new LuaError("bad argument (Enum<" + enumClass.getTypeName() + "> expected, got nil from " + value + ")");

        return e;
    }

    @Deprecated
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

    // ---------- Indirect Functions ---------- //


    public static LuaFunction toFunction(Function<LuaValue, LuaValue> function) {
        return new IndirectFunction(function);
    }

    public static LuaFunction toFunction(Consumer<LuaValue> function) {
        return new IndirectConsumer(function);
    }

    public static LuaFunction toFunction(Supplier<LuaValue> function) {
        return new IndirectSupplier(function);
    }

    public static LuaFunction toFunction(BiFunction<LuaValue, LuaValue, LuaValue> function) {
        return new IndirectBiFunction(function);
    }

    public static LuaFunction toFunction(BiConsumer<LuaValue, LuaValue> function) {
        return new IndirectBiConsumer(function);
    }

    public static LuaFunction toFunction(TriFunction<LuaValue, LuaValue, LuaValue, LuaValue> function) {
        return new IndirectTriFunction(function);
    }

    public static LuaFunction toFunction(TriConsumer<LuaValue, LuaValue, LuaValue> function) {
        return new IndirectTriConsumer(function);
    }

    public static LuaFunction toVarargFunction(Function<Varargs, Varargs> function) {
        return new IndirectVarargFunction(function);
    }

    public static LuaFunction toVarargFunction(Consumer<Varargs> function) {
        return new IndirectVarargConsumer(function);
    }

    public static LuaFunction toVarargFunction(Supplier<Varargs> function) {
        return new IndirectVarargSupplier(function);
    }

    private static class IndirectFunction extends OneArgFunction {
        private final Function<LuaValue, LuaValue> function;

        private IndirectFunction(Function<LuaValue, LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call(LuaValue arg1) {
            return function.apply(arg1);
        }
    }

    private static class IndirectConsumer extends OneArgFunction {
        private final Consumer<LuaValue> function;

        private IndirectConsumer(Consumer<LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call(LuaValue arg1) {
            function.accept(arg1);
            return LuaConstant.NIL;
        }
    }

    private static class IndirectSupplier extends ZeroArgFunction {
        private final Supplier<LuaValue> function;

        private IndirectSupplier(Supplier<LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call() {
            return function.get();
        }
    }

    private static class IndirectBiFunction extends TwoArgFunction {
        private final BiFunction<LuaValue, LuaValue, LuaValue> function;

        private IndirectBiFunction(BiFunction<LuaValue, LuaValue, LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return function.apply(arg1, arg2);
        }
    }

    private static class IndirectBiConsumer extends TwoArgFunction {
        private final BiConsumer<LuaValue, LuaValue> function;

        private IndirectBiConsumer(BiConsumer<LuaValue, LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            function.accept(arg1, arg2);
            return LuaConstant.NIL;
        }
    }

    private static class IndirectTriFunction extends ThreeArgFunction {
        protected final TriFunction<LuaValue, LuaValue, LuaValue, LuaValue> function;

        private IndirectTriFunction(TriFunction<LuaValue, LuaValue, LuaValue, LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            return function.apply(arg1, arg2, arg3);
        }
    }

    private static class IndirectTriConsumer extends ThreeArgFunction {
        private final TriConsumer<LuaValue, LuaValue, LuaValue> function;

        private IndirectTriConsumer(TriConsumer<LuaValue, LuaValue, LuaValue> function) {
            this.function = function;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            function.accept(arg1, arg2, arg3);
            return LuaConstant.NIL;
        }
    }

    private static class IndirectVarargFunction extends VarArgFunction {
        private final Function<Varargs, Varargs> function;

        private IndirectVarargFunction(Function<Varargs, Varargs> function) {
            this.function = function;
        }

        @Override
        public Varargs invoke(Varargs args) {
            return function.apply(args);
        }
    }

    private static class IndirectVarargConsumer extends VarArgFunction {
        private final Consumer<Varargs> function;

        private IndirectVarargConsumer(Consumer<Varargs> function) {
            this.function = function;
        }

        @Override
        public Varargs invoke(Varargs args) {
            function.accept(args);
            return LuaConstant.NIL;
        }
    }

    private static class IndirectVarargSupplier extends VarArgFunction {
        private final Supplier<Varargs> function;

        private IndirectVarargSupplier(Supplier<Varargs> function) {
            this.function = function;
        }

        @Override
        public Varargs invoke(Varargs args) {
            return function.get();
        }
    }
}
