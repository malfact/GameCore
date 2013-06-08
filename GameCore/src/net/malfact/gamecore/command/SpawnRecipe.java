package net.malfact.gamecore.command;

import net.malfact.gamecore.recipe.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class SpawnRecipe extends PluginSubCommand{

	public SpawnRecipe(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		Player player = null;
		if (sender instanceof Player) player = (Player) sender;
		else return false;
		
		if (args.length < 2)
			return false;
		
		PlayerInventory inventory = player.getInventory();
		int slot = inventory.firstEmpty();
		if (slot == -1){
			player.sendMessage("§4No empty slots in inventory!");
			return true;
		}
		
		Recipe recipe = RecipeRegister.getRecipe(args[1]);
		
		if (recipe == null){
			player.sendMessage("§4Invalid Recipe!");
			return true;
		}
		
		inventory.setItem(slot, recipe.getRecipeItem());
		
		return true;
	}
}
