package net.malfact.gamecore.loot;

import net.malfact.gamecore.lib.Rarity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentGenerator {
	private static int[] weaponEnchantments = {16, 17, 18, 19, 20, 21, 34};
	private static int[] armorEnchantments = {0,1,2,3,4,5,6,7,34};
	
	public static void enchantWeapon(ItemStack item, Rarity rarity){
		
		int a = 0;
		if (rarity.getLevel() == 1)
			a = (int) Math.round(Math.random());
		if (rarity.getLevel() == 2)
			a = 1+(int) Math.round(Math.random());
		if (rarity.getLevel() == 3)
			a = 2+(int) Math.round(Math.random());
		if (rarity.getLevel() == 4)
			a = 3+(int) Math.round(Math.random()*2);
		if (rarity.getLevel() == 5)
			a = 5+(int) Math.round(Math.random()*2);
		
		for (int i=0; i<a; i++){
			int we = (int) Math.round(Math.random()*(weaponEnchantments.length-1));
			item.addUnsafeEnchantment(Enchantment.getById(weaponEnchantments[we]), 
					10-(10-2*rarity.getLevel() + ((int) Math.round(Math.random()*3)) ));
		}
	}
	
	public static void enchantArmor(ItemStack item, Rarity rarity){
		
		int a = 0;
		if (rarity.getLevel() == 1)
			a = (int) Math.round(Math.random());
		if (rarity.getLevel() == 2)
			a = 1+(int) Math.round(Math.random());
		if (rarity.getLevel() == 3)
			a = 2+(int) Math.round(Math.random());
		if (rarity.getLevel() == 4)
			a = 3+(int) Math.round(Math.random()*2);
		if (rarity.getLevel() == 5)
			a = 5+(int) Math.round(Math.random()*2);
		
		for (int i=0; i<a; i++){
			int we = (int) Math.round(Math.random()*(armorEnchantments.length-1));
			item.addUnsafeEnchantment(Enchantment.getById(armorEnchantments[we]), 
					10-(10-2*rarity.getLevel() + ((int) Math.round(Math.random()*3)) ));
		}
	}
}
