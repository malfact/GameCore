package net.malfact.gamecore.util;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.player.QueuedPlayer;
import org.bukkit.entity.Player;

public class Validate {



    public static QueuedPlayer isGamePlayer(Player player) {
        QueuedPlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);

        if (gamePlayer == null)
            throw new RuntimeException("Player {" + player.getName() + "} is not registered as a GamePlayer!");

        return gamePlayer;
    }
}
