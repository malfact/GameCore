package net.malfact.gamecore.lib;

import java.util.ArrayList;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ItemData {
	
	/* Blocks */
	AIR(Material.AIR, 0, "Air", Rarity.NORMAL),
	STONE(Material.STONE, 0, "Stone", Rarity.NORMAL),
	GRASS(Material.GRASS, 0, "Grass", Rarity.NORMAL),
	COBBLESTONE(Material.COBBLESTONE, 0, "Cobblestone", Rarity.NORMAL),
	OAK_WOOD(Material.WOOD, 0, "Oak Wood Planks", Rarity.NORMAL),
	SPRUCE_WOOD(Material.WOOD, 1, "Spruce Wood Planks", Rarity.NORMAL),
	BIRCH_WOOD(Material.WOOD, 2, "Birch Wood Planks", Rarity.NORMAL),
	JUNGLE_WOOD(Material.WOOD, 3, "Jungle Wood Planks", Rarity.NORMAL),
	OAK_SAPLING(Material.WOOD, 0, "Oak Sapling", Rarity.NORMAL),
	SPRUCE_SAPLING(Material.WOOD, 1, "Spruce Sapling", Rarity.NORMAL),
	BIRCH_SAPLING(Material.WOOD, 2, "Birch Sapling", Rarity.NORMAL),
	JUNGLE_SAPLING(Material.WOOD, 3, "Jungle Sapling", Rarity.NORMAL),
	BEDROCK(Material.BEDROCK, 0, "Bedrock", Rarity.NORMAL),
	WATER(Material.WATER, 0, "Water", Rarity.NORMAL),
	STATIONARY_WATER(Material.STATIONARY_WATER, 0, "Water", Rarity.NORMAL),
	LAVA(Material.LAVA, 0, "Lava", Rarity.NORMAL),
	STATIONARY_LAVA(Material.STATIONARY_LAVA, 0, "Lava", Rarity.NORMAL),
	SAND(Material.SAND, 0, "Sand", Rarity.NORMAL),
	GRAVEL(Material.GRAVEL, 0, "Gravel", Rarity.NORMAL),
	GOLD_ORE(Material.GOLD_ORE, 0, "Gold Ore", Rarity.NORMAL),
	IRON_ORE(Material.IRON_ORE, 0, "Iron Ore", Rarity.NORMAL),
	COAL_ORE(Material.COAL_ORE, 0, "Coal Ore", Rarity.NORMAL),
	OAK_LOG(Material.LOG, 0, "Oak Wood", Rarity.NORMAL),
	SPRUCE_LOG(Material.LOG, 1, "Spruce Wood", Rarity.NORMAL),
	BIRCH_LOG(Material.LOG, 2, "Birch Log", Rarity.NORMAL),
	JUNGLE_LOG(Material.LOG, 3, "Jungle Wood", Rarity.NORMAL),
	OAK_LEAVES(Material.LEAVES, 0, "Oak Leaves", Rarity.NORMAL),
	SPRUCE_LEAVES(Material.LEAVES, 1, "Spruce Leaves", Rarity.NORMAL),
	BIRCH_LEAVES(Material.LEAVES, 2, "Birch Leaves", Rarity.NORMAL),
	JUNGLE_LEAVES(Material.LEAVES, 3, "Jungle LEaves", Rarity.NORMAL),
	SPONGE(Material.SPONGE, 0, "Sponge", Rarity.UNCOMMON),
	GLASS(Material.GLASS, 0, "Glass", Rarity.NORMAL),
	LAPIS_ORE(Material.LAPIS_ORE, 0, "Lapis Lazuli Ore", Rarity.NORMAL),
	LAPIS_BLOCK(Material.LAPIS_BLOCK, 0, "Lapis Lazuli Block", Rarity.UNCOMMON),
	DISPENSER(Material.DISPENSER, 0, "Dispenser", Rarity.NORMAL),
	SANDSTONE(Material.SANDSTONE, 0, "Sandstone", Rarity.NORMAL),
	CHISELED_SANDSTONE(Material.SANDSTONE, 1, "Chiseled Sandstone", Rarity.NORMAL),
	SMOOTH_SANDSTONE(Material.SANDSTONE, 2, "Smooth Sandstone", Rarity.NORMAL),
	NOTE_BLOCK(Material.NOTE_BLOCK, 0, "Note Block", Rarity.NORMAL),
	POWERED_RAIL(Material.POWERED_RAIL, 0, "Powered Rail", Rarity.NORMAL),
	DETECTOR_RAIL(Material.DETECTOR_RAIL, 0, "Detector Rail", Rarity.NORMAL),
	STICKY_PISTON(Material.PISTON_STICKY_BASE, 0, "Sticky Piston", Rarity.NORMAL),
	WEB(Material.WEB, 0, "Web", Rarity.NORMAL),
	DEAD_SHRUB(Material.LONG_GRASS, 0, "Dead Shrub", Rarity.NORMAL),
	LONG_GRASS(Material.LONG_GRASS, 1, "Long Grass", Rarity.NORMAL),
	FERN(Material.LONG_GRASS, 2, "Fern", Rarity.NORMAL),
	DEAD_BUSH(Material.DEAD_BUSH, 0, "Dead Bush", Rarity.NORMAL),
	PISTON(Material.PISTON_BASE, 0, "Piston", Rarity.NORMAL),
	WHITE_WOOL(Material.WOOL, 0, "White Wool", Rarity.NORMAL),
	ORANGE_WOOL(Material.WOOL, 1, "Orange Wool", Rarity.NORMAL),
	MAGENTA_WOOL(Material.WOOL, 2, "Megenta Wool", Rarity.NORMAL),
	LIGHT_BLUE_WOOL(Material.WOOL,30, "Light Blue Wool", Rarity.NORMAL),
	YELLOW_WOOL(Material.WOOL, 4, "Yellow Wool", Rarity.NORMAL),
	LIME_WOOL(Material.WOOL, 5, "Lime Wool", Rarity.NORMAL),
	PINK_WOOL(Material.WOOL, 6, "Pink Wool", Rarity.NORMAL),
	GRAY_WOOL(Material.WOOL, 7, "Gray Wool", Rarity.NORMAL),
	LIGHT_GRAY_WOOL(Material.WOOL, 8, "Light Gray Wool", Rarity.NORMAL),
	CYAN_WOOL(Material.WOOL, 9, "Cyan Wool", Rarity.NORMAL),
	PURPLE_WOOL(Material.WOOL, 10, "Purple Wool", Rarity.NORMAL),
	BLUE_WOOL(Material.WOOL, 11, "Blue Wool", Rarity.NORMAL),
	BROWN_WOOL(Material.WOOL, 12, "Brown Wool", Rarity.NORMAL),
	GREEN_WOOL(Material.WOOL, 13, "Green Wool", Rarity.NORMAL),
	RED_WOOL(Material.WOOL, 14, "Red Wool", Rarity.NORMAL),
	BLACK_WOOL(Material.WOOL, 15, "Black Wool", Rarity.NORMAL),
	YELLOW_FLOWER(Material.YELLOW_FLOWER, 0, "Yellow Flower", Rarity.NORMAL),
	RED_FLOWER(Material.RED_ROSE, 0, "Red Flower", Rarity.NORMAL),
	BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 0, "Brown Mushroom", Rarity.NORMAL),
	RED_MUSHROOM(Material.RED_MUSHROOM, 0, "Red Mushroom", Rarity.NORMAL),
	GOLD_BLOCK(Material.GOLD_BLOCK, 0, "Gold Block", Rarity.UNCOMMON),
	IRON_BLOCK(Material.IRON_BLOCK, 0, "Iron Block", Rarity.UNCOMMON),
	DOUBLE_STEP(Material.DOUBLE_STEP, 0, "Double Slab", Rarity.NORMAL),
	STONE_SLAB(Material.STEP, 0, "Stone Slab", Rarity.NORMAL),
	SANDSTONE_SLAB(Material.STEP, 1, "Sandstone Slab", Rarity.NORMAL),
	WOOD_SLAB(Material.STEP, 2, "Wood Slab", Rarity.NORMAL),
	COBBLESTONE_SLAB(Material.STEP, 3, "Cobblestone Slab", Rarity.NORMAL),
	BRICK_SLAB(Material.STEP, 4, "Brick Slab", Rarity.NORMAL),
	STONE_BRICK_SLAB(Material.STEP, 5, "Stone Brick Slab", Rarity.NORMAL),
	NETHER_BRICK_SLAB(Material.STEP, 6, "Nether Brick Slab", Rarity.NORMAL),
	QUARTZ_SLAB(Material.STEP, 7, "Quartz Slab", Rarity.NORMAL),
	BRICKS(Material.BRICK, 0, "Bricks", Rarity.NORMAL),
	TNT(Material.TNT, 0, "TNT", Rarity.NORMAL),
	BOOKSHELF(Material.BOOKSHELF, 0, "Bookshelf", Rarity.NORMAL),
	MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, 0, "Mossy Cobblestone", Rarity.NORMAL),
	OBSIDIAN(Material.OBSIDIAN, 0, "Obsidian", Rarity.NORMAL),
	TORCH(Material.TORCH, 0, "Torch", Rarity.NORMAL),
	FIRE(Material.FIRE, 0, "Fire", Rarity.UNCOMMON),
	SPAWNER(Material.MOB_SPAWNER, 0, "Monster Spawner", Rarity.NORMAL),
	OAK_STAIRS(Material.WOOD_STAIRS, 0, "Oak Stairs", Rarity.NORMAL),
	CHEST(Material.CHEST, 0, "Chest", Rarity.NORMAL),
	DIAMOND_ORE(Material.DIAMOND_ORE, 0, "Diamond Ore", Rarity.UNCOMMON),
	DIAMOND_BLOCK(Material.DIAMOND_BLOCK, 0, "Diamond Block", Rarity.RARE),
	WORKBENCH(Material.WORKBENCH, 0, "Crafting Table", Rarity.NORMAL),
	LADDER(Material.LADDER, 0, "Ladder", Rarity.NORMAL),
	RAILS(Material.RAILS, 0, "Rails", Rarity.NORMAL),
	COBBLESTONE_STAIRS(Material.COBBLESTONE_STAIRS, 0, "Cobblestone Stairs", Rarity.NORMAL),
	LEVER(Material.LEVER, 0, "Lever", Rarity.NORMAL),
	STONE_PLATE(Material.STONE_PLATE, 0, "Pressure Plate", Rarity.NORMAL),
	WOOD_PLATE(Material.WOOD_PLATE, 0, "Pressure Plate", Rarity.NORMAL),
	REDSTONE_ORE(Material.REDSTONE_ORE, 0, "Redstone Ore", Rarity.NORMAL),
	REDSTONE_TORCH(Material.REDSTONE_TORCH_ON, 0, "Redstone Torch", Rarity.NORMAL),
	STONE_BUTTON(Material.STONE_BUTTON, 0, "Stone Button", Rarity.NORMAL),
	SNOW(Material.SNOW, 0, "Snow", Rarity.NORMAL),
	ICE(Material.ICE, 0, "Ice", Rarity.NORMAL),
	SNOW_BLOCK(Material.SNOW_BLOCK, 0, "Snow Block", Rarity.NORMAL),
	CACTUS(Material.CACTUS, 0, "Cactus", Rarity.NORMAL),
	CLAY_BLOCK(Material.CLAY, 0, "Clay Block", Rarity.NORMAL),
	JUKEBOX(Material.JUKEBOX, 0, "Jukebox", Rarity.NORMAL),
	FENCE(Material.FENCE, 0, "Fence", Rarity.NORMAL),
	PUMPKIN(Material.PUMPKIN, 0, "Pumpkin", Rarity.NORMAL),
	NETHERRACK(Material.NETHERRACK, 0, "Netherrack", Rarity.NORMAL),
	SOUL_SAND(Material.SOUL_SAND, 0, "Soul Sand", Rarity.NORMAL),
	GLOWSTONE(Material.GLOWSTONE, 0, "Glowstone", Rarity.NORMAL),
	JACK_O_LATERN(Material.JACK_O_LANTERN, 0, "Jack 'o' Latern", Rarity.NORMAL),
	RED_MUSHROOM_CAP(Material.HUGE_MUSHROOM_1, 0, "Mushroom Block", Rarity.NORMAL),
	BROWN_MUSHROOM_CAP(Material.HUGE_MUSHROOM_2, 0, "Mushroom Block", Rarity.NORMAL),
	IRON_BARS(Material.IRON_FENCE, 0, "Iron Bars", Rarity.NORMAL),
	GLASS_PANE(Material.THIN_GLASS, 0, "Glass Pane", Rarity.NORMAL),
	MELON_BLOCK(Material.MELON_BLOCK, 0, "Melon", Rarity.NORMAL),
	VINES(Material.VINE, 0, "Vines", Rarity.NORMAL),
	FENCE_GATE(Material.FENCE_GATE, 0, "Fence Gate", Rarity.NORMAL),
	BRICK_STAIRS(Material.BRICK_STAIRS, 0, "Brick Stairs", Rarity.NORMAL),
	STONE_BRICK_STAIRS(Material.SMOOTH_STAIRS, 0, "Stone Brick Stairs", Rarity.NORMAL),
	MYCELIUM(Material.MYCEL, 0, "Mycelium", Rarity.NORMAL),
	LILLY_PAD(Material.WATER_LILY, 0, "Lily Pad", Rarity.NORMAL),
	NETHER_BRICKS(Material.NETHER_BRICK, 0, "Nether Bricks", Rarity.NORMAL),
	NETHER_BRICK_FENCE(Material.NETHER_FENCE, 0, "Nether Brick Fence", Rarity.NORMAL),
	NETHER_BRICK_STAIRS(Material.NETHER_BRICK_STAIRS, 0, "Nether Brick Stairs", Rarity.NORMAL),
	ENCHANTMENT_TABLE(Material.ENCHANTMENT_TABLE, 0, "Enchantment Table", Rarity.UNCOMMON),
	END_PORTAL_FRAME(Material.ENDER_PORTAL_FRAME, 0, "End Portal Frame", Rarity.EPIC),
	END_STONE(Material.ENDER_STONE, 0, "End Stone", Rarity.NORMAL),
	DRAGON_EGG(Material.DRAGON_EGG, 0, "Dragon Egg", Rarity.NORMAL),
	REDSTONE_LAMP_ON(Material.REDSTONE_LAMP_ON, 0, "Redstone Lamp", Rarity.NORMAL),
	REDSTONE_LAMP_OFF(Material.REDSTONE_LAMP_OFF, 0, "Redstone Lamp", Rarity.NORMAL),
	WOOD_DOUBLE_SLAB(Material.WOOD_DOUBLE_STEP, 0, "Double Slab", Rarity.NORMAL),
	OAK_WOOD_SLAB(Material.WOOD_STEP, 0, "Oak Wood Slab", Rarity.NORMAL),
	SPRUCE_WOoD_SLAB(Material.WOOD_STEP, 1, "Spruce Wood Slab", Rarity.NORMAL),
	BIRCH_WOOD_SLAB(Material.WOOD_STEP, 2, "Birch Wood Slab", Rarity.NORMAL),
	JUNGLE_WOOD_SLAB(Material.WOOD_STEP, 3, "Jungle Wood Slab", Rarity.NORMAL),
	SANDSTONE_STAIRS(Material.SANDSTONE_STAIRS, 0, "Sandstone Stairs", Rarity.NORMAL),
	EMERALD_ORE(Material.EMERALD_ORE, 0, "Emerald Ore", Rarity.UNCOMMON),
	ENDER_CHEST(Material.ENDER_CHEST, 0, "Ender Chest", Rarity.RARE),
	TRIPEWIRE_HOOK(Material.TRIPWIRE_HOOK, 0, "Tripwire Hook", Rarity.NORMAL),
	EMERALD_BLOCK(Material.EMERALD_BLOCK, 0, "Emerald Block", Rarity.RARE),
	SPRUCE_WOOD_STAIRS(Material.SPRUCE_WOOD_STAIRS, 0, "Spruce Wood Stairs", Rarity.NORMAL),
	BIRCH_WOOD_STAIRS(Material.BIRCH_WOOD_STAIRS, 0, "Birch Wood Stairs", Rarity.NORMAL),
	JUNGLE_WOOD_STAIRS(Material.JUNGLE_WOOD_STAIRS, 0, "Jungle Wood Stairs", Rarity.NORMAL),
	BEACON(Material.BEACON, 0, "Beacon", Rarity.RARE),
	COBBLESTONE_WALL(Material.COBBLE_WALL, 0, "Cobblestone Wall", Rarity.NORMAL),
	MOSSY_COBBLESTONE_WALL(Material.COBBLE_WALL, 1, "Mossy Cobblestone Wall", Rarity.NORMAL),
	WOOD_BUTTON(Material.WOOD_BUTTON, 0, "Wood Button", Rarity.NORMAL),
	ANVIL(Material.ANVIL, 0, "Anvil", Rarity.UNCOMMON),
	TRAPPED_CHEST(Material.TRAPPED_CHEST, 0, "Trapped Chest", Rarity.NORMAL),
	GOLD_WEIGHTED_PRESSURE_PLATE(Material.GOLD_PLATE, 0, "Weighted Pressure Plate", Rarity.NORMAL),
	IRON_WEIGHTED_PRESUSRE_PLATE(Material.IRON_PLATE, 0, "Weighted Pressure Plate", Rarity.NORMAL),
	DAYLIGHT_SENSOR(Material.DAYLIGHT_DETECTOR, 0, "Daylight Sensor", Rarity.NORMAL),
	REDSTONE_BLOCK(Material.REDSTONE_BLOCK, 0, "Redstone Block", Rarity.NORMAL),
	NETHER_QUARTZ_ORE(Material.QUARTZ_ORE, 0, "Nether Quartz Ore", Rarity.NORMAL),
	HOPPER(Material.HOPPER, 0, "Hopper", Rarity.NORMAL),
	SMOOTH_QUARTZ(Material.QUARTZ_BLOCK, 0, "Quartz Block", Rarity.NORMAL),
	CHISELED_QUARTZ(Material.QUARTZ_BLOCK, 1, "Chiseled Quartz Block", Rarity.NORMAL),
	PILLAR_QUARTZ(Material.QUARTZ_BLOCK, 2, "Quartz Pillar", Rarity.NORMAL),
	QUARTZ_STAIRS(Material.QUARTZ_STAIRS, 0, "Quartz Stairs", Rarity.NORMAL),
	ACTIVATOR_RAIL(Material.ACTIVATOR_RAIL, 0, "Activator Rail", Rarity.NORMAL),
	DROPPER(Material.DROPPER, 0, "Dropper", Rarity.NORMAL),
	
	/* Items */
	IRON_SHOVEL(Material.IRON_SPADE, 0, "Iron Shovel", Rarity.NORMAL),
	IRON_PICKAXE(Material.IRON_PICKAXE, 0, "Iron Pickaxe", Rarity.NORMAL),
	IRON_AXE(Material.IRON_AXE, 0, "Iron Axe", Rarity.NORMAL),
	FLINT_AND_STELL(Material.FLINT_AND_STEEL, 0, "Flint and Steel", Rarity.NORMAL),
	APPLE(Material.APPLE, 0, "Apple", Rarity.NORMAL),
	BOW(Material.BOW, 0, "Bow", Rarity.NORMAL),
	ARROW(Material.ARROW, 0, "Arrow", Rarity.NORMAL),
	COAL(Material.COAL, 0, "Coal", Rarity.NORMAL),
	CHARCOAL(Material.COAL, 1, "Charcoal", Rarity.NORMAL),
	DIAMOND(Material.DIAMOND, 0, "Diamond", Rarity.UNCOMMON),
	IRON_INGOT(Material.IRON_INGOT, 0, "Iron Ingot", Rarity.NORMAL),
	GOLD_INGOT(Material.GOLD_INGOT, 0, "", Rarity.NORMAL),
	IRON_SWORD(Material.IRON_SWORD, 0, "Iron Sword", Rarity.NORMAL),
	WOOD_SWORD(Material.WOOD_SWORD, 0, "Wood Sword", Rarity.NORMAL),
	WOOD_SHOVEl(Material.WOOD_SPADE, 0, "Wood Shovel", Rarity.NORMAL),
	WOOD_PICKAXE(Material.WOOD_PICKAXE, 0, "Wood Pickaxe", Rarity.NORMAL),
	WOOD_AXE(Material.WOOD_AXE, 0, "Wood Axe", Rarity.NORMAL),
	DIAMOND_SWORD(Material.DIAMOND_SWORD, 0, "Diamond Sword", Rarity.UNCOMMON),
	DIAMOND_SHOVEL(Material.DIAMOND_SPADE, 0, "Diamond Shovel", Rarity.UNCOMMON),
	DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE, 0, "Diamond Pickaxe", Rarity.UNCOMMON),
	DIAMOND_AXE(Material.DIAMOND_AXE, 0, "Diamond Axe", Rarity.UNCOMMON),
	STICK(Material.STICK, 0, "Stick", Rarity.POOR),
	BOWL(Material.BOWL, 0, "Bowl", Rarity.POOR),
	SOUP_BOWL(Material.MUSHROOM_SOUP, 0, "Mushroom Soup", Rarity.NORMAL),
	GOLD_SWORD(Material.GOLD_SWORD, 0, "Gold Sword", Rarity.NORMAL),
	GOLD_SHOVEL(Material.GOLD_SPADE, 0, "Gold Shovel", Rarity.NORMAL),
	GOLD_PICKAXE(Material.GOLD_PICKAXE, 0, "Gold Pickaxe", Rarity.NORMAL),
	GOLD_AXE(Material.GOLD_AXE, 0, "Gold Axe", Rarity.NORMAL),
	STRING(Material.STRING, 0, "String", Rarity.POOR),
	FEATHER(Material.FEATHER, 0, "Feather", Rarity.POOR),
	GUNPOWDER(Material.SULPHUR, 0, "Gunpowder", Rarity.POOR),
	WOOD_HOE(Material.WOOD_HOE, 0, "Wood Hoe", Rarity.NORMAL),
	STONE_HOE(Material.STONE_HOE, 0, "Stone Hoe", Rarity.NORMAL),
	IRON_HOE(Material.IRON_HOE, 0, "Iron Hoe", Rarity.NORMAL),
	DIAMOND_HOE(Material.DIAMOND_HOE, 0, "Diamond Hoe", Rarity.NORMAL),
	GOLD_HOE(Material.GOLD_HOE, 0, "Gold Hoe", Rarity.NORMAL),
	SEEDS(Material.SEEDS, 0, "Seeds", Rarity.NORMAL),
	WHEAT(Material.WHEAT, 0, "Wheat", Rarity.NORMAL),
	BREAD(Material.BREAD, 0, "Bread", Rarity.NORMAL),
	LEATHER_HELMET(Material.LEATHER_HELMET, 0, "Leather Helmet", Rarity.NORMAL),
	LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE, 0, "Leather Chestplate", Rarity.NORMAL),
	LEATHER_PANTS(Material.LEATHER_LEGGINGS, 0, "Leather Pants", Rarity.NORMAL),
	LEATHER_BOOTS(Material.LEATHER_BOOTS, 0, "Leather Boots", Rarity.NORMAL),
	CHAINMAIL_HELMET(Material.CHAINMAIL_HELMET, 0, "Chainmail Helmet", Rarity.NORMAL),
	CHAINMAIL_CHESTPLATE(Material.CHAINMAIL_CHESTPLATE, 0, "Chainmail Chestplate", Rarity.NORMAL),
	CHAINMAIL_LEGGINGS(Material.CHAINMAIL_LEGGINGS, 0, "Chainmail Leggings", Rarity.NORMAL),
	CHAINMAIL_BOOTS(Material.CHAINMAIL_BOOTS, 0, "Chainmail Boots", Rarity.NORMAL),
	IRON_HELEMT(Material.IRON_HELMET, 0, "Iron Helmet", Rarity.NORMAL),
	IRON_CHESTPLATE(Material.IRON_CHESTPLATE, 0, "Iron Chestplate", Rarity.NORMAL),
	IRON_LEGGINGS(Material.IRON_LEGGINGS, 0, "Iron Leggings", Rarity.NORMAL),
	IRON_BOOTS(Material.IRON_BOOTS, 0, "Iron Boots", Rarity.NORMAL),
	DIAMOND_HELMET(Material.DIAMOND_HELMET, 0, "Diamond Helmet", Rarity.UNCOMMON),
	DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE, 0, "Diamond Chestplate", Rarity.UNCOMMON),
	DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS, 0, "Diamond Leggings", Rarity.UNCOMMON),
	DIAMOND_BOOTS(Material.DIAMOND_BOOTS, 0, "Diamond Boots", Rarity.UNCOMMON),
	GOLD_HELMET(Material.GOLD_HELMET, 0, "Gold Helmet", Rarity.NORMAL),
	GOLD_CHESTPLATE(Material.GOLD_CHESTPLATE, 0, "Gold Chestplate", Rarity.NORMAL),
	GOLD_LEGGINGS(Material.GOLD_LEGGINGS, 0, "Gold Leggings", Rarity.NORMAL),
	GOLD_BOOTS(Material.GOLD_BOOTS, 0, "Gold Boots", Rarity.NORMAL),
	FLINT(Material.FLINT, 0, "Flint", Rarity.POOR),
	RAW_PORK(Material.PORK, 0, "Raw Pork", Rarity.NORMAL),
	COOKED_PORK(Material.GRILLED_PORK, 0, "Cooked Pork", Rarity.NORMAL),
	PAINTING(Material.PAINTING, 0, "Painting", Rarity.NORMAL),
	GOLDEN_APPLE_0(Material.GOLDEN_APPLE, 0, "Golden Apple", Rarity.UNCOMMON),
	GOLDEN_APPLE_1(Material.GOLDEN_APPLE, 1, "Golden Apple", Rarity.RARE),
	SIGN(Material.SIGN, 0, "Sign", Rarity.NORMAL),
	WOOD_DOOR(Material.WOOD_DOOR, 0, "Wood Door", Rarity.NORMAL),
	IRON_DOOR(Material.IRON_DOOR, 0, "Iron Door", Rarity.NORMAL),
	BUCKET(Material.BUCKET, 0, "Bucket", Rarity.NORMAL),
	WATER_BUCKET(Material.WATER_BUCKET, 0, "Water Bucket", Rarity.NORMAL),
	LAVA_BUCKET(Material.LAVA_BUCKET, 0, "Lava Bucket", Rarity.NORMAL),
	MINECART(Material.MINECART, 0, "Minecart", Rarity.NORMAL),
	SADDLE(Material.SADDLE, 0, "Saddle", Rarity.NORMAL),
	MILK_BUCKET(Material.MILK_BUCKET, 0, "Milk Bucket", Rarity.NORMAL),
	BRICK(Material.BRICK, 0, "Brick", Rarity.NORMAL),
	CLAY(Material.CLAY_BALL, 0, "Clay", Rarity.NORMAL),
	SUGAR_CANE(Material.SUGAR_CANE, 0, "Sugar Cane", Rarity.NORMAL),
	PAPER(Material.PAPER, 0, "Paper", Rarity.POOR),
	SLIME_BALL(Material.SLIME_BALL, 0, "Slime Ball", Rarity.NORMAL),
	CHEST_MINECART(Material.STORAGE_MINECART, 0, "Minecart with Chest", Rarity.NORMAL),
	FURNACE_MINECART(Material.POWERED_MINECART, 0, "Minecart with Furnace", Rarity.NORMAL),
	EGG(Material.EGG, 0, "Chicken Egg", Rarity.NORMAL),
	COMPASS(Material.COMPASS, 0, "Compass", Rarity.NORMAL),
	FISHING_ROD(Material.FISHING_ROD, 0, "Fishing Rod", Rarity.NORMAL),
	CLOCK(Material.WATCH, 0, "Clock", Rarity.NORMAL),
	GLOWSTONE_DUST(Material.GLOWSTONE_DUST, 0, "Glowstone Dest", Rarity.NORMAL),
	RAW_FISH(Material.RAW_FISH, 0, "Raw Fish", Rarity.NORMAL),
	COOKED_FISH(Material.COOKED_FISH, 0, "Cooked Fish", Rarity.NORMAL),
	BLACK_DYE(Material.INK_SACK, 0, "Black Dye", Rarity.NORMAL),
	RED_DYE(Material.INK_SACK, 1, "Red Dye", Rarity.NORMAL),
	GREEN_DYE(Material.INK_SACK, 2, "Green Dye", Rarity.NORMAL),
	BROWN_DYE(Material.INK_SACK, 3, "Brown Dye", Rarity.NORMAL),
	BLUE_DYE(Material.INK_SACK, 4, "Blue Dye", Rarity.NORMAL),
	PURPLE_DYE(Material.INK_SACK, 5, "Purple Dye", Rarity.NORMAL),
	CYAN_DYE(Material.INK_SACK, 6, "Cyan Dye", Rarity.NORMAL),
	LIGHT_GRAY_DYE(Material.INK_SACK, 7, "Light Gray Dye", Rarity.NORMAL),
	GRAY_DYE(Material.INK_SACK, 8, "Gray Dye", Rarity.NORMAL),
	PINK_DYE(Material.INK_SACK, 9, "Pink Dye", Rarity.NORMAL),
	LIME_DYE(Material.INK_SACK, 10, "Lime Dye", Rarity.NORMAL),
	YELLOW_DYE(Material.INK_SACK, 11, "Yellow Dye", Rarity.NORMAL),
	LIGHT_BLUE_DYE(Material.INK_SACK, 12, "Light Blue Dye", Rarity.NORMAL),
	MAGENTA_DYE(Material.INK_SACK, 13, "Megenta Dye", Rarity.NORMAL),
	ORANGE_DYE(Material.INK_SACK, 14, "Orange Dye", Rarity.NORMAL),
	WHITE_DYE(Material.INK_SACK, 15, "White Dye", Rarity.NORMAL),
	BONE(Material.BONE, 0, "Bone", Rarity.POOR),
	Sugar(Material.SUGAR, 0, "Sugar", Rarity.NORMAL),
	CAKE(Material.CAKE, 0, "Cake", Rarity.NORMAL),
	BED(Material.BED, 0, "Bed", Rarity.NORMAL),
	REPEATER(Material.DIODE, 0, "Redstone Repeater", Rarity.NORMAL),
	COOKIE(Material.COOKIE, 0, "Cookie", Rarity.NORMAL),
	MAP(Material.MAP, 0, "Map", Rarity.NORMAL),
	SHEARS(Material.SHEARS, 0, "Shears", Rarity.NORMAL),
	MELON(Material.MELON, 0, "Melon", Rarity.NORMAL),
	MELON_SEEDS(Material.MELON_SEEDS, 0, "Melon Seeds", Rarity.NORMAL),
	PUMPKIN_SEEDS(Material.PUMPKIN_SEEDS, 0, "Pumpkin Seeds", Rarity.NORMAL),
	RAW_BEEF(Material.RAW_BEEF, 0, "Raw Beef", Rarity.NORMAL),
	COOKED_BEEF(Material.COOKED_BEEF, 0, "Cooked Beef", Rarity.NORMAL),
	RAW_CHICKEN(Material.RAW_CHICKEN, 0, "Raw Chicken", Rarity.NORMAL),
	COOKED_CHICKEN(Material.COOKED_CHICKEN, 0, "Cooked Chicken", Rarity.NORMAL),
	ROTTEN_FLESH(Material.ROTTEN_FLESH, 0, "Rotten Flesh", Rarity.POOR),
	ENDER_PEARL(Material.ENDER_PEARL, 0, "Ender Pearl", Rarity.UNCOMMON),
	BLAZE_ROD(Material.BLAZE_ROD, 0, "Blaze Rod", Rarity.UNCOMMON),
	GHAST_TEAR(Material.GHAST_TEAR, 0, "Ghast Tear", Rarity.UNCOMMON),
	GOLD_NUGGET(Material.GOLD_NUGGET, 0, "Gold Nugget", Rarity.NORMAL),
	NETHER_WARTS(Material.NETHER_WARTS, 0, "Nether Wars", Rarity.NORMAL),
	POTION_BOTTLE(Material.POTION, 0, "Potion Bottle", Rarity.NORMAL),
	GLASS_BOTTLE(Material.GLASS_BOTTLE, 0, "Glass Bottle", Rarity.NORMAL),
	SPIDER_EYE(Material.SPIDER_EYE, 0, "Spider Eye", Rarity.NORMAL),
	FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, 0, "Fermented Spider Eye", Rarity.NORMAL),
	BLAZE_POWDER(Material.BLAZE_POWDER, 0, "Blaze Powder", Rarity.NORMAL),
	BREWING_STAND(Material.BREWING_STAND_ITEM, 0, "Brewing Stand", Rarity.UNCOMMON),
	CAULDRON(Material.CAULDRON_ITEM, 0, "Cauldron", Rarity.NORMAL),
	EYE_OF_ENDER(Material.EYE_OF_ENDER, 0, "Eye of Ender", Rarity.NORMAL),
	GLISTERING_MELON(Material.SPECKLED_MELON, 0, "Glistering Melon", Rarity.NORMAL),
	SPAWN_EGG(Material.MONSTER_EGG, 0, "Monster Egg", Rarity.RARE),
	
	CREEPER_SPAWN_EGG(Material.MONSTER_EGG, 50, "Creeper Egg", Rarity.RARE),
	SKELETON_SPAWN_EGG(Material.MONSTER_EGG, 51, "Skeleton Egg", Rarity.RARE),
	SPIDER_SPAWN_EGG(Material.MONSTER_EGG, 52, "Spider Egg", Rarity.RARE),
	ZOMBIE_SPAWN_EGG(Material.MONSTER_EGG, 54, "Zombie Egg", Rarity.RARE),
	SLIME_SPAWN_EGG(Material.MONSTER_EGG, 55, "Slime Egg", Rarity.RARE),
	GHAST_SPAWN_EGG(Material.MONSTER_EGG, 56, "Creeper Egg", Rarity.RARE),
	ZOMBIE_PIGMAN_SPAWN_EGG(Material.MONSTER_EGG, 57, "Creeper Egg", Rarity.RARE),
	ENDERMAN_SPAWN_EGG(Material.MONSTER_EGG, 58, "Creeper Egg", Rarity.RARE),
	CAVE_SPIDER_SPAWN_EGG(Material.MONSTER_EGG, 59, "Creeper Egg", Rarity.RARE),
	SILVERFISH_SPAWN_EGG(Material.MONSTER_EGG, 60, "Creeper Egg", Rarity.RARE),
	BLAZE_SPAWN_EGG(Material.MONSTER_EGG, 61, "Creeper Egg", Rarity.RARE),
	MAGMA_CUBE_SPAWN_EGG(Material.MONSTER_EGG, 62, "Creeper Egg", Rarity.RARE),
	BAT_SPAWN_EGG(Material.MONSTER_EGG, 65, "Creeper Egg", Rarity.RARE),
	WITCH_SPAWN_EGG(Material.MONSTER_EGG, 66, "Creeper Egg", Rarity.RARE),
	PIG_SPAWN_EGG(Material.MONSTER_EGG, 90, "Creeper Egg", Rarity.RARE),
	SHEEP_SPAWN_EGG(Material.MONSTER_EGG, 91, "Creeper Egg", Rarity.RARE),
	COW_SPAWN_EGG(Material.MONSTER_EGG, 92, "Creeper Egg", Rarity.RARE),
	CHICKEN_SPAWN_EGG(Material.MONSTER_EGG, 93, "Creeper Egg", Rarity.RARE),
	SQUID_SPAWN_EGG(Material.MONSTER_EGG, 94, "Creeper Egg", Rarity.RARE),
	WOLF_SPAWN_EGG(Material.MONSTER_EGG, 95, "Creeper Egg", Rarity.RARE),
	MOOSHROOM_SPAWN_EGG(Material.MONSTER_EGG, 96, "Creeper Egg", Rarity.RARE),
	OCELOT_SPAWN_EGG(Material.MONSTER_EGG, 98, "Creeper Egg", Rarity.RARE),
	VILLAGER_SPAWN_EGG(Material.MONSTER_EGG, 120, "Creeper Egg", Rarity.RARE),
	EXP_BOTTLE(Material.EXP_BOTTLE, 0, "Bottle o' Enchanting", Rarity.RARE),
	FIRE_CHARGE(Material.FIREBALL, 0, "Fire Charge", Rarity.NORMAL),
	BOOK_AND_QUIL(Material.BOOK_AND_QUILL, 0, "Book and Quil", Rarity.NORMAL),
	WRITTEN_BOOK(Material.WRITTEN_BOOK, 0, "Written Book", Rarity.NORMAL),
	EMERALD(Material.EMERALD, 0, "Emerald", Rarity.RARE),
	ITEM_FRAME(Material.ITEM_FRAME, 0, "Item Frame", Rarity.NORMAL),
	FLOWER_POT(Material.FLOWER_POT_ITEM, 0, "Flower Pot", Rarity.POOR),
	CARROT(Material.CARROT_ITEM, 0, "Carrot", Rarity.NORMAL),
	POTATO(Material.POTATO_ITEM, 0, "Potato", Rarity.NORMAL),
	BAKED_POTATO(Material.BAKED_POTATO, 0, "Baked Potato", Rarity.NORMAL),
	POISON_POTATO(Material.POISONOUS_POTATO, 0, "Poisonous Potato", Rarity.NORMAL),
	EMPTY_MAP(Material.EMPTY_MAP, 0, "Empty Map", Rarity.NORMAL),
	GOLDEN_CARROT(Material.GOLDEN_CARROT, 0, "Golden Carrot", Rarity.NORMAL),
	SKELETON_HEAD(Material.SKULL_ITEM, 0, "Skeleton Skull", Rarity.UNCOMMON),
	WITHER_SKELETON_HEAD(Material.SKULL_ITEM, 0, "Wither Skeleton Head", Rarity.RARE),
	ZOMBIE_HEAD(Material.SKULL_ITEM, 0, "Zombie Head", Rarity.UNCOMMON),
	PLAYER_HEAD(Material.SKULL_ITEM, 0, "Head", Rarity.NORMAL),
	CREEPER_HEAD(Material.SKULL_ITEM, 0, "Creeper Head", Rarity.UNCOMMON),
	CARROT_STICK(Material.CARROT_STICK, 0, "Carrot on a Stick", Rarity.NORMAL),
	NETHER_STAR(Material.NETHER_STAR, 0, "Nether Star", Rarity.RARE),
	PUMPKIN_PIE(Material.PUMPKIN_PIE, 0, "Pumpkin Pie", Rarity.NORMAL),
	FIREWORK_ROCKET(Material.FIREWORK, 0, "Firework Rocket", Rarity.NORMAL),
	FIREWORK_STAR(Material.FIREWORK_CHARGE, 0, "Firework Star", Rarity.NORMAL),
	ENCHANTED_BOOK(Material.ENCHANTED_BOOK, 0, "Enchanted Book", Rarity.UNCOMMON),
	REDSTONE_COMPARATOR(Material.REDSTONE_COMPARATOR, 0, "Redstone Comparator", Rarity.NORMAL),
	NETHER_BRICK(Material.NETHER_BRICK_ITEM, 0, "Nether Brick", Rarity.NORMAL),
	NETHER_QUARTZ(Material.QUARTZ, 0, "Quartz", Rarity.NORMAL),
	TNT_MINECART(Material.EXPLOSIVE_MINECART, 0, "Minecart with TNT", Rarity.NORMAL),
	HOPPER_MINECART(Material.HOPPER_MINECART, 0, "Minecart with Hopper", Rarity.NORMAL),
	MUSIC_DISC_1(Material.GOLD_RECORD, 2256, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_2(Material.GREEN_RECORD, 2257, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_3(Material.RECORD_3, 2258, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_4(Material.RECORD_4, 2259, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_5(Material.RECORD_5, 2260, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_6(Material.RECORD_6, 2261, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_7(Material.RECORD_7, 2262, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_8(Material.RECORD_8, 2263, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_9(Material.RECORD_9, 2264, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_10(Material.RECORD_10, 2265, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_11(Material.RECORD_11, 2266, "Music Disc", Rarity.UNCOMMON),
	MUSIC_DISC_12(Material.RECORD_12, 2267, "Music Disc", Rarity.UNCOMMON);
	
	private static final ArrayList<ItemData> itemdata = new ArrayList<ItemData>();
	
	private final Material material;
	private final int damage;
	private final String name;
	private final Rarity rarity;
	
	static {
        for (ItemData data : EnumSet.allOf(ItemData.class)) {
        	itemdata.add(data);
        }
    }
	
	private ItemData(Material material, int damage, String name, Rarity rarity) {
		this.material = material;
		this.damage = damage;
		this.name = name;
		this.rarity = rarity;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public String getName() {
		return name;
	}
	
	public Rarity getRarity() {
		return rarity;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public static ItemData getBlockData(Material material, int damage){
		for (ItemData data : itemdata){
			if (data.getMaterial() == material && data.getDamage() == damage){
				return data;
			}
		}
		for (ItemData data : itemdata){
			if (data.getMaterial() == material && data.getDamage() == 0){
				return data;
			}
		}
		return null;
	}
	
	public static String getName(Material material, int damage){
		ItemData data = getBlockData(material, damage);
		if (data != null)
			return data.getName();
		else
			return null;
	}
	
	public static Rarity getRarity(Material material, int damage){
		ItemData data = getBlockData(material, damage);
		if (data != null)
			return data.getRarity();
		else
			return null;
	}
	
	public static String getLocalizedName(ItemStack item){
		ItemMeta im = item.getItemMeta();
		if (im.getDisplayName() != null)
			return im.getDisplayName();
		else if (getName(item.getType(), item.getDurability()) != null)
			return getName(item.getType(), item.getDurability());
		else
			return "General Item";
	}
	
	public static ItemStack setLocalizedName(ItemStack item){
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(getLocalizedName(item));
		item.setItemMeta(im);
		return item;
	}
}
