package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameCoreManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager extends GameCoreManager {

    private static final Map<UUID, String> PLAYERS_IN_GAMES = new HashMap<>();

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

        PLAYERS_IN_GAMES.entrySet().removeIf(entry -> entry.getValue().equals(name));
    }

    public Game getGame(String game) {
        return games.get(game);
    }

    public Game getGame(Player player) {
        if (!PLAYERS_IN_GAMES.containsKey(player.getUniqueId()))
            return null;

        return getGame(PLAYERS_IN_GAMES.get(player.getUniqueId()));
    }

    public boolean joinGame(Player player, Game game) {
        if (isPlayerInGame(player))
            return false;

        if (game.getState() != Game.State.RUNNING)
            return false;

        game.joinGame(player);
        PLAYERS_IN_GAMES.put(player.getUniqueId(), game.getName());
        return true;
    }

    public boolean leaveGame(Player player) {
        Game game = getGame(player);
        if (game == null)
            return false;

        game.leaveGame(player);
        PLAYERS_IN_GAMES.remove(player.getUniqueId());
        return true;
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

        PLAYERS_IN_GAMES.clear();
        games.clear();
    }

    public String[] getGameNames() {
        return games.keySet().toArray(new String[0]);
    }

    public boolean isPlayerInGame(Player player) {
        return PLAYERS_IN_GAMES.containsKey(player.getUniqueId());
    }
}
