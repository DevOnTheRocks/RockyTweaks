package rocks.devonthe.rockycore.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;

@JEIPlugin
public class RockyCoreJEIPlugin extends BlankModPlugin {

	private static IRecipeRegistry recipeRegistry;

	@Override public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}

	public static IRecipeRegistry getRecipeRegistry() {
		return recipeRegistry;
	}

	public static void addRecipe(IRecipeWrapper recipe, String recipeCategory) {
		if (recipeRegistry == null) return;
		recipeRegistry.addRecipe(recipe, recipeCategory);
	}

	public static void removeRecipe(IRecipeWrapper recipe, String recipeCategory) {
		if (recipeRegistry == null) return;
		recipeRegistry.removeRecipe(recipe, recipeCategory);
	}
}
