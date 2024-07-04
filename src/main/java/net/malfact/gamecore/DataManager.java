package net.malfact.gamecore;

import net.malfact.gamecore.game.Game;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class DataManager extends GameCoreManager {

    private static final String PLAYERDATA_SCHEMA =
        "CREATE TABLE IF NOT EXISTS playerdata(" +
            "\"uuid\" TEXT NOT NULL," +
            "\"data_key\" TEXT NOT NULL," +
            "\"data_value\" BLOB," +
        "PRIMARY KEY(\"uuid\",\"data_key\"))";

    private static final String GAMEDATA_SCHEMA =
        "CREATE TABLE IF NOT EXISTS gamedata(" +
            "\"game_key\" TEXT NOT NULL," +
            "\"data_key\" TEXT NOT NULL," +
            "\"data_value\" BLOB," +
        "PRIMARY KEY(\"game_key\",\"data_key\"))";

//    private static final String TEAMS_SCHEMA =
//        "CREATE TABLE IF NOT EXISTS teams(" +
//            "\"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
//            "\"name\" TEXT," +
//            "\"owner\" TEXT" +
//        ")";

//    private static final String PLAYERS_SCHEMA =
//        "CREATE TABLE IF NOT EXISTS players(" +
//            "\"uuid\" NOT NULL UNIQUE," +
//            "\"name\" TEXT NOT NULL," +
//            "\"team\" INTEGER," +
//        "PRIMARY KEY(\"uuid\"))";

    private static final String PLAYER_GET =
        "SELECT * FROM playerdata WHERE uuid = ? AND data_key = ?";

    private static final String PLAYER_SET =
        "INSERT INTO playerdata(uuid, data_key, data_value) VALUES(?, ?, ?) " +
        "ON CONFLICT(uuid, data_key) DO UPDATE SET data_value = ? " +
        "WHERE uuid = ? AND data_key = ?";

    private static final String PLAYER_CLEAR =
        "DELETE FROM playerdata WHERE uuid = ? AND data_key = ?";

    private static final String GAME_GET =
        "SELECT * FROM gamedata WHERE game_key = ? AND data_key = ?";

    private static final String GAME_SET =
        "INSERT INTO gamedata(game_key, data_key, data_value) VALUES(?, ?, ?) " +
            "ON CONFLICT(game_key, data_key) DO UPDATE SET data_value = ? " +
            "WHERE game_key = ? AND data_key = ?";

    private static final String GAME_CLEAR =
        "DELETE FROM gamedata WHERE game_key = ? AND data_key = ?";

    private Connection connection = null;

    public DataManager(GameCore plugin) {
        super(plugin);
    }

    private boolean isClosed() {
        if (connection == null)
            return true;

        try {
            return connection.isClosed();
        } catch (SQLException ignored) {
            return true;
        }
    }

    public Object getPlayerData(Player player, String key) {
        if (isClosed())
            return null;

        try {
            var statement = connection.prepareStatement(PLAYER_GET);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, key);

            var result = statement.executeQuery();
            var output = result.getObject("data_value");
            statement.close();
            return output;
        } catch (SQLException e) {
            plugin.logError(e.getMessage());
        }

        return null;
    }

    public void setPlayerData(Player player, String key, Object value) {
        if (isClosed())
            return;

        try {
            var statement = connection.prepareStatement(PLAYER_SET);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(5, player.getUniqueId().toString());
            statement.setString(2, key);
            statement.setString(6, key);
            statement.setObject(3, value);
            statement.setObject(4, value);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            plugin.logError(e.getMessage());
        }
    }

    public void clearPlayerData(Player player, String key) {
        if (isClosed())
            return;

        try {
            var statement = connection.prepareStatement(PLAYER_CLEAR);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, key);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            plugin.logError(e.getMessage());
        }
    }

    public Object getGameData(Game game, String key) {
        if (isClosed())
            return null;

        try {
            var statement = connection.prepareStatement(GAME_GET);
            statement.setString(1, game.getName());
            statement.setString(2, key);

            var result = statement.executeQuery();
            var output = result.getObject("data_value");
            statement.close();
            return output;
        } catch (SQLException e) {
            plugin.logError(e.getMessage());
        }

        return null;
    }

    public void setGameData(Game game, String key, Object value) {
        if (isClosed())
            return;

        try {
            var statement = connection.prepareStatement(GAME_SET);
            statement.setString(1, game.getName());
            statement.setString(5, game.getName());
            statement.setString(2, key);
            statement.setString(6, key);
            statement.setObject(3, value);
            statement.setObject(4, value);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            plugin.logError(e.getMessage());
        }
    }

    public void clearGameData(Game game, String key) {
        if (isClosed())
            return;

        try {
            var statement = connection.prepareStatement(GAME_CLEAR);
            statement.setString(1, game.getName());
            statement.setString(2, key);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            plugin.logError(e.getMessage());
        }
    }

    @Override
    public void load() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/scores.db");
            var statement = connection.createStatement();
            statement.execute(PLAYERDATA_SCHEMA);
            statement.execute(GAMEDATA_SCHEMA);
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            plugin.logError("Failed to connect to database\n" + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException ignored) {}
    }
}
