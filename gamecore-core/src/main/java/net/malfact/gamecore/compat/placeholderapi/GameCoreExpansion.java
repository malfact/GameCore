package net.malfact.gamecore.compat.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.player.QueuedPlayer;
import net.malfact.gamecore.queue.GameQueue;
import net.malfact.gamecore.team.GameTeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GameCoreExpansion extends PlaceholderExpansion {

    private final GameCore plugin;

    public GameCoreExpansion(GameCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "gamecore";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    private String plc(String str) {
        return "%" + getIdentifier() + "_" + str + "%";
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Arrays.asList(
            plc("player_queue"),
            plc("player_team"),
            plc("score_{<score>}"),

            plc("queue_count_{<queue>}"),
            plc("queue_enabled_{<queue>}"),

            plc("team_count_{team}")
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (identifier.equalsIgnoreCase("player_queue")) {
            QueuedPlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);

            return gamePlayer == null ? "none" : gamePlayer.getQueueName();

        } else if (identifier.equalsIgnoreCase("player_team")) {
            QueuedPlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);

            return gamePlayer == null ? "none" : gamePlayer.getTeamName();

        } else if (identifier.startsWith("queue_")) {

        } else if (identifier.startsWith("team_")) {

        }

        int selectorStart = identifier.indexOf('{');
        int selectorEnd = identifier.indexOf('}');
        String selector = null;
        if (selectorStart != -1 && selectorEnd != -1) {
            selector = identifier.substring(selectorStart+1,selectorEnd);
            identifier = identifier.replace("_{" + selector + "}", "").toLowerCase();
        }

        String[] args = identifier.split("_");

        if (args.length <= 1)
            return null;

        switch(args[0]) {
            case "queue":
                if (selector == null) return null;

                GameQueue queue = GameCore.getQueueManager().getQueue(selector);

                if (queue == null) return null;

                if (args[1].equals("count"))
                    return ""+queue.getPlayerCount();
                else if (args[1].equals("enabled"))
                    return queue.getEnabled() ? "true" : "false";
                break;
            case "team":
                if (selector == null) return null;

                GameTeam team = GameCore.getTeamManager().getTeam(selector);

                if (team == null) return null;

                if (args[1].equalsIgnoreCase("count")) {
                    return "" + team.getPlayerCount();
                }
                break;
        }

        return null;
    }
}
