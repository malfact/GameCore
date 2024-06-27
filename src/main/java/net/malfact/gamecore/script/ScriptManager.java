package net.malfact.gamecore.script;

import net.kyori.adventure.text.Component;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.attribute.AttributeLib;
import net.malfact.gamecore.api.entity.EntityLib;
import net.malfact.gamecore.api.entity.PlayerLib;
import net.malfact.gamecore.api.inventory.LuaItemStack;
import net.malfact.gamecore.api.world.LocationLib;
import net.malfact.gamecore.api.world.Vector3Lib;
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
import java.util.HashMap;
import java.util.Map;

public final class ScriptManager {

    private static final Globals serverGlobals = JsePlatform.standardGlobals();

    static {
        serverGlobals.load(new Vector3Lib());
        serverGlobals.load(new LocationLib());
        LuaTable itemStack = new LuaTable();
        itemStack.set("new", LuaItemStack.CONSTRUCTOR);
        serverGlobals.set("ItemStack", itemStack);
        serverGlobals.load(new EntityLib());
        serverGlobals.load(new PlayerLib());
        serverGlobals.load(new AttributeLib());
    }

    private final Map<String, LuaValue> luaLibraries;

    private final Map<String, LuaScript> gameScripts;

    private final LuaValue sandboxChunk;

    private final GameCore plugin;

    public ScriptManager(GameCore plugin) {
        this.plugin = plugin;

        createDirectories();
        this.luaLibraries = new HashMap<>();
        this.gameScripts = new HashMap<>();

        this.sandboxChunk = loadResourceScript("sandbox");
    }

    public LuaScript[] getGameScripts() {
        return gameScripts.values().toArray(new LuaScript[0]);
    }

    public LuaScript getGameScript(String name) {
        return gameScripts.get(name);
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
            script.load(serverGlobals, (LuaTable) sandboxChunk.call());
        }
    }

    public void reloadGameScript(String name) {
        LuaScript script = gameScripts.get(name);
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
