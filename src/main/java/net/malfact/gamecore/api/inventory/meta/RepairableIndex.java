package net.malfact.gamecore.api.inventory.meta;

import org.bukkit.inventory.meta.Repairable;
import org.luaj.vm2.LuaValue;

class RepairableIndex extends ItemMetaLib.Index<Repairable> {
    RepairableIndex() {
        super(Repairable.class);
    }

    @Override
    public Object get(Repairable meta, String key) {
        if (key.equals("repairCost"))
            return meta.hasRepairCost() ? meta.getRepairCost() : null;

        return null;
    }

    @Override
    public void set(Repairable meta, String key, LuaValue value) {
        if (key.equals("repairCost"))
            meta.setRepairCost(value.checkint());
    }
}
