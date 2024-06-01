package net.malfact.gamecore.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.player.GamePlayer;
import net.malfact.gamecore.queue.GameQueue;
import net.malfact.gamecore.team.GameTeam;
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

        int selectorStart = params.indexOf('{');
        int selectorEnd = params.indexOf('}');
        String selector = null;
        if (selectorStart != -1 && selectorEnd != -1) {
            selector = params.substring(selectorStart+1,selectorEnd);
            params = params.replace("_{" + selector + "}", "").toLowerCase();
        }

        String[] args = params.split("_");

        if (args.length <= 1)
            return null;

        switch(args[0]) {
            case "player":
                GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);
                if (gamePlayer == null)
                    return "none";

                if (args[1].equals("queue"))
                    return gamePlayer.getQueueName();
                else if (args[1].equals("team"))
                    return gamePlayer.getTeamName();
                break;
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
