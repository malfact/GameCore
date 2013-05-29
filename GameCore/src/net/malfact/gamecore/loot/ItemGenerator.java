package net.malfact.gamecore.loot;

import java.util.ArrayList;
import java.util.List;
import net.malfact.gamecore.GameCore.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGenerator {		
	public static void populateItems(){
		Weapon.weapons.add(new Weapon(Material.WOOD_SWORD, new Rarity[]{Rarity.POOR,Rarity.NORMAL}, Type.SWORD));
		Weapon.weapons.add(new Weapon(Material.WOOD_AXE, new Rarity[]{Rarity.POOR,Rarity.NORMAL}, Type.AXE));
		Weapon.weapons.add(new Weapon(Material.STONE_SWORD, new Rarity[]{Rarity.NORMAL,Rarity.UNCOMMON}, Type.SWORD));
		Weapon.weapons.add(new Weapon(Material.STONE_AXE, new Rarity[]{Rarity.NORMAL,Rarity.UNCOMMON}, Type.AXE));
		Weapon.weapons.add(new Weapon(Material.IRON_SWORD, new Rarity[]{Rarity.UNCOMMON,Rarity.RARE,Rarity.EPIC}, Type.SWORD));
		Weapon.weapons.add(new Weapon(Material.IRON_AXE, new Rarity[]{Rarity.UNCOMMON,Rarity.RARE,Rarity.EPIC}, Type.AXE));
		Weapon.weapons.add(new Weapon(Material.GOLD_SWORD, new Rarity[]{Rarity.RARE,Rarity.EPIC}, Type.SWORD));
		Weapon.weapons.add(new Weapon(Material.GOLD_AXE, new Rarity[]{Rarity.RARE,Rarity.EPIC}, Type.AXE));
		Weapon.weapons.add(new Weapon(Material.DIAMOND_SWORD, new Rarity[]{Rarity.RARE,Rarity.EPIC,Rarity.LEGENDARY}, Type.SWORD));
		Weapon.weapons.add(new Weapon(Material.DIAMOND_AXE, new Rarity[]{Rarity.RARE,Rarity.EPIC,Rarity.LEGENDARY}, Type.AXE));
		
		Weapon.epics.add(new epic("Null Sword Of Null", Type.SWORD));
		Weapon.epics.add(new epic("Null Axe Of Null", Type.AXE));
		Weapon.legendaries.add(new legendary("Heartstone Blade", Type.SWORD));
		Weapon.legendaries.add(new legendary("Infinity Blade", Type.SWORD));
		Weapon.legendaries.add(new legendary("Hashbringer", Type.SWORD));
		Weapon.legendaries.add(new legendary("Yorick's Bloody Axe", Type.AXE));
		Weapon.legendaries.add(new legendary("Obsidian Destroyer", Type.AXE));
		Weapon.legendaries.add(new legendary("Ragnarök", Type.AXE));
		
		Armor.armor.add(new Armor(Material.LEATHER_HELMET, new Rarity[]{Rarity.POOR,Rarity.NORMAL}, Type.HELMET));
		Armor.armor.add(new Armor(Material.LEATHER_CHESTPLATE, new Rarity[]{Rarity.POOR,Rarity.NORMAL}, Type.CHESTPLATE));
		Armor.armor.add(new Armor(Material.LEATHER_LEGGINGS, new Rarity[]{Rarity.POOR,Rarity.NORMAL}, Type.LEGGINGS));
		Armor.armor.add(new Armor(Material.LEATHER_BOOTS, new Rarity[]{Rarity.POOR,Rarity.NORMAL}, Type.BOOTS));
		Armor.armor.add(new Armor(Material.CHAINMAIL_HELMET, new Rarity[]{Rarity.NORMAL,Rarity.UNCOMMON}, Type.HELMET));
		Armor.armor.add(new Armor(Material.CHAINMAIL_CHESTPLATE, new Rarity[]{Rarity.NORMAL,Rarity.UNCOMMON}, Type.CHESTPLATE));
		Armor.armor.add(new Armor(Material.CHAINMAIL_LEGGINGS, new Rarity[]{Rarity.NORMAL,Rarity.UNCOMMON}, Type.LEGGINGS));
		Armor.armor.add(new Armor(Material.CHAINMAIL_BOOTS, new Rarity[]{Rarity.NORMAL,Rarity.UNCOMMON}, Type.BOOTS));
		Armor.armor.add(new Armor(Material.IRON_HELMET, new Rarity[]{Rarity.UNCOMMON,Rarity.RARE,Rarity.EPIC}, Type.HELMET));
		Armor.armor.add(new Armor(Material.IRON_CHESTPLATE, new Rarity[]{Rarity.UNCOMMON,Rarity.RARE,Rarity.EPIC}, Type.CHESTPLATE));
		Armor.armor.add(new Armor(Material.IRON_LEGGINGS, new Rarity[]{Rarity.UNCOMMON,Rarity.RARE,Rarity.EPIC}, Type.LEGGINGS));
		Armor.armor.add(new Armor(Material.IRON_BOOTS, new Rarity[]{Rarity.UNCOMMON,Rarity.RARE,Rarity.EPIC}, Type.BOOTS));
		Armor.armor.add(new Armor(Material.GOLD_HELMET, new Rarity[]{Rarity.RARE,Rarity.EPIC}, Type.HELMET));
		Armor.armor.add(new Armor(Material.GOLD_CHESTPLATE, new Rarity[]{Rarity.RARE,Rarity.EPIC}, Type.CHESTPLATE));
		Armor.armor.add(new Armor(Material.GOLD_LEGGINGS, new Rarity[]{Rarity.RARE,Rarity.EPIC}, Type.LEGGINGS));
		Armor.armor.add(new Armor(Material.GOLD_BOOTS, new Rarity[]{Rarity.RARE,Rarity.EPIC}, Type.BOOTS));
		Armor.armor.add(new Armor(Material.DIAMOND_HELMET, new Rarity[]{Rarity.RARE,Rarity.EPIC,Rarity.LEGENDARY}, Type.HELMET));
		Armor.armor.add(new Armor(Material.DIAMOND_CHESTPLATE, new Rarity[]{Rarity.RARE,Rarity.EPIC,Rarity.LEGENDARY}, Type.CHESTPLATE));
		Armor.armor.add(new Armor(Material.DIAMOND_LEGGINGS, new Rarity[]{Rarity.RARE,Rarity.EPIC,Rarity.LEGENDARY}, Type.LEGGINGS));
		Armor.armor.add(new Armor(Material.DIAMOND_BOOTS, new Rarity[]{Rarity.RARE,Rarity.EPIC,Rarity.LEGENDARY}, Type.BOOTS));
	}
	
	public static void spawnRandomArmor(Player player){
		if (player == null)
			return;
		
		ItemStack item = randomArmor();
		
		Inventory inv = player.getInventory();
		if (inv.firstEmpty() != -1)
			inv.setItem(inv.firstEmpty(), item);
	}
	
	public static void spawnRandomArmor(Rarity rarity, Player player){
		if (player == null)
			return;
		
		ItemStack item = randomArmor(rarity);
		
		Inventory inv = player.getInventory();
		if (inv.firstEmpty() != -1)
			inv.setItem(inv.firstEmpty(), item);
	}
	
	public static ItemStack randomArmor(){
		double rareBase = ((double)Math.round(Math.random()*100000))/1000;
		int rarity = (rareBase <= 10.0) ? 0 : (rareBase > 10.0 && rareBase <= 90.0) ? 1 : (rareBase > 90.0 && rareBase <= 99.0) ? 2 : (rareBase > 99.0 && rareBase <= 99.9) ? 3 : (rareBase > 99.9 && rareBase <= 99.99) ? 4 : (rareBase > 99.9 && rareBase <= 99.99) ? 4 : 5;
		
		return randomArmor(Rarity.getFromInt(rarity));
	}
	
	public static ItemStack randomArmor(Rarity rarity){
		Armor armor = Armor.getRandomArmorOfRarity(rarity);
		
		ItemStack item = new ItemStack(armor.getMaterial(), 1);
		ItemMeta im = item.getItemMeta();
		
		List<String> lore= new ArrayList<String>();
		{
			lore.add(rarity.getColor()+rarity.getText());
		}
		im.setLore(lore);
		im.setDisplayName(rarity.getColor()+NameGenerator.generateRandomName(armor, rarity));
		item.setItemMeta(im);

		EnchantmentGenerator.enchantArmor(item, rarity);
		
		return item;
	}
	
	public static void spawnRandomWeapon(Player player){
		if (player == null)
			return;
		
		ItemStack item = randomWeapon();
		
		Inventory inv = player.getInventory();
		if (inv.firstEmpty() != -1)
			inv.setItem(inv.firstEmpty(), item);
	}
	
	public static void spawnRandomWeapon(Rarity rarity, Player player){
		if (player == null)
			return;
		
		ItemStack item = randomWeapon(rarity);
		
		Inventory inv = player.getInventory();
		if (inv.firstEmpty() != -1)
			inv.setItem(inv.firstEmpty(), item);
	}
	
	public static ItemStack randomWeapon(){
		double rareBase = ((double)Math.round(Math.random()*100000))/1000;
		int rarity = (rareBase <= 10.0) ? 0 : (rareBase > 10.0 && rareBase <= 90.0) ? 1 : (rareBase > 90.0 && rareBase <= 99.0) ? 2 : (rareBase > 99.0 && rareBase <= 99.9) ? 3 : (rareBase > 99.9 && rareBase <= 99.99) ? 4 : (rareBase > 99.9 && rareBase <= 99.99) ? 4 : 5;
		
		return randomWeapon(Rarity.getFromInt(rarity));
	}
	
	public static ItemStack randomWeapon(Rarity rarity){
		Weapon weapon = Weapon.getRandomWeaponOfRarity(rarity);
		
		ItemStack item = new ItemStack(weapon.getMaterial(), 1);
		ItemMeta im = item.getItemMeta();
		
		List<String> lore= new ArrayList<String>();
		{
			lore.add(rarity.getColor()+rarity.getText());
		}
		im.setLore(lore);
		im.setDisplayName(rarity.getColor()+NameGenerator.generateRandomName(weapon, rarity));
		item.setItemMeta(im);

		EnchantmentGenerator.enchantWeapon(item, rarity);
		
		return item;
	}
}
