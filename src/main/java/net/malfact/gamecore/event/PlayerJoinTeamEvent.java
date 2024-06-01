package net.malfact.gamecore.event;

import net.malfact.gamecore.player.GamePlayer;
import net.malfact.gamecore.team.GameTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinTeamEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final GamePlayer player;
    private final GameTeam team;

    public PlayerJoinTeamEvent(@NotNull GamePlayer player, @NotNull GameTeam team) {
        this.player = player;
        this.team = team;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull GamePlayer getPlayer() {
        return this.player;
    }

    public @NotNull GameTeam getTeam() {
        return this.team;
    }
}
