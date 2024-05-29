package net.malfact.gamecore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.malfact.gamecore.players.PlayerListener;
import net.malfact.gamecore.players.PlayerManager;
import net.malfact.gamecore.queues.QueueManager;
import net.malfact.gamecore.teams.TeamManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class GameCore extends JavaPlugin {

    public static String queuePrefix = "gamecore.queue.";
    public static boolean tagPlayers = true;

    private static GameCore instance;

    private ComponentLogger logger;

    private QueueManager queueManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;

    public static void ReloadConfig() {
        if (instance == null)
            return;

        instance.logInfo("Reloading \"config.yml\"");
        instance.reloadConfig();
        FileConfiguration config = instance.getConfig();
        tagPlayers = config.getBoolean("tag-players");
    }

    @Override
    public void onEnable() {
        instance = this;
        this.logger = getComponentLogger();

        queueManager = new QueueManager(this);
        teamManager = new TeamManager(this);
        playerManager = new PlayerManager(this);

        saveDefaultConfig();
        FileConfiguration config = getConfig();
        tagPlayers = config.getBoolean("queues.tag-players");
        logInfo("Loaded 'config.yml'");

        queueManager.load();
        teamManager.load();
        playerManager.load();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        logInfo("Registered Event Handlers");
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
