package net.malfact.gamecore.api.inventory;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LuaItemStack extends LuaUserdata {

    // Lua Library Functions
    public static final LuaFunction CONSTRUCTOR = new Constructor();
    // Lua Member Functions
    private static final LuaFunction ADD_ENCHANT = new AddEnchant();
    private static final LuaFunction REMOVE_ENCHANT = new RemoveEnchant();
    private static final LuaFunction CLEAR_ENCHANTS = new ClearEnchant();
    private static final Clone CLONE = new Clone();

    private ItemStack itemStack;

    public LuaItemStack(ItemStack itemStack) {
        super(itemStack);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    protected String getMaterial() {
        return itemStack.getType().key().asMinimalString();
    }

    protected void setMaterial(String materialType) {
        itemStack = itemStack.withType(checkMaterial(materialType));
    }

    protected int getAmount() {
        return itemStack.getAmount();
    }

    protected void setAmount(int amount) {
        itemStack.setAmount(amount);
    }

    protected void setItemMeta(LuaItemMeta itemMeta) {
        itemStack.setItemMeta((ItemMeta) itemMeta.m_instance);
    }

    protected void addEnchantment(Enchantment enchant, int level) {
        itemStack.addUnsafeEnchantment(enchant, level);
    }

    protected void removeEnchantment(Enchantment enchant) {
        itemStack.removeEnchantment(enchant);
    }

    protected void clearEnchantments() {
        itemStack.removeEnchantments();
    }

    // --- --- --- --- --- --- --- --- --- ---
    //      Lua API
    // --- --- --- --- --- --- --- --- --- ---

    @Override
    public LuaValue len() {
        return valueOf(itemStack.getAmount());
    }

    @Override
    public LuaValue get(LuaValue key) {
        if (!key.isstring())
            return super.get(key);

        return switch (key.tojstring()) {
            case "type" -> valueOf(getMaterial());
            case "amount" -> valueOf(getAmount());
            case "itemMeta" -> itemStack.hasItemMeta() ? new LuaItemMeta(itemStack.getItemMeta()) : LuaConstant.NIL;
            case "isEmpty" -> valueOf(itemStack.isEmpty());
            case "clone" -> LuaItemStack.CLONE;
            case "addEnchant" -> LuaItemStack.ADD_ENCHANT;
            case "removeEnchant" -> LuaItemStack.REMOVE_ENCHANT;
            case "clearEnchants" -> LuaItemStack.CLEAR_ENCHANTS;
            default -> LuaConstant.NIL;
        };
    }

    public void set(LuaValue key, LuaValue value) {
        if (!key.isstring()) {
            super.set(key, value);
            return;
        }

        switch (key.tojstring()) {
            case "type" -> doIf(value::isstring, value::tojstring, this::setMaterial);
            case "amount" -> doIf(value::isint, value::toint, this::setAmount);
            case "itemMeta" -> doIf(() -> value instanceof LuaItemMeta, () -> (LuaItemMeta) value, this::setItemMeta);
        }
    }

    private <T> void doIf(BooleanSupplier supplier, Supplier<T> convert, Consumer<T> consumer) {
        if (supplier.getAsBoolean())
            consumer.accept(convert.get());
    }

    private static Material checkMaterial(String materialType) {
        NamespacedKey key = NamespacedKey.fromString(materialType);
        if (key == null)
            throw new LuaError("bad argument (NamespacedKey expected, got nil)");

        Material material = Registry.MATERIAL.get(key);
        if (material == null)
            throw new LuaError("bad argument (Material expected, got nil from " + materialType + ")");

        return material;
    }

    private static Enchantment checkEnchant(LuaValue value) {
        String enchant = value.checkjstring();
        NamespacedKey key = NamespacedKey.fromString(enchant);

        if (key == null)
            throw new LuaError("bad argument (NamespacedKey expected, got nil)");

        Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);

        Enchantment enchantment = registry.get(key);
        if (enchantment == null)
            throw new LuaError("bad argument (Enchantment expected, got nil from " + enchant + ")");

        return registry.get(key);
    }

    private static LuaItemStack checkItemStack(LuaValue value) {
        if (value instanceof LuaItemStack)
            return (LuaItemStack) value;

        throw new LuaError("bad argument (ItemStack expected, got" + value.getType().typeName + ")");
    }

    private static class AddEnchant extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue self, LuaValue enchant, LuaValue level) {
            checkItemStack(self).addEnchantment(checkEnchant(enchant), level.checkint());
            return LuaConstant.NIL;
        }
    }

    private static class RemoveEnchant extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue self, LuaValue enchant) {
            checkItemStack(self).removeEnchantment(checkEnchant(enchant));
            return LuaConstant.NIL;
        }
    }

    private static class ClearEnchant extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue self) {
            checkItemStack(self).clearEnchantments();
            return LuaConstant.NIL;
        }
    }

    private static class Clone extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue self) {
            if (self instanceof LuaItemStack itemStack)
                return new LuaItemStack(itemStack.itemStack.clone());

            return LuaConstant.NIL;
        }
    }

    private static class Constructor extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue materialValue) {
            if (!materialValue.isstring())
                return LuaConstant.NIL;

            return new LuaItemStack(new ItemStack(checkMaterial(materialValue.tojstring())));
        }
    }
}
