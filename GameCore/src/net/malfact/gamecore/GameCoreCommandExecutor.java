package net.malfact.gamecore;

import net.malfact.gamecore.GameCore.Rarity;
import net.malfact.gamecore.lib.Options;
import net.malfact.gamecore.loot.ItemGenerator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCoreCommandExecutor implements CommandExecutor {

	GameCore plugin;
	
	public GameCoreCommandExecutor(GameCore plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String list, String[] args) {
		Player player = null;
		if (sender instanceof Player) player = (Player) sender;
		
		if (!cmd.getName().equalsIgnoreCase("gc"))
			return false;
		if (player == null)
			return false;
		if (args.length == 0)
			return false;
		
		if (args.length == 1) {
			switch (args[0].toLowerCase()){
				case "randomweapon":
					if (sender.hasPermission("gamecore.spawn.weapon"))
						ItemGenerator.spawnRandomWeapon(player);
					break;
				case "randomarmor":
					if (sender.hasPermission("gamecore.spawn.armor"))
						ItemGenerator.spawnRandomArmor(player);
					break;
				case "reloadconfig":
					if (sender.hasPermission("gamecore.util.reloadconfig")) {
						plugin.reloadConfig();
						Options.loadOptions(plugin);
					}
					break;
			}
		} else if (args.length == 2){
			switch (args[0].toLowerCase()){
				case "randomweapon":
					if (sender.hasPermission("gamecore.spawn.weapon")){
						try {
							Rarity r = Rarity.valueOf(args[1].toUpperCase());
							ItemGenerator.spawnRandomWeapon(r, player);
						} catch (IllegalArgumentException e){
							player.sendMessage(ChatColor.RED+"Invalid Rarity!");
						}
					}
					break;
				case "randomarmor":
					if (sender.hasPermission("gamecore.spawn.armor")){
						try {
							Rarity r = Rarity.valueOf(args[1].toUpperCase());
							ItemGenerator.spawnRandomArmor(r, player);
						} catch (IllegalArgumentException e){
							player.sendMessage(ChatColor.RED+"Invalid Rarity!");
						}
					}
					break;
				}
		}
		
		return true;
	}

}
