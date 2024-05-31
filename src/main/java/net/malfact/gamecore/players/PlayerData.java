package net.malfact.gamecore.players;

import org.bukkit.Location;

import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerData {

    public final UUID uuid;
    public final String name;
    public final Location teleportLocation;
    public final String time;

    public PlayerData(UUID uuid, String name, Location teleportLocation) {
        this.uuid = uuid;
        this.name = name;
        this.teleportLocation = teleportLocation;
        this.time = Instant.now().toString();
    }
}
