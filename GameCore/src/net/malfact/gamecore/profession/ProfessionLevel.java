package net.malfact.gamecore.profession;

public enum ProfessionLevel {
	APPRENTICE(0,75,5,100),
	JOURNEYMAN(50,150,10,500),
	EXPERT(125,225,20,5000),
	ARTISAN(225,300,35,50000);
	
	private final int minimumtraininglevel;
	private final int maximumskilllevel;
	private final int minimumplayerlevel;
	private final double trainingcost;
	
	private ProfessionLevel(int a, int b, int c, double d) {
		minimumtraininglevel = a;
		maximumskilllevel = b;
		minimumplayerlevel = c;
		trainingcost = d;
	}
	
	public int getMinimumtraininglevel() {
		return minimumtraininglevel;
	}
	
	public int getMaximumskilllevel() {
		return maximumskilllevel;
	}
	
	public int getMinimumplayerlevel() {
		return minimumplayerlevel;
	}
	
	public double getTrainingcost() {
		return trainingcost;
	}
}
