package net.malfact.gamecore;

public abstract class GameManager {

    protected final GameCore plugin;

    public GameManager(GameCore plugin) {
        this.plugin = plugin;
    }

    public abstract void save();

    public abstract void load();
}
