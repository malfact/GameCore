package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.Damageable;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;

public class DamageableIndex extends ItemMetaLib.Index<Damageable> {
    DamageableIndex() {
        super(Damageable.class);
    }

    @Override
    public Object get(Damageable meta, String key) {
        return switch (key) {
            case "maxDamage" -> meta.hasMaxDamage() ? meta.getMaxDamage() : LuaConstant.NIL;
            case "damage" -> meta.hasDamageValue() ? meta.getDamage() : LuaConstant.NIL;
            default -> null;
        };
    }

    @Override
    public void set(Damageable meta, String key, LuaValue value) {
        switch (key) {
            case "maxDamage" -> meta.setMaxDamage(value.checkint());
            case "damage" -> meta.setDamage(value.checkint());
        }
    }
}
