package net.malfact.gamecore.players;

import org.bukkit.Location;

import java.time.Instant;
import java.util.UUID;

public class PlayerData {

    public UUID uuid;
    public String name;
    public Location teleportLocation;
    public String time;

    public PlayerData(UUID uuid, String name, Location teleportLocation) {
        this.uuid = uuid;
        this.name = name;
        this.teleportLocation = teleportLocation;
        this.time = Instant.now().toString();
    }
}
