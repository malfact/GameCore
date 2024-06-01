package net.malfact.gamecore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.malfact.gamecore.command.GameCoreCommands;
import net.malfact.gamecore.placeholder.GameCoreExpansion;
import net.malfact.gamecore.player.PlayerManager;
import net.malfact.gamecore.queue.QueueManager;
import net.malfact.gamecore.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class GameCore extends JavaPlugin {

    public static final String queuePrefix = "gamecore.queue.";
    public static boolean tagPlayers = true;

    private static GameCore instance;

    private ComponentLogger logger;
    private Messages messages;

    private QueueManager queueManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;

    @Override
    public void onLoad() {
        GameCoreCommands.register(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.logger = getComponentLogger();

        saveDefaultConfig();
        FileConfiguration config = getConfig();
        tagPlayers = config.getBoolean("queues.tag-players");
        logInfo("Loaded 'config.yml'");

        messages = new Messages(this);
        messages.saveDefaultMessages();
        messages.reloadMessages();
        logInfo("Loaded 'messages.yml'");

        queueManager = new QueueManager(this);
        teamManager = new TeamManager(this);
        playerManager = new PlayerManager(this);

        queueManager.load();
        teamManager.load();

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
    }

    public void logInfo(String message) {
        logger.info(Component.text(message));
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
}
