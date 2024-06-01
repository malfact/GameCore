package net.malfact.gamecore;

public abstract class GameManager {

    protected final GameCore plugin;

    public GameManager(GameCore plugin) {
        this.plugin = plugin;
    }

    public void save() {}

    public void load() {}

    /**
     * Runs cleanup on this Game Manager
     */
    public void clean() {}
}
