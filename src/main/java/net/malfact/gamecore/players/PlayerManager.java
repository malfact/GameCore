package net.malfact.gamecore.players;

import com.google.gson.reflect.TypeToken;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameManager;
import net.malfact.gamecore.util.Json;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;

public class PlayerManager extends GameManager {

    private static Map<UUID, GamePlayer> players;
    private static final Type dataType = new TypeToken<PlayerData>() {}.getType();

    public PlayerManager(GameCore plugin) {
        super(plugin);

        players = new HashMap<>();
    }

    public GamePlayer getPlayer(@NotNull Player player) {
        if (players == null || players.isEmpty())
            return null;

        return players.get(player.getUniqueId());
    }

    public List<GamePlayer> getPlayers(@NotNull List<Player> players) {
        List<GamePlayer> gamePlayers = new ArrayList<>();
        for (Player player : players) {
            GamePlayer gamePlayer = getPlayer(player);
            if (gamePlayer != null)
                gamePlayers.add(gamePlayer);
        }

        return  gamePlayers;
    }

    @NotNull GamePlayer load(@NotNull Player player) {
        PlayerData data = Json.read(plugin, "data/players/" + player.getUniqueId(), dataType);

        GamePlayer gamePlayer = getPlayer(player);

        if (gamePlayer == null) {
            plugin.logInfo("Creating new Game Player " + player.getName());
            gamePlayer = data == null ? new GamePlayer(player) : new GamePlayer(player, data);
            players.put(player.getUniqueId(), gamePlayer);
        }

        return gamePlayer;
    }

    void save(@NotNull GamePlayer gamePlayer) {
        PlayerData data = gamePlayer.getDataObject();

        if (data == null)
            Json.delete(plugin, "data/players/" + gamePlayer.uuid);
        else
            Json.write(plugin, "data/players/" + gamePlayer.uuid, data);
    }

    void save(@NotNull Player player) {
        if (players.isEmpty())
            return;

        save(players.get(player.getUniqueId()));
    }

    @Override
    public void save() {
        for (GamePlayer gamePlayer : players.values()) {
            save(gamePlayer);
        }

        plugin.logInfo("Saved Players to teams.json");
    }

    /**
     * GamePlayers are loaded when the Player logs in
     */
    @Override
    public void load() {}
}
