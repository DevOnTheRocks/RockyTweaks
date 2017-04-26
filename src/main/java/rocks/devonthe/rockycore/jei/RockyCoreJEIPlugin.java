package rocks.devonthe.rockycore.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class RockyCoreJEIPlugin extends BlankModPlugin {

	private static IRecipeRegistry recipeRegistry;

	@Override public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}

	public static IRecipeRegistry getRecipeRegistry() {
		return recipeRegistry;
	}
}
