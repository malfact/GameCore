package net.malfact.gamecore.players;

import net.malfact.gamecore.GameCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        GamePlayer player = GameCore.getPlayerManager().load(event.getPlayer());

        if (player.hasTeleportLocation()) {
            event.setSpawnLocation(player.getTeleportLocation());
            player.setTeleportLocation(null);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        GameCore.getPlayerManager().save(event.getPlayer());
    }
}
