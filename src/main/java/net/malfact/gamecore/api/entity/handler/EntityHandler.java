package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.script.Instance;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

@SuppressWarnings("unused")
public abstract class EntityHandler {

    protected static final LuaValue NIL = LuaConstant.NIL;

    @NotNull
    protected final <T extends Entity> T checkEntity(Instance instance, Entity entity, Class<T> entityClazz) {
        if (!instance.getGame().hasEntity(entity))
            throw new LuaError(entity.getName() + " is not registered with " + instance.getGame().getName());

        if (entityClazz.isInstance(entity))
            return entityClazz.cast(entity);

        throw new LuaError(entity.getName() + " is not an instance of " + entityClazz.getSimpleName());
    }

    @Nullable
    protected final <T extends Entity> T toEntity(Instance instance, Entity entity, Class<T> entityClass) {
        if (!instance.getGame().hasEntity(entity))
            return null;

        if (entityClass.isInstance(entity))
            return entityClass.cast(entity);

        return null;
    }

    public abstract boolean acceptsEntity(Entity entity);

    public abstract LuaValue onIndex(Instance instance, Entity entity, LuaValue key);

    public abstract boolean onNewIndex(Instance instance, Entity entity, LuaValue key, LuaValue value);
}
