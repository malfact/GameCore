package net.malfact.gamecore.players;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.event.PlayerJoinTeamEvent;
import net.malfact.gamecore.event.PlayerLeaveTeamEvent;
import net.malfact.gamecore.teams.GameTeam;
import net.malfact.gamecore.util.Validate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        GamePlayer player = GameCore.getPlayerManager().load(event.getPlayer());

        // Runs cached teleports
        if (player.hasTeleportLocation()) {
            event.setSpawnLocation(player.getTeleportLocation());
            player.setTeleportLocation(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        GamePlayer player = Validate.isGamePlayer(event.getPlayer());

        // Runs cached teleports
        if (player.hasTeleportLocation()) {
            event.setRespawnLocation(player.getTeleportLocation());
            player.setTeleportLocation(null);
        }

        GameTeam team = player.getTeam();

        // Respawn players at Team's spawn if player is in team and the spawn exists
        if (team != null && team.getSpawn() != null)
            event.setRespawnLocation(team.getSpawn());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        GameCore.getPlayerManager().save(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        GamePlayer player = Validate.isGamePlayer(event.getPlayer());
        GameTeam team = player.getTeam();

        // Remove player from their Team if flag is set
        if (team != null && team.getLeaveOnDeath()) {
            team.removePlayer(player);
        }
    }

    @EventHandler
    public void onPlayerJoinTeam(PlayerJoinTeamEvent event) {
        GamePlayer player = event.getPlayer();
        GameTeam team = event.getTeam();

        if (team.getSpawn() != null)
            player.teleport(team.getSpawn(), true);
    }

    @EventHandler
    public void onPlayerLeaveTeam(PlayerLeaveTeamEvent event) {
        GamePlayer player = event.getPlayer();
        GameTeam team = event.getTeam();

        if (team.getExit() != null)
            player.teleport(team.getExit(), true);
    }
}
