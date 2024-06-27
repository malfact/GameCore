package net.malfact.gamecore;

public abstract class GameCoreManager {

    protected final GameCore plugin;

    public GameCoreManager(GameCore plugin) {
        this.plugin = plugin;
    }

    public void save() {}

    public void load() {}

    /**
     * Runs cleanup on this Game Manager
     */
    public void clean() {}
}
