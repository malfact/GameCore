package net.malfact.gamecore.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.players.GamePlayer;
import net.malfact.gamecore.queues.GameQueue;
import net.malfact.gamecore.teams.GameTeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.substring(0,6).equalsIgnoreCase("player")) {
            params = params.substring(7);

            GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);
            if (gamePlayer == null)
                return "none";

            if (params.equalsIgnoreCase("queue"))
                return gamePlayer.getQueue();
            else if (params.equalsIgnoreCase("team")) {
                GameTeam team = gamePlayer.getTeam();
                return team == null ? "none" : team.name;
            }

            return "";
        }

        int selectorStart = params.indexOf('{');
        int selectorEnd = params.indexOf('}');
        if (selectorStart == -1 || selectorEnd == -1)
            return null;

        String selector = params.substring(selectorStart+1,selectorEnd);
        params = params.replace("_{" + selector + "}", "");

        String[] args = params.split("_");

        if (args.length < 2) return "";

        if (args[0].equalsIgnoreCase("queue")) {
            GameQueue queue = GameCore.getQueueManager().getQueue(selector);

            if (queue == null)
                return null;

            switch (args[1].toLowerCase()) {
                case "count":
                    return ""+queue.getPlayerCount();
                case "enabled":
                    return queue.getEnabled() ? "true" : "false";
            }
        } else if (args[1].equalsIgnoreCase("team")) {
            GameTeam team = GameCore.getTeamManager().getTeam(selector);

            if (team == null)
                return null;

            if (args[1].equalsIgnoreCase("count")) {
                return "" + team.getPlayerCount();
            }
        }

        return null;
    }
}
