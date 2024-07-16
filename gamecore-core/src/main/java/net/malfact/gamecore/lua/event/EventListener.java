package net.malfact.gamecore.lua.event;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.event.GameEvent;
import net.malfact.gamecore.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class EventListener implements Listener, EventExecutor {

    protected final Game instance;
    private final UUID uuid;

    public EventListener(Game instance) {
        this.instance = instance;
        this.uuid = UUID.randomUUID();
    }

    public abstract void onEvent(Event event);

    @Override
    public final void execute(@NotNull Listener listener, @NotNull Event event) {

        var manager = GameCore.gameManager();

        boolean accepted = switch (event) {
            case GameEvent e ->                 e.getGame().equals(instance);
            case PlayerEvent e ->               manager.isPlayerInGame(e.getPlayer(), instance);
            case EntityDamageByEntityEvent e -> instance.hasEntity(e.getEntity()) || instance.hasEntity(e.getDamager());
            case EntityEvent e ->               instance.hasEntity(e.getEntity());
            case VehicleEvent e ->              instance.hasEntity(e.getVehicle());
            default -> true;
        };

        if (accepted)
            onEvent(event);
    }

}
