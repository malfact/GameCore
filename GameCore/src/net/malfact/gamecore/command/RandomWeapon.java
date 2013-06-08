package net.malfact.gamecore.command;

import net.malfact.gamecore.lib.Rarity;
import net.malfact.gamecore.loot.ItemGenerator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomWeapon extends PluginSubCommand{
	public RandomWeapon(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		Player player = null;
		if (sender instanceof Player) player = (Player) sender;
		else return false;
		
		switch (args.length){
			case 1:
				ItemGenerator.spawnRandomWeapon(player);
				return true;
			case 2:
				try {
					Rarity r = Rarity.valueOf(args[1].toUpperCase());
					ItemGenerator.spawnRandomWeapon(r, player);
				} catch (IllegalArgumentException e){
					player.sendMessage("§4Invalid Rarity!");
				}
				return true;
		}
		return false;
	}
}
