package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameCoreManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameManager extends GameCoreManager {

    private final Map<String, Game> games;

    public GameManager(GameCore plugin) {
        super(plugin);
        games = new HashMap<>();
    }

    public void registerGame(Game game) {
        if (games.containsKey(game.getName()))
            throw new IllegalArgumentException("Game " + game.getName() + " already registered");

        GameCore.logDebug("Registered Game: " + game.getName());
        games.put(game.getName(), game);
    }

    public void unregisterGame(String name) {
        var game = games.remove(name);
        if (game != null && game.isRunning()) {
            game.forceStop();
        }
    }

    public Game getGame(String game) {
        return games.get(game);
    }

    public Game getGame(Player player) {
        for (var game : games.values()) {
            if (game.hasPlayer(player))
                return game;
        }

        return null;
    }

    /**
     * Stop all currently running games.<br>
     * Used for shutdown sequence.
     */
    public void stopGames() {
        if (games.isEmpty())
            return;

        for (Game game : games.values()) {
            game.stop();
            game.forceStop();
        }
    }

    public String[] getGameNames() {
        return games.keySet().toArray(new String[0]);
    }
}
