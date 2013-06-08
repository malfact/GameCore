package net.malfact.gamecore.tool;

import java.util.ArrayList;
import java.util.List;

import net.malfact.gamecore.lib.Options;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NetherStarTool extends Tool{
	
	public NetherStarTool(int id) {
		super(id);
	}

	public boolean rightClickAir(Player player){
		if (player.hasPermission("gamecore.tool.netherstar"))
			return rightClick(player);
		else
			return false;
	}
	
	public boolean rightClickBlock(Player player){
		if (player.hasPermission("gamecore.tool.netherstar"))
			return rightClick(player);
		else
			return false;
	}
	
	private boolean rightClick(Player player){
		ItemStack item = player.getItemInHand();
		
		if (item == null)
			return false;
		
		if (item.getAmount() > 1 ){
			player.sendMessage("§5Too many nether stars in hand!");
			return true;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if (!meta.hasLore()){
			meta.setDisplayName("§5Nether Star Recall");
			
			List<String> lore= new ArrayList<String>();
			{
				lore.add("§aRight Click to return to this recall point!");
				lore.add(player.getWorld().getName() + ";" + player.getLocation().getBlockX() + ";" + player.getLocation().getBlockY() + ";" + player.getLocation().getBlockZ());
				lore.add("§5Last Use: " + 0);
			}
			
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			player.sendMessage("§5The nether star has been allocated!");
			
			return true;
		} else {
			List<String> lore = meta.getLore();
			String[] lines = lore.toArray(new String[lore.size()]);
			
			String[] pos = lines[1].split(";");
			
			World world = Bukkit.getServer().getWorld(pos[0]);
			int x = Integer.parseInt(pos[1]);
			int y = Integer.parseInt(pos[2]);
			int z = Integer.parseInt(pos[3]);
			
			String[] time = lines[2].split(": ");
			
			long oldTime = Long.parseLong(time[1]);
			long newTime = System.currentTimeMillis();
			
			long lapse = (newTime - oldTime)/1000;
			if (lapse >= Options.NetherStarCooldown || player.hasPermission("gamecore.tool.netherstar.bypasscooldown")){
				lore.set(2, "§5Last Use: " + newTime);
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				player.sendMessage("§5The nether star vibrates with magical energy!");
				
				world.playEffect(player.getLocation(), Effect.SMOKE, 31);
				world.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
				
				player.teleport(new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch()), TeleportCause.PLUGIN);
				
				world.playEffect(player.getLocation(), Effect.SMOKE, 31);
				world.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
			} else {
				player.sendMessage("§5The nether star is still recharging!");
				player.sendMessage("§5"+(Options.NetherStarCooldown-lapse)+" seconds remaining!");
				return true;
			}
		}
		return false;
	}
}
