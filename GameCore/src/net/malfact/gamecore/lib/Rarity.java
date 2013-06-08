package net.malfact.gamecore.lib;

public enum Rarity {
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
