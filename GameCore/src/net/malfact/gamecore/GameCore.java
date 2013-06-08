package net.malfact.gamecore;

import java.lang.reflect.Field;
import net.malfact.gamecore.command.PluginCommand;
import net.malfact.gamecore.command.RandomArmor;
import net.malfact.gamecore.command.RandomWeapon;
import net.malfact.gamecore.command.ReloadConfig;
import net.malfact.gamecore.command.SpawnRecipe;
import net.malfact.gamecore.lib.Options;
import net.malfact.gamecore.listeners.PlayerListener;
import net.malfact.gamecore.loot.ItemGenerator;
import net.malfact.gamecore.profession.Profession;
import net.malfact.gamecore.recipe.Recipe;
import net.malfact.gamecore.recipe.RecipeRegister;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GameCore extends JavaPlugin {
	
	private static CommandMap cmap;
	
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
	
	private static GameCore instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		/* Init Config */
		getConfig().options().copyDefaults(true);
		saveConfig();
		/*--------------------*/
		
		Options.loadOptions(this);
		
		ItemGenerator.populateItems();
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		RecipeRegister.addRecipe(new Recipe("Iron_Bar", Profession.mining, 0, 0, new ItemStack[]{}, new ItemStack[]{new ItemStack(Material.IRON_BLOCK, 1)}));
	
		/* Get Command Map */
		try{
            if(Bukkit.getServer() instanceof CraftServer){
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap)f.get(Bukkit.getServer());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
		/*--------------------*/
		
		/*Register Commands*/
		cmap.register("gamecore", 
				new PluginCommand("gamecore", this, "gc", "gamec", "gcore")
					.setSubCommands(
							new RandomArmor("randomarmor").setUsage("/gamecore randomarmor [rarity]").setDescription("Spawn random piece of [rarity] armor.").setPermission("gamecore.spawn.armor"),
							new RandomWeapon("randomweapon").setUsage("/gamecore randomweapon [rarity]").setDescription("Spawn random piece of [rarity] weapon.").setPermission("gamecore.spawn.weapon"),
							new ReloadConfig("reloadconfig").setUsage("/gamecore reloadconfig").setDescription("Reload GameCore config file.").setPermission("gamecore.util.reloadconfig"),
							new SpawnRecipe("spawnrecipe").setUsage("/gamecore spawnrecipe <recipe>").setDescription("Spawn recipe item for <recipe>.").setPermission("gamecore.spawn.recipe")
					).setUsage("/gamecore [cmd]").setDescription("Help for GameCore commands.")
				);
		/*--------------------*/
	}
	 @Override
	public void onDisable() { 
	}
	 
	public static GameCore getInstance(){
		return instance;
	}
	
	public static CommandMap getCommandMap(){
        return cmap;
    }
}
