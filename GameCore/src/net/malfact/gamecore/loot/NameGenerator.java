package net.malfact.gamecore.loot;

import net.malfact.gamecore.GameCore.Rarity;

import org.bukkit.Material;

public class NameGenerator {
	static String[] suffix = {
		" of the Wolf",
		" of the Wind",
		" of the Sun",
		" of the Moon",
		" of Black Suns",
		" of Silver Ash",
		" of Forgotten Pasts",
	};

	public static String randomSuffix(){
		int s = (int)(Math.round(Math.random()*(suffix.length-1)));
		return suffix[s];
	}
	
	public static String generateRandomName(Object item, Rarity rarity){
		String name = "";
		if (item instanceof Weapon){
			Weapon weapon = (Weapon) item;
			if (rarity.getLevel() < 2)
				name = getName(weapon.getMaterial());
			else if (rarity.getLevel() > 1 && rarity.getLevel() < 4){
				name = getName(weapon.getMaterial())+randomSuffix();
			} else if (rarity.getLevel() == 4){
				name = Weapon.getRandomEpicOfType(weapon.getType()).getName();
			} else {
				name = Weapon.getRandomLegendaryOfType(weapon.getType()).getName();
			}
		} else if (item instanceof Armor){
			Armor armor = (Armor) item;
			if (rarity.getLevel() < 2)
				name = getName(armor.getMaterial());
			else if (rarity.getLevel() > 1 && rarity.getLevel() < 4){
				name = getName(armor.getMaterial())+randomSuffix();
			} else if (rarity.getLevel() == 4){
				name = Armor.getRandomEpicOfType(armor.getType()).getName();
			} else {
				name = Armor.getRandomLegendaryOfType(armor.getType()).getName();
			}
		}
		
		
		
		return name;
	}
	
	public static String getName(Material material){
		String refreshedName = "";
		String name = material.toString().toLowerCase();
		
		String[] a = name.split("");
		a[1]=a[1].toUpperCase();
		for (int i=1; i<a.length; i++){
			if (a[i].equalsIgnoreCase("_")){
				a[i] = " ";
				a[i+1] = a[i+1].toUpperCase();
			}
			refreshedName = refreshedName+a[i];
		}

		return refreshedName;
	}
}
