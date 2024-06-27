package net.malfact.gamecore.score;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameCoreManager;
import net.malfact.gamecore.player.GamePlayer;

import java.util.HashSet;
import java.util.Set;

public class ScoreManager extends GameCoreManager {

    private final Set<String> score;

    public ScoreManager(GameCore plugin) {
        super(plugin);

        this.score = new HashSet<>();
    }

    public void addScore(String name) {
        score.add(name);
    }

    public void removeScore(String name) {
        score.remove(name);
    }

    public void addScore(GamePlayer player, String name, int amount) {

    }

    public void removeScore(GamePlayer player, String name, int amount) {

    }

    public void setScore(GamePlayer player, String name, int amount) {

    }
}
