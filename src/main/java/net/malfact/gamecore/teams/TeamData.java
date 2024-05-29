package net.malfact.gamecore.teams;

import org.bukkit.Location;

public class TeamData {
    public String name;
    public Location spawn;
    public Location exit;

    public TeamData(String name, Location spawn, Location exit) {
        this.name = name;
        this.spawn = spawn;
        this.exit = exit;
    }
}
