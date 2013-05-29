package net.malfact.gamecore.listeners;

import net.malfact.gamecore.tool.Tool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener{
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if (item == null) 
			return;
		
		Tool tool = Tool.getToolFromId(item.getTypeId());
		
		if (tool == null)
			return;
		
		if (event.getAction() == Action.LEFT_CLICK_AIR){
			if (tool.leftClickAir(player))
				event.setCancelled(true);
			
		} else if (event.getAction() == Action.LEFT_CLICK_BLOCK){
			if (tool.leftClickBlock(player))
				event.setCancelled(true);
			
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR){
			if (tool.rightClickAir(player))
				event.setCancelled(true);
			
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (tool.rightClickBlock(player))
				event.setCancelled(true);
			
		}
	}
}
