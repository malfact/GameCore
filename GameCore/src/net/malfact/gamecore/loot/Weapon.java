package net.malfact.gamecore.loot;

import java.util.ArrayList;

import net.malfact.gamecore.GameCore.*;
import net.malfact.gamecore.lib.Rarity;

import org.bukkit.Material;

public class Weapon {
	private Material material;
	private Rarity[] rarity;
	private Type type;
	private boolean canDrop;
	
	public static ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	public static ArrayList<epic> epics = new ArrayList<epic>();
	public static ArrayList<legendary> legendaries = new ArrayList<legendary>();
	
	public Weapon(Material material, Rarity[] rarity, Type type) {
		this.material = material;
		this.rarity = rarity;
		this.type = type;
		this.canDrop = true;
	}
	
	public Weapon(Material material, Rarity[] rarity, Type type, boolean canDrop) {
		this.material = material;
		this.rarity = rarity;
		this.type = type;
		this.canDrop = canDrop;
	}
	
	public Material getMaterial(){
		return this.material;
	}
	
	public Rarity[] getRarity(){
		return  this.rarity;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public boolean canDrop(){
		return this.canDrop;
	}
	
	public static Weapon[] getAllWeaponsOfRarity(Rarity rarity){
		Weapon[] a = weapons.toArray(new Weapon[weapons.size()]);
		ArrayList<Weapon> b = new ArrayList<Weapon>();
		for (int i = 0; i < a.length; i++){
			Rarity[] rare = a[i].getRarity();
			for (int j = 0; j < rare.length; j++){
				if (rare[j] == rarity) {
					b.add(a[i]);
					break;
				}
			}
		}
		return b.toArray(new Weapon[b.size()]);
	}
	
	public static epic[] getAllEpicsOfType(Type type){
		epic[] a = epics.toArray(new epic[epics.size()]);
		ArrayList<epic> b = new ArrayList<epic>();
		for (int i = 0; i < a.length; i++){
			if (a[i].getType() == type) {
				b.add(a[i]);
			}
		}
		return b.toArray(new epic[b.size()]);
	}
	
	public static legendary[] getAllLegendariesOfType(Type type){
		legendary[] a = legendaries.toArray(new legendary[legendaries.size()]);
		ArrayList<legendary> b = new ArrayList<legendary>();
		for (int i = 0; i < a.length; i++){
			if (a[i].getType() == type) {
				b.add(a[i]);
			}
		}
		return b.toArray(new legendary[b.size()]);
	}
	
	public static epic getRandomEpicOfType(Type type){
		epic[] a = Weapon.getAllEpicsOfType(type);
		int b = (int) Math.round(Math.random()*(a.length-1));
		return a[b];
	}
	
	public static legendary getRandomLegendaryOfType(Type type){
		legendary[] a = Weapon.getAllLegendariesOfType(type);
		int b = (int) Math.round(Math.random()*(a.length-1));
		return a[b];
	}
	
	public static Weapon getRandomWeaponOfRarity(Rarity rarity){
		Weapon[] a = Weapon.getAllWeaponsOfRarity(rarity);
		int b = (int) Math.round(Math.random()*(a.length-1));
		return a[b];
	}
}
