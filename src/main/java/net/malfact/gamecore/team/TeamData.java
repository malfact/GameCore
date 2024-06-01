package net.malfact.gamecore.team;

import org.bukkit.Location;

public class TeamData {
    public final String name;
    public final Location spawn;
    public final Location exit;
    public final boolean leaveOnDeath;

    public TeamData(String name, Location spawn, Location exit, boolean leaveOnDeath) {
        this.name = name;
        this.spawn = spawn;
        this.exit = exit;
        this.leaveOnDeath = leaveOnDeath;
    }
}
