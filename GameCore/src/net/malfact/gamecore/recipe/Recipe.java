package net.malfact.gamecore.recipe;
import net.malfact.gamecore.profession.*;
import org.bukkit.inventory.ItemStack;

public class Recipe {

	private String name;
	private ItemStack[] components;
	private ProfessionLevel level;
	private int playerlevel;
	
	public Recipe(String name, ProfessionLevel level, int playerlevel, ItemStack[] components) {
		this.name = name;
		this.level = level;
		this.playerlevel = playerlevel;
		this.components = components;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack[] getComponents() {
		return components;
	}
	
	public ProfessionLevel getLevel() {
		return level;
	}
	
	public int getPlayerlevel() {
		return playerlevel;
	}
}
