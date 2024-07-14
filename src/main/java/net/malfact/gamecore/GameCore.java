package net.malfact.gamecore;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.ScriptApi;
import net.malfact.gamecore.command.GameCoreCommands;
import net.malfact.gamecore.compat.placeholderapi.GameCoreExpansion;
import net.malfact.gamecore.compat.worldguard.GameRegionHandler;
import net.malfact.gamecore.compat.worldguard.WorldGuardFlags;
import net.malfact.gamecore.config.ConfigUpdater;
import net.malfact.gamecore.game.GameManager;
import net.malfact.gamecore.game.ScriptedGame;
import net.malfact.gamecore.player.PlayerManager;
import net.malfact.gamecore.queue.QueueManager;
import net.malfact.gamecore.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class GameCore extends JavaPlugin {

    public static final String queuePrefix = "gamecore.queue.";
    public static boolean tagPlayers = true;

    private static GameCore instance;

    private ComponentLogger logger;
    private Messages messages;

    private QueueManager queueManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private GameManager gameManager;
    private DataManager dataManager;
    private EntityManager entityManager;

    private LuaScriptApi luaApi;

    @Override
    public void onLoad() {
        instance = this;
        this.logger = getComponentLogger();

        // --- Update and Load Config.yml ---
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        reloadConfig();

        FileConfiguration config = getConfig();
        tagPlayers = config.getBoolean("queues.tag-players");

        logger.info("Loaded 'config.yml'");

        // --- *------------------------* ---

        // --- Update and Load Messages.yml ---
        messages = new Messages(this);
        messages.saveDefaultMessages();
        File messagesFile = new File(getDataFolder(), "messages.yml");
        try {
            ConfigUpdater.update(this, "messages.yml", messagesFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        messages.reloadMessages();
        logger.info("Loaded 'messages.yml'");
        // --- *------------------------* ---

        GameCoreCommands.register(this);

        if (config.getBoolean("compatability.worldguard") && Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            logger.info("WorldGuard is enabled, registering compatability.");
            WorldGuardFlags.registerFlags();
        }
    }

    @Override
    public void onEnable() {

        // --- Load Game Managers ---
        queueManager = new QueueManager(this);
        teamManager = new TeamManager(this);
        playerManager = new PlayerManager(this);
        gameManager = new GameManager(this);
        dataManager = new DataManager(this);
        entityManager = new EntityManager();

        queueManager.load();
        teamManager.load();
        dataManager.load();
        // --- *------------------------* ---
        // Load Scripts after all plugins enabled
        getServer().getScheduler().scheduleSyncDelayedTask(this, this::postEnable);

        getServer().getPluginManager().registerEvents(new CoreApiPlugin(), this);
        getServer().getPluginManager().registerEvents(gameManager, this);
        getServer().getPluginManager().registerEvents(playerManager, this);
        getServer().getPluginManager().registerEvents(entityManager, this);
        logger.info("Registered Event Handlers");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            logger.info("PlaceholderAPI is enabled, registering Expansion.");
            new GameCoreExpansion(this).register();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            var mng = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getSessionManager();
            mng.registerHandler(GameRegionHandler.FACTORY, com.sk89q.worldguard.session.handler.ExitFlag.FACTORY);
        }
    }

    @Override
    public void onDisable() {
        queueManager.save();
        queueManager.clean();

        teamManager.save();
        teamManager.clean();

        gameManager.stopGames();

        dataManager.save();
    }

    private void postEnable() {
        luaApi = new LuaScriptApi();
        luaApi.buildLibraries();

        if (getConfig().getBoolean("lua.auto-load-scripts")) {
            Path gamesPath = Paths.get(getDataFolder() + "/games");

            try {
                if (!Files.exists(gamesPath)) {
                    Files.createDirectories(gamesPath);
                }
            } catch (IOException e) {
                logger.error("Unable to load game scripts: Failed to create game script directory!\n\t{}", e.getMessage());
                return;
            }

            if (!Files.isDirectory(gamesPath)) {
                logger.error("Unable to load game scripts: {} is not a directory!", gamesPath);
                return;
            }

            File[] files = gamesPath.toFile().listFiles((dir, name) -> name.endsWith(".lua"));
            if (files == null)
                return;

            for (File file : files) {
                if (!file.canRead())
                    continue;

                var script = luaApi.getScript(file);
                ScriptedGame game = new ScriptedGame(script);
                gameManager.registerGame(game);
                luaApi.loadScript(script, game);
            }
        }
    }

    public static void ReloadConfig() {
        if (instance == null)
            return;

        instance.logger.info("Reloading 'config.yml'");
        instance.reloadConfig();
        FileConfiguration config = instance.getConfig();
        tagPlayers = config.getBoolean("tag-players");

        instance.logger.info("Reloaded 'config.yml'");

        instance.logger.info("Reloading 'messages.yml");
        instance.messages.reloadMessages();
        instance.logger.info("Reloaded 'messages.yml'");
    }

    public static GameCore instance() {
        return instance;
    }

    public static Messages getMessages() {
        return instance.messages;
    }

    public static ComponentLogger logger() {
        return instance.logger;
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

    public static GameManager gameManager() {
        return instance.gameManager;
    }

    public static ScriptApi scriptApi() {
        return instance.luaApi;
    }

    public static LuaApi luaApi() {
        return instance.luaApi;
    }

    public static DataManager getDataManager() {
        return instance.dataManager;
    }

    public static EntityManager entityManager() {
        return instance.entityManager;
    }
}
