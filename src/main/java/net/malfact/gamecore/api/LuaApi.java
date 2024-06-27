package net.malfact.gamecore.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.malfact.gamecore.api.attribute.AttributeLib;
import net.malfact.gamecore.api.entity.EntityLib;
import net.malfact.gamecore.api.inventory.LuaInventory;
import net.malfact.gamecore.api.inventory.LuaItemMeta;
import net.malfact.gamecore.api.inventory.LuaItemStack;
import net.malfact.gamecore.api.world.LocationLib;
import net.malfact.gamecore.api.world.Vector3;
import net.malfact.gamecore.api.world.Vector3Lib;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.*;

import java.util.Locale;

public final class LuaApi {

    public static LuaValue getValueOf(Object o) {
        return switch (o) {
            // Lua Api
            case LuaValue value -> value;
            // Primitives
            case Boolean b -> LuaValue.valueOf(b);
            case Integer n -> LuaValue.valueOf(n);
            case Number n -> LuaValue.valueOf(n.doubleValue());
            // Objects
            case byte[] n -> LuaValue.valueOf(n);
            case String s -> LuaValue.valueOf(s);
            case Enum<?> e -> LuaValue.valueOf(e.toString().toLowerCase(Locale.ROOT));
            // Bukkit Api
            case Location loc -> LocationLib.getValueOf(loc);
            case Vector3 vec -> Vector3Lib.getValueOf(vec);
            case Inventory inv -> LuaInventory.of(inv); // Todo: Update API
            case Entity entity -> EntityLib.getValueOf(entity);
            case ItemStack itemStack -> new LuaItemStack(itemStack); // ToDo: Update Api
            case ItemMeta meta -> new LuaItemMeta(meta); // ToDo: Update Api
            case AttributeInstance att -> AttributeLib.getValueOf(att);
            case AttributeModifier mod -> AttributeLib.getValueOf(mod);
//            case Event event -> EventLib.getValueOf(event); // ToDo
            default -> LuaConstant.NIL;
        };

    }

    public static NamespacedKey toNamespacedKey(String value) {
        NamespacedKey key = NamespacedKey.fromString(value);
        if (key == null)
            throw new LuaError("bad argument (NamespacedKey expected, got nil)");

        return key;
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

    public static PotionEffectType toPotionEffectType(String value) {
        NamespacedKey key = toNamespacedKey(value);

        PotionEffectType effectType = Registry.POTION_EFFECT_TYPE.get(key);
        if (effectType == null)
            throw new LuaError("bad argument (PotionEffectType expected, got nil from " + value + ")");

        return effectType;
    }

    public static Attribute toAttribute(String value) {
        NamespacedKey key = toNamespacedKey(value);

        Attribute attribute = Registry.ATTRIBUTE.get(key);

        if (attribute == null)
            throw new LuaError("bad argument (Attribute expected, got nil from " + value + ")");

        return attribute;
    }

    public static EntityType toEntityType(String value) {
        NamespacedKey key = toNamespacedKey(value);

        EntityType entityType =  Registry.ENTITY_TYPE.get(key);
        if (entityType == null)
            throw new LuaError("bad argument (EntityType expected, got nil from " + value + ")");

        return entityType;
    }

    public static Component toComponent(String value) {
        return MiniMessage.miniMessage().deserialize(value);
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
