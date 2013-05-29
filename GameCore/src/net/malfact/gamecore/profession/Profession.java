package net.malfact.gamecore.profession;

import java.util.ArrayList;

import net.malfact.gamecore.recipe.Recipe;

public class Profession {
	
	public ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	public void addRecipe(Recipe recipe){
		recipes.add(recipe);
	}
	
	public Recipe getRecipe(String name){
		for (Recipe recipe : recipes){
			if (recipe.getName().equalsIgnoreCase(name))
				return recipe;
		}
		return null;
	}
}
