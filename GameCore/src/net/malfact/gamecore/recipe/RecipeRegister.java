package net.malfact.gamecore.recipe;

import java.util.ArrayList;

public class RecipeRegister {
	
	private static ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	
	public static Recipe getRecipe(String name){
		for (Recipe recipe : recipeList ){
			if (recipe.getName().equalsIgnoreCase(name))
				return recipe;
		}
		return null;
	}
	
	public static void addRecipe(Recipe recipe){
		recipeList.add(recipe);
	}
	
	public static Recipe[] getRecipes(){
		return recipeList.toArray(new Recipe[recipeList.size()]);
	}
}
