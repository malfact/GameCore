package net.malfact.gamecore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.malfact.gamecore.command.GameCoreCommands;
import net.malfact.gamecore.config.ConfigUpdater;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.GameManager;
import net.malfact.gamecore.game.LuaGameBuilder;
import net.malfact.gamecore.placeholder.GameCoreExpansion;
import net.malfact.gamecore.player.PlayerManager;
import net.malfact.gamecore.queue.QueueManager;
import net.malfact.gamecore.script.LuaScript;
import net.malfact.gamecore.script.ScriptManager;
import net.malfact.gamecore.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.lib.jse.coercion.CoerceJavaToLua;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


public final class GameCore extends JavaPlugin {

    public static final String queuePrefix = "gamecore.queue.";
    private static final Logger log = LoggerFactory.getLogger(GameCore.class);
    public static boolean tagPlayers = true;

    private static GameCore instance;

    private ComponentLogger logger;
    private Messages messages;

    private QueueManager queueManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private GameManager gameManager;
    private ScriptManager luaManager;

    @Override
    public void onLoad() {
        GameCoreCommands.register(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.logger = getComponentLogger();

        // --- Update and Load Config.yml ---
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile);
        } catch (IOException e) {
            logError(e.getMessage());
        }
        reloadConfig();

        FileConfiguration config = getConfig();
        tagPlayers = config.getBoolean("queues.tag-players");
        logInfo("Loaded 'config.yml'");
        // --- *------------------------* ---

        // --- Update and Load Messages.yml ---
        messages = new Messages(this);
        messages.saveDefaultMessages();
        File messagesFile = new File(getDataFolder(), "messages.yml");
        try {
            ConfigUpdater.update(this, "messages.yml", messagesFile);
        } catch (IOException e) {
            logError(e.getMessage());
        }
        messages.reloadMessages();
        logInfo("Loaded 'messages.yml'");
        // --- *------------------------* ---

        // --- Load Game Managers ---
        queueManager = new QueueManager(this);
        teamManager = new TeamManager(this);
        luaManager = new ScriptManager(this);
        playerManager = new PlayerManager(this);
        gameManager = new GameManager(this);

        queueManager.load();
        teamManager.load();
        // --- *------------------------* ---
        // Load Scripts after all plugins enabled
        getServer().getScheduler().scheduleSyncDelayedTask(this, this::postEnable);

        getServer().getPluginManager().registerEvents(new TestListener(), this);

        getServer().getPluginManager().registerEvents(playerManager, this);
        logInfo("Registered Event Handlers");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            logInfo("PlaceholderAPI is enabled, registering Expansion.");
            new GameCoreExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        queueManager.save();
        queueManager.clean();

        teamManager.save();
        teamManager.clean();

        gameManager.stopGames();
    }

    private void postEnable() {
        luaManager.preloadGameScripts();

        if (getConfig().getBoolean("lua.auto-load-scripts")) {
            luaManager.loadGameScripts();

            LuaScript[] scripts = luaManager.getGameScripts();
            for (var script : scripts) {
                LuaGameBuilder builder = new LuaGameBuilder(script);
                script.getEnvironment().set("GameBuilder", CoerceJavaToLua.coerce(builder));
                script.run();
                script.getEnvironment().set("GameBuilder", LuaConstant.NIL);
                Game game = builder.build();
                gameManager.registerGame(game);
                game.start();
            }
        }
    }

    public void logInfo(String message) {
        logger.info(Component.text(message));
    }

    public void logError(String message) {
        logger.error(Component.text(message));
    }

    public static void ReloadConfig() {
        if (instance == null)
            return;

        instance.logInfo("Reloading 'config.yml'");
        instance.reloadConfig();
        FileConfiguration config = instance.getConfig();
        tagPlayers = config.getBoolean("tag-players");
        instance.logInfo("Reloaded 'config.yml'");

        instance.logInfo("Reloading 'messages.yml");
        instance.messages.reloadMessages();
        instance.logInfo("Reloaded 'messages.yml'");
    }

    public static GameCore getInstance() {
        return instance;
    }

    public static Messages getMessages() {
        return instance.messages;
    }

    public static QueueManager getQueueManager() {
        return instance.queueManager;
    }

    public static TeamManager getTeamManager() {
        return instance.teamManager;
    }

    public static PlayerManager getPlayerManager() {
        return instance.playerManager;
    }

    public static GameManager getGameManager() {
        return instance.gameManager;
    }

    public static ScriptManager getScriptManager() {
        return instance.luaManager;
    }
}
