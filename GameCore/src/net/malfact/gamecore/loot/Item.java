package net.malfact.gamecore.loot;

import java.util.ArrayList;
import net.malfact.gamecore.GameCore.Rarity;
import net.malfact.gamecore.GameCore.Type;
import net.malfact.gamecore.GameCore.epic;
import net.malfact.gamecore.GameCore.legendary;
import org.bukkit.Material;

public class Item {
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<epic> epics = new ArrayList<epic>();
	public static ArrayList<legendary> legendaries = new ArrayList<legendary>();
	
	private Material material;
	private Rarity[] rarity;
	private Type type;
	private boolean canDrop;
	
	public Item(Material material, Rarity[] rarity, Type type) {
		this.material = material;
		this.rarity = rarity;
		this.type = type;
	}
	
	public Item(Material material, Rarity[] rarity, Type type, boolean canDrop) {
		this.material = material;
		this.rarity = rarity;
		this.type = type;
		this.canDrop = canDrop;
	}
	
	public Material getMaterial(){return this.material;}
	public Rarity[] getRarity(){return  this.rarity;}
	public Type getType(){return this.type;}
	public boolean canDrop(){return this.canDrop;}
	
	public static Item[] getItemsOfTypeAndRarity(Type type, Rarity rarity){
		Item[] a = items.toArray(new Item[items.size()]);
		ArrayList<Item> b = new ArrayList<Item>();
		for (int i = 0; i < a.length; i++){
			if (a[i].getType() == type) {
				Rarity[] rare = a[i].getRarity();
				for (int j = 0; j < rare.length; j++){
					if (rare[j] == rarity) {
						b.add(a[i]);
						break;
					}
				}
			}
		}
		return b.toArray(new Item[b.size()]);
	}
	
	public static Item getRandomItemOfTypeAndRarity(Type type, Rarity rarity){
		Item[] a = Item.getItemsOfTypeAndRarity(type, rarity);
		int b = (int) Math.round(Math.random()*(a.length-1));
		return a[b];
	}
}
