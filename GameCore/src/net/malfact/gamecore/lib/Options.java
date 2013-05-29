package net.malfact.gamecore.lib;

import net.malfact.gamecore.GameCore;

public class Options {
	
	//Not Defaults
	public static int NetherStarCooldown = 0; //60 minutes = 3600 seconds
	public static int SpawnCooldown = 0;
	
	public static void loadOptions(GameCore plugin){
		NetherStarCooldown = plugin.getConfig().getInt("tools.netherstar.cooldown", 5);
		SpawnCooldown = plugin.getConfig().getInt("spawn.cooldown", 5);
		
		plugin.getLogger().info("Config Reloaded!");
	}
}
