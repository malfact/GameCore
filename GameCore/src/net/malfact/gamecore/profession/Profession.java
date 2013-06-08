package net.malfact.gamecore.profession;

public class Profession {
	
	public static final Profession mining = new Profession(1, "Mining");
	
	private String name;
	private int id;
	
	public Profession(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
}
