package rocks.devonthe.rockycore.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import rocks.devonthe.rockycore.crafttweaker.anvil.AnvilRecipe;
import rocks.devonthe.rockycore.crafttweaker.anvil.AnvilRecipeHandler;

import java.util.Collections;

@JEIPlugin
public class RockyCoreJEIPlugin extends BlankModPlugin {

	private static IJeiHelpers jeiHelpers;
	private static IRecipeRegistry recipeRegistry;

	@Override public void register(IModRegistry registry) {
		jeiHelpers = registry.getJeiHelpers();

		AnvilRecipeHandler.getRecipes().forEach(r ->
			registry.addAnvilRecipe(
				r.getLeft(),
				Collections.singletonList(r.getRight()),
				Collections.singletonList(r.getOutput())
			));
	}

	@Override public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}

	public static void addAnvilRecipe(AnvilRecipe recipe) {
		recipeRegistry.addRecipe(recipe.getWrapper());
	}

	public static void removeAnvilRecipe(AnvilRecipe recipe) {
		recipeRegistry.removeRecipe(recipe.getWrapper());
	}
}
