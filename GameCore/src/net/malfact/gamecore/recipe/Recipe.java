package net.malfact.gamecore.recipe;

import java.util.ArrayList;
import java.util.List;

import net.malfact.gamecore.lib.ItemData;
import net.malfact.gamecore.profession.*;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipe {
	private String name;
	private ItemStack[] results;
	private ItemStack[] components;
	private int skill;
	private int playerlevel;
	private Profession profession;
	
	public Recipe(String name, Profession profession, int skill, int playerlevel, ItemStack[] results, ItemStack[] components) {
		this.name = name.replace(" ", "_");
		this.profession = profession;
		this.skill = skill;
		this.playerlevel = playerlevel;
		this.results = results;
		this.components = components;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack[] getComponents() {
		return components;
	}
	
	public int getSkill() {
		return skill;
	}
	
	public int getPlayerlevel() {
		return playerlevel;
	}
	
	public ItemStack[] getResults(){
		return results;
	}
	
	public Profession getProfession(){
		return profession;
	}
	
	public ItemStack getRecipeItem(){
		ItemStack item = new ItemStack(Material.PAPER, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName("§b"+getName().replace("_", " ")+" recipe");
		List<String> lore= new ArrayList<String>();
		{
			lore.add("§c" + getSkill() + " " + getProfession().getName());
			lore.add("§cLevel " + getPlayerlevel());
			for (ItemStack s : getComponents()){
				lore.add("§7" + s.getAmount() + "x" + ItemData.getLocalizedName(s));
			}
			lore.add("§aRight Click to add to recipes!");
		}
		im.setLore(lore);
		item.setItemMeta(im);
		
		return item;
	}
}
