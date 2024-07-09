package net.malfact.gamecore;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.ScriptApi;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.event.RegisterGlobalLibraryEvent;
import net.malfact.gamecore.event.RegisterLocalLibraryEvent;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.ScriptedGame;
import org.bukkit.Bukkit;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LuaScriptApi implements LuaApi, ScriptApi {

    private final Globals serverGlobals = JsePlatform.standardGlobals();
    private Map<Class<?>, TypeHandler<?>> typeHandlers;

    private boolean locked = false;
    private LuaTable globalEnv;

    @Override
    public TypeHandler<?> getTypeHandler(Class<?> clazz) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            if (typeHandlers.containsKey(currentClass)) {
                var handler = typeHandlers.get(currentClass);
                typeHandlers.put(clazz, handler);
                return handler;
            }
            currentClass = currentClass.getSuperclass();
        }

        GameCore.logger().debug("Did not find handler for {}", clazz.getSimpleName());

        return null;
    }


    @Override
    public LuaValue getUserdataOf(Object obj) {
        return getUserdataOf(obj, null);
    }

    @Override
    public LuaValue getUserdataOf(Object obj, Game instance) {
        if (obj == null)
            return LuaConstant.NIL;

        if (obj instanceof LuaValue luaValue)
            return luaValue;

        var handler = getTypeHandler(obj.getClass());
        if (handler == null)
            return LuaConstant.NIL;

        if (instance == null)
            return handler.getUserdataOfRaw(obj);
        else
            return handler.getUserdataOfRaw(obj, instance);
    }


    LuaScriptApi(){}

    void buildLibraries() {
        if (locked) return;
        locked = true;

        globalEnv = createGlobalEnv();
    }

    @Override
    public LuaScript getScript(File file) {
        if (!file.exists() || !file.canRead())
            return null;

        return new LuaScript(file);
    }

    @Override
    public void loadScript(LuaScript script, ScriptedGame game) {
        script.load(serverGlobals, createLocalEnv(game));
    }

    private LuaTable createGlobalEnv() {
        LuaTable env = new LuaTable();

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, serverGlobals);

        GameCore.logger().info("[API] :: Registering Lua Libraries");

        List<LuaLib> libs = new ArrayList<>();
        Map<Class<?>, TypeHandler<?>> handlers = new HashMap<>();
        Bukkit.getPluginManager().callEvent(new RegisterGlobalLibraryEvent(libs, handlers));

        for (var lib : libs) {
            lib.load(env);
        }

        this.typeHandlers = handlers;

        GameCore.logger().info("[API] .. Finished Registering {} Lua Libraries", libs.size());

        env.setmetatable(meta);

        return env;
    }

    private LuaTable createLocalEnv(Game instance) {
        LuaTable env = new LuaTable();

        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, globalEnv);

        GameCore.logger().info("[API] :: Registering Lua Libraries for {}", instance.getName());
        List<LuaLib> libs = new ArrayList<>();
        Bukkit.getPluginManager().callEvent(new RegisterLocalLibraryEvent(instance, libs));
        for (var lib : libs) {
            lib.load(env);
        }
        GameCore.logger().info("[API] .. Finished Registering {} Lua Libraries for {}", libs.size(), instance.getName());

        env.setmetatable(meta);

        return env;
    }
}
