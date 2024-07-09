package net.malfact.gamecore.lua.minecraft.meta;

import org.bukkit.inventory.meta.Damageable;
import org.luaj.vm2.LuaValue;

public class DamageableHandler extends ItemMetaHandler<Damageable> {

    public DamageableHandler() {
        super(Damageable.class);
    }

    @Override
    public LuaValue get(Damageable meta, String key) {
        return switch (key) {
            case "maxDamage" -> meta.hasMaxDamage() ? LuaValue.valueOf(meta.getMaxDamage()) : NIL;
            case "damage" -> meta.hasDamageValue() ? LuaValue.valueOf(meta.getDamage()) : NIL;
            default -> null;
        };
    }

    @Override
    public boolean set(Damageable meta, String key, LuaValue value) {
        switch (key) {
            case "maxDamage" -> meta.setMaxDamage(value.checkint());
            case "damage" -> meta.setDamage(value.checkint());
            default -> {
                return false;
            }
        }

        return true;
    }
}
