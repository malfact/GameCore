package net.malfact.gamecore.script;

import net.kyori.adventure.text.Component;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.DataStoreLib;
import net.malfact.gamecore.api.GameLib;
import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.entity.EntityLib;
import net.malfact.gamecore.api.entity.PlayerLib;
import net.malfact.gamecore.api.inventory.InventoryLib;
import net.malfact.gamecore.api.inventory.ItemStackLib;
import net.malfact.gamecore.api.types.*;
import net.malfact.gamecore.api.userdata.DamageSourceUserdata;
import net.malfact.gamecore.api.userdata.InstancedUserdataProvider;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.api.world.LocationLib;
import net.malfact.gamecore.api.world.Vector3Lib;
import net.malfact.gamecore.api.world.WorldLib;
import net.malfact.gamecore.game.ScriptedGame;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class ScriptManager {

    private static final Map<Class<?>,TypeLib> TYPE_LIBS = new HashMap<>();
    private static final List<Function<Instance,InstancedLib>> GLOBAL_LIBS = new ArrayList<>();
    private static final List<Function<Instance,InstancedLib>> LOCAL_LIBS = new ArrayList<>();
    private static final List<Function<Instance, InstancedUserdataProvider>> PROVIDERS = new ArrayList<>();

    private static final Globals serverGlobals = JsePlatform.standardGlobals();

    public static void registerTypeLib(Class<?> typeClass, TypeLib lib) {
        TYPE_LIBS.put(typeClass, lib);
    }

    public static void registerGlobalLib(Function<Instance, InstancedLib> constructor) {
        GLOBAL_LIBS.add(constructor);
    }

    public static void registerLocalLib(Function<Instance, InstancedLib> constructor) {
        LOCAL_LIBS.add(constructor);
    }

    public static void registerUserdataProvider(Function<Instance, InstancedUserdataProvider> provider) {
        PROVIDERS.add(provider);
    }

    public static TypeLib getTypeLib(Class<?> typeClass) {
        return TYPE_LIBS.get(typeClass);
    }

    private final Map<String, LuaScript> gameScripts;
    private final Map<String, Instance> instances;

    private final LuaValue sandboxChunk;
    private final GameCore plugin;

    private Instance globalInstance = null;

    public ScriptManager(GameCore plugin) {
        this.plugin = plugin;

        createDirectories();
        this.gameScripts = new HashMap<>();
        this.instances = new HashMap<>();

        this.sandboxChunk = loadResourceScript("sandbox");

        registerTypeLib(Attribute.class, new AttributeTypeLib());
        registerTypeLib(Material.class, new MaterialTypeLib());
        registerTypeLib(EntityType.class, new EnchantTypeLib());
        registerTypeLib(PotionType.class, new PotionTypeLib());
        registerTypeLib(PotionEffectType.class, new PotionEffectTypeLib());
        registerTypeLib(Enchantment.class, new EnchantTypeLib());

        registerGlobalLib(Vector3Lib::new);
        registerGlobalLib(LocationLib::new);
        registerGlobalLib(ItemStackLib::new);
        registerGlobalLib(InventoryLib::new);

        registerLocalLib(GameLib::new);
        registerLocalLib(EntityLib::new);
        registerLocalLib(PlayerLib::new);
        registerLocalLib(WorldLib::new);
        registerLocalLib(DataStoreLib::new);

        registerUserdataProvider(DamageSourceUserdata::new);
    }

    // Loads Libraries into Globals
    public void load() {
        GameCore.logDebug("Loading Global Instance...");
        globalInstance = new Instance.GlobalInstance();

        List<UserdataProvider> providers = new ArrayList<>();

        for (var lib : TYPE_LIBS.values()) {
            serverGlobals.load(lib);

            if (lib instanceof UserdataProvider provider)
                providers.add(provider);
        }

        for (var constructor : GLOBAL_LIBS) {
            var lib = constructor.apply(globalInstance);

            serverGlobals.load(lib);

            if (lib instanceof UserdataProvider provider)
                providers.add(provider);
        }

        globalInstance.setProviders(providers);
    }

    public void unload() {
        globalInstance.clean();;
    }

    // Load Game Libraries into Env
    private LuaTable loadEnv(Instance instance) {
        GameCore.logDebug("Loading Local Instance: " + instance.getGame().getName());
        LuaTable env = (LuaTable) sandboxChunk.call();

        List<UserdataProvider> providers = new ArrayList<>();

        for (var constructor : LOCAL_LIBS) {
            var lib = constructor.apply(instance);
            env.load(lib);
            if (lib instanceof UserdataProvider provider)
                providers.add(provider);
        }

        for (var constructor : PROVIDERS) {
            providers.add(constructor.apply(instance));
        }

        instance.setProviders(providers);

        return env;
    }

    public boolean preloadGameScript(String name) {
        if (gameScripts.containsKey(name))
            return true;

        Path gamePath = Paths.get(plugin.getDataFolder() + "/games/" + name + ".lua");
        if (!Files.exists(gamePath))
            return false;

        LuaScript script = new LuaScript(gamePath.toFile());
        gameScripts.put(script.getName(), script);
        return true;
    }

    public boolean loadGameScript(String name) {
        LuaScript script = gameScripts.get(name);

        if (script == null)
            return false;

        Instance instance = new Instance.LocalInstance(globalInstance);
        ScriptedGame game = new ScriptedGame(() -> getInstance(script.getName()));
        instance.setGame(game);
        instance.setScript(script);

        instances.put(script.getName(), instance);

        script.load(serverGlobals, loadEnv(instance));
        GameCore.getGameManager().registerGame(game);
        return true;
    }

    public void unloadGameScript(String name) {
        Instance instance = instances.get(name);
        if (instance != null) {
            instance.clean();
            instance.setGame(null);
            instance.setScript(null);
            instances.remove(name);
        }

        LuaScript script = gameScripts.get(name);
        if (script != null) {
            gameScripts.remove(name);
        }
    }

    public void preloadGameScripts() {
        Path gamesPath = Paths.get(plugin.getDataFolder() + "/games");

        try {
            if (!Files.exists(gamesPath)) {
                Files.createDirectories(gamesPath);
            }
        } catch (IOException e) {
            plugin.logError("Unable to load game scripts: Failed to create game script directory!\n" + e.getMessage());
            return;
        }

        if (!Files.isDirectory(gamesPath)) {
            plugin.logError("Unable to load game scripts: " + gamesPath + " is not a directory!");
            return;
        }

        File[] files = gamesPath.toFile().listFiles((dir, name) -> name.endsWith(".lua"));
        if (files == null)
            return;

        for (File file : files) {
            LuaScript script = new LuaScript(file);
            gameScripts.put(script.getName(), script);
        }
    }

    public void loadGameScripts() {
        plugin.logInfo("Loading Game Scripts.");
        for (var script : gameScripts.values()) {
            Instance instance = new Instance.LocalInstance(globalInstance);
            ScriptedGame game = new ScriptedGame(() -> getInstance(script.getName()));
            instance.setGame(game);
            instance.setScript(script);

            instances.put(script.getName(), instance);

            script.load(serverGlobals, loadEnv(instance));
            GameCore.getGameManager().registerGame(game);
        }
    }

    private Instance getInstance(String name) {
        return instances.get(name);
    }

    public void reloadScript(LuaScript script, ScriptedGame game) {
        if (script == null || game == null) {
            GameCore.logWarning("Unable to reload script!");
            return;
        }
        plugin.logInfo("Reloading script: " + script.getName());

        Instance instance = new Instance.LocalInstance(globalInstance);
        instance.setGame(game);
        instance.setScript(script);

        instances.put(script.getName(), instance);

        script.load(serverGlobals, loadEnv(instance));
    }

    public void reloadScript(LuaScript script) {
        if (script == null)
            return;

        script.load(serverGlobals, (LuaTable) sandboxChunk.call());
    }

    private void createDirectories() {
        Path gamesPath = Paths.get(plugin.getDataFolder() + "/games");

        try {
            if (!Files.exists(gamesPath)) {
                Files.createDirectories(gamesPath);
            }
        } catch (IOException e) {
            plugin.getComponentLogger().error(Component.text(e.getMessage()));
        }
    }

    private LuaValue loadResourceScript(final String name) {
        InputStream stream = plugin.getResource("lua/" + name + ".lua");
        if (stream == null)
            return LuaConstant.NIL;

        InputStreamReader reader = new InputStreamReader(stream);
        LuaValue chunk = serverGlobals.load(reader, name);
        try {
            reader.close();
        } catch (IOException ignored) {}

        return chunk;
    }

}
