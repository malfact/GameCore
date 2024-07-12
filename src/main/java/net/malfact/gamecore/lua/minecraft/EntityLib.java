package net.malfact.gamecore.lua.minecraft;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.event.EventLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public final class EntityLib extends EventLib {

    private final LuaFunction func_new = LuaUtil.toFunction(this::createEntity);

    public EntityLib(Game instance) {
        super(instance, "entity");
    }

    @Override
    protected LuaValue createLib(LuaValue env) {
        LuaTable lib = new LuaTable();

        lib.set("new",      func_new);
        env.set("Entity",   lib);

        return lib;
    }

    private LuaValue createEntity(LuaValue arg1, LuaValue arg2) {
        EntityType type = LuaUtil.checkEntityType(arg1);

        // Only allow spawnable entities
        if (!type.isSpawnable())
            return LuaConstant.NIL;

        // Allow for NIL, Vector3, or Location
        Location location;
        if (!arg2.isnil())
            location = LuaUtil.checkLocation(arg2);
        else
            location = new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0);

        // Only allow enabled entities
        if (!type.isEnabledByFeature(location.getWorld()))
            return LuaConstant.NIL;

        Entity entity = location.getWorld().createEntity(location, type.getEntityClass());
        instance.registerEntity(entity);

        return LuaApi.userdataOf(entity, instance);
    }
}