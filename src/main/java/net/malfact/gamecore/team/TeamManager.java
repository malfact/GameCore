package net.malfact.gamecore.team;

import com.google.gson.reflect.TypeToken;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameCoreManager;
import net.malfact.gamecore.player.GamePlayer;
import net.malfact.gamecore.util.Json;
import org.bukkit.Bukkit;

import java.util.*;

public class TeamManager extends GameCoreManager {
    private static final Map<String, GameTeam> TEAMS = new HashMap<>();
    private static final Map<UUID, String> PLAYER_TEAMS = new HashMap<>();

    public TeamManager(GameCore plugin) {
        super(plugin);
    }

    /**
     * Creates a Team referenced by name
     * @param name the name of the Team
     * @return the Team that was created,
     * <i>null</i> if a queue by that name already exists or the name is invalid
     */
    public GameTeam addTeam(String name) {
        if (name == null || name.isEmpty() || TEAMS.containsKey(name))
            return null;

        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name) != null)
            return null;

        GameTeam team = new GameTeam(name, this);
        TEAMS.put(name, team);

        return team;
    }

    /**
     * Deletes a Team by its name
     *
     * @param name the name of the Team
     */
    public void removeTeam(String name) {
        if (name == null || name.isEmpty())
            return;

        GameTeam deleted = TEAMS.remove(name);

        if (deleted != null)
            deleted.clean();

    }

    /**
     * Gets a Team by its name
     * @param name the name to find
     * @return the Team, otherwise <i>null</i>
     */
    public GameTeam getTeam(String name) {
        return TEAMS.get(name);
    }

    /**
     * Gets the Team a player is currently in
     * @param player the player
     * @return the Team, otherwise <i>null</i>
     */
    public GameTeam getTeam(GamePlayer player) {
        String team = PLAYER_TEAMS.get(player.getUniqueId());

        if (team == null)
            return null;

        return TEAMS.get(team);
    }

    /**
     * Gets if the Team with name exists
     * @param name the name of the Team
     * @return <i>true</i> if it exists, <i>false</i> otherwise
     */
    public boolean hasTeam(String name) {
        return TEAMS.containsKey(name);
    }

    /**
     * Gets all currently registered Teams
     * @return a set containing all registered Teams
     */
    public Set<GameTeam> getTeams() {
        return new HashSet<>(TEAMS.values());
    }

    /**
     * Gets the names of all currently registered Teams
     * @return an array of Team names
     */
    public String[] getTeamNames() {
        return TEAMS.keySet().toArray(new String[0]);
    }

    /**
     * Cleans all registered Teams in preparation for shutdown
     */
    public void clean() {
        for (GameTeam team : TEAMS.values()) {
            team.clean();
        }
    }

    /**
     * Saves Teams to 'teams.json'
     */
    @Override
    public void save() {
        List<TeamData> data = new ArrayList<>();

        // Safely cleans up deleted teams
        for (GameTeam team : TEAMS.values()) {
            data.add(team.getDataObject());
        }

        Json.write(plugin, "data/teams", data);
        plugin.logInfo("Saved Teams to teams.json");
    }

    /**
     * Loads Teams from 'teams.json'
     */
    @Override
    public void load() {
        List<TeamData> data = Json.read(plugin, "data/teams", new TypeToken<List<TeamData>>() {}.getType());
        if (data == null|| data.isEmpty())
            return;

        for (TeamData teamData : data) {

            GameTeam gameTeam = getTeam(teamData.name);

            if (gameTeam == null)
                gameTeam = new GameTeam(teamData, this);
            else
                gameTeam.setDataObject(teamData);

            TEAMS.put(teamData.name, gameTeam);
        }

        plugin.logInfo("Loaded Teams from 'teams.json'");
    }

    void setPlayerTeam(UUID uuid, String team) {
        if (team == null || team.isEmpty())
            PLAYER_TEAMS.remove(uuid);
        else
            PLAYER_TEAMS.put(uuid, team);
    }

    String getPlayerTeam(UUID uuid) {
        return PLAYER_TEAMS.get(uuid);
    }
}
