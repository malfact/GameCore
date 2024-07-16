package net.malfact.gamecore.compat.worldguard;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.game.Game;
import org.bukkit.Bukkit;

public class GameRegion {

    public static GameRegion get(LocalPlayer player, ApplicableRegionSet set) {
        String gameName = set.queryValue(player, WorldGuardFlags.GAME_REGION);
        Game game = GameCore.gameManager().getGame(gameName);
        if (game == null || !game.isActive())
            return null;

        return new GameRegion(game, player, set);
    }

    public final Game game;
    public final boolean joinOnEnter;
    public final boolean leaveOnExit;
    public final boolean allowGameOnly;
    public final String trigger;

    private GameRegion(Game game, LocalPlayer player, ApplicableRegionSet set) {
        this.game = game;
        this.joinOnEnter = getBoolValue(set, player, WorldGuardFlags.JOIN_GAME);
        this.leaveOnExit = getBoolValue(set, player, WorldGuardFlags.LEAVE_GAME);
        this.allowGameOnly = getBoolValue(set, player, WorldGuardFlags.ALLOW_GAME_ONLY);
        this.trigger = set.queryValue(player, WorldGuardFlags.GAME_TRIGGER);
    }

    public void onEnter(LocalPlayer player) {
        if (joinOnEnter && !isPlayerInGame(player))
            GameCore.gameManager().joinGame(Bukkit.getPlayer(player.getUniqueId()), this.game);
    }

    public void onTrigger(LocalPlayer player) {
        if (trigger != null && isPlayerInGame(player))
            game.sendTrigger(GameCore.gameManager().getPlayerProxy(player.getUniqueId()), trigger);
    }

    public boolean checkEntry(LocalPlayer player) {
        return !allowGameOnly || isPlayerInGame(player);
    }

    public void onExit(LocalPlayer player) {
        if (leaveOnExit && isPlayerInGame(player))
            GameCore.gameManager().leaveGame(Bukkit.getPlayer(player.getUniqueId()));
    }

    private boolean isPlayerInGame(LocalPlayer player) {
        return GameCore.gameManager().isPlayerInGame(Bukkit.getPlayer(player.getUniqueId()), game);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Game other)
            return this.game.equals(other);
        else if (obj instanceof GameRegion region)
            return this.game.equals(region.game);

        return false;
    }

    private boolean getBoolValue(ApplicableRegionSet set, LocalPlayer player, Flag<Boolean> flag) {
        var value = set.queryValue(player, flag);
        return value != null ? value : false;
    }
}
