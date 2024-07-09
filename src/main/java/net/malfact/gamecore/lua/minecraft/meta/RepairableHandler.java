package net.malfact.gamecore.lua.minecraft.meta;

import org.bukkit.inventory.meta.Repairable;
import org.luaj.vm2.LuaValue;

public class RepairableHandler extends ItemMetaHandler<Repairable> {

    public RepairableHandler() {
        super(Repairable.class);
    }

    @Override
    public LuaValue get(Repairable meta, String key) {
        if (key.equals("repairCost"))
            return meta.hasRepairCost() ? LuaValue.valueOf(meta.getRepairCost()) : NIL;

        return null;
    }

    @Override
    public boolean set(Repairable meta, String key, LuaValue value) {
        if (key.equals("repairCost")) {
            meta.setRepairCost(value.checkint());
            return true;
        }

        return false;
    }
}
