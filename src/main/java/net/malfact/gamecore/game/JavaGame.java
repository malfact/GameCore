package net.malfact.gamecore.game;

import net.malfact.gamecore.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* ToDo: Game
 *  - Games can have a Queue
 *  - Settings:
 *    - max_player_count (int 0..; default=0)
 *    - min_player_count (int 0..; default=0)
 *    - offline_timeout (int 0..; default=0)
 *    - max_game_length (int 0..; default=0)
 *    - keep_parties_together (boolean; default=false)
 *    - max_party_size (int 0..5; default=5)
 */

public class JavaGame {

    public final String name;
    public String displayName;

    private final List<GameTeam> teams;
    private GameTeam defaultTeam;

    private int timer;
    private int maxTime;

//    private final ArrayList<>

    JavaGame(@NotNull String name) {
        Objects.requireNonNull(name);

        this.name = name;
        this.teams = new ArrayList<>();
        this.timer = 0;
    }

    public void addTeam(@NotNull GameTeam team) {
        Objects.requireNonNull(team);

        this.teams.add(team);
    }

    public void removeTeam(@NotNull GameTeam team) {
        Objects.requireNonNull(team);

        this.teams.remove(team);
    }

    public @Nullable GameTeam getDefaultTeam() {
        return this.defaultTeam;
    }

    public void setDefaultTeam(@Nullable GameTeam team) {
        defaultTeam = team;

        if (team != null && !teams.contains(team))
            teams.add(defaultTeam);
    }

    public @NotNull String getDisplayName() {
        return displayName == null ? name : displayName;
    }

    public void setDisplayName(@Nullable String name) {
        this.displayName = name;
    }

    public void start() {
        this.timer = 0;
    }

    private void test() {
        Block block = Bukkit.getWorlds().getFirst().getBlockAt(0,0,0);
    }

    private static class TimedTrigger {
        private Location location;
    }

    private enum GameTriggerType {
        ABSOLUTE,
        RELATIVE,
        EVENT
    }
}
