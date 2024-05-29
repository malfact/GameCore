package net.malfact.gamecore.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.malfact.gamecore.GameCore;
import org.bukkit.OfflinePlayer;
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
        params = params.toLowerCase();

        switch (params) {
            case "player_queue":
                return "1";
            case "player_team":
                return "2";
            case "queue_enabled":
                return "3";
            case "game_active":
                return "4";
        }
    }
}
