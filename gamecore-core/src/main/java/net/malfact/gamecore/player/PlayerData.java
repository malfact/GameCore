package net.malfact.gamecore.player;

import org.bukkit.Location;

import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerData {

    public final UUID uuid;
    public final String name;
    public boolean dead;
    public Location teleportLocation;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.dead = false;
        this.teleportLocation = null;
    }

    public boolean hasSavableData() {
        return teleportLocation != null;
    }
}
