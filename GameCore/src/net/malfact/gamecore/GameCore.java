package net.malfact.gamecore;

import net.malfact.gamecore.lib.Options;
import net.malfact.gamecore.listeners.PlayerListener;
import net.malfact.gamecore.loot.ItemGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class GameCore extends JavaPlugin {
	
	public static enum Rarity {
		POOR(0,"§7","Poor"), 
		NORMAL(1,"§f","Normal"), 
		UNCOMMON(2,"§a","Uncommon"), 
		RARE(3,"§9","Rare"), 
		EPIC(4,"§5","Epic"), 
		LEGENDARY(5,"§6","Legendary");
		
		private final int level;
		private final String color;
		private final String text;
		private Rarity(int level, String color, String text){
			this.level = level;
			this.color = color;
			this.text = text;
		}
		
		public int getLevel(){return this.level;}
		public String getColor(){return this.color;}
		public String getText(){return this.text;}
		
		public static Rarity getFromInt(int lvl){
			switch (lvl){
				case 0:
					return POOR;
				case 1:
					return NORMAL;
				case 2:
					return UNCOMMON;
				case 3:
					return RARE;
				case 4:
					return EPIC;
				case 5: 
					return LEGENDARY;
				default:
					return POOR;
			}
		}
	}
	
	public static enum Type {
		SWORD, AXE, HELMET, CHESTPLATE, LEGGINGS, BOOTS, OTHER;
	}
	
	public static class epic{
		private String name;
		private Type type;
		
		public epic(String name, Type type) {
			this.name = name;
			this.type = type;
		}
		public String getName() {return name;}
		public Type getType() {return type;}
	}
	
	public static class legendary{
		private String name;
		private Type type;
		
		public legendary(String name, Type type) {
			this.name = name;
			this.type = type;
		}
		public String getName() {return name;}
		public Type getType() {return type;}
	}
	
	@Override
	public void onEnable() {
		/* Init Config */
		getConfig().options().copyDefaults(true);
		saveConfig();
		/* End Init Config */
		
		Options.loadOptions(this);
		
		ItemGenerator.populateItems();
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		getCommand("gc").setExecutor(new GameCoreCommandExecutor(this));
	}
	 @Override
	public void onDisable() { 
	}
}
