package rocks.devonthe.rockycore.crafttweaker.anvil;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import rocks.devonthe.rockycore.RockyCore;
import rocks.devonthe.rockycore.jei.RockyCoreJEIPlugin;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;

@ZenClass("mods.rockycore.Anvil")
public class AnvilRecipeHandler {

	protected static final String name = "Anvil";
	private static ArrayList<AnvilRecipe> recipes = Lists.newArrayList();

	static {
		new AnvilListener();
	}

	@ZenMethod
	public static void addRecipe(IItemStack left, IItemStack right, IItemStack output, int cost) {
		MineTweakerAPI.apply(new Add(new AnvilRecipe(toStack(left), toStack(right), toStack(output), cost)));
	}

	private static class Add extends BaseListAddition<AnvilRecipe> {

		public Add(AnvilRecipe recipe) {
			super(AnvilRecipeHandler.name, AnvilRecipeHandler.recipes);
			this.recipes.add(recipe);
		}

		@Override public void apply() {
			if (!this.recipes.isEmpty()) {
				for (AnvilRecipe recipe : this.recipes) {
					if (recipe != null) {
						if (AnvilRecipeHandler.recipes.add(recipe)) {
							this.successful.add(recipe);
							RockyCoreJEIPlugin.addAnvilRecipe(recipe);
						} else {
							LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(recipe)));
						}
					} else {
						LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
					}
				}
			}
		}

		@Override public void undo() {
			if (!this.successful.isEmpty()) {
				for (AnvilRecipe recipe : this.successful) {
					if (recipe != null) {
						if (AnvilRecipeHandler.recipes.remove(recipe)) {
							RockyCoreJEIPlugin.removeAnvilRecipe(recipe);
						} else {
							LogHelper.logError(String.format("Error removing %s Recipe for %s", this.name, this.getRecipeInfo(recipe)));
						}
					} else {
						LogHelper.logError(String.format("Error removing %s Recipe: null object", this.name));
					}
				}
			}
		}

		@Override public String getRecipeInfo(AnvilRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}

/*	@ZenMethod
	public static void remove(IItemStack input) {

		List<AnvilRecipe> recipes = new LinkedList<>();

		if (input == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		for (AnvilRecipe recipe : AnvilRecipeHandler.recipes) {
			if (matches(input, toIItemStack(recipe.getOutput()))) {
				recipes.add(recipe);
			}
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", name, input.toString()));
		}
	}

	private static class Remove extends BaseListRemoval<AnvilRecipe> {

		public Remove(List<AnvilRecipe> recipes) {
			super(AnvilRecipeHandler.name, AnvilRecipeHandler.recipes, recipes);
			recipes.forEach(RockyCoreJEIPlugin::removeAnvilRecipe);
		}

		@Override public String getRecipeInfo(AnvilRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}*/

	public static ArrayList<AnvilRecipe> getRecipes() {
		return recipes;
	}

	public static boolean addRecipe(AnvilRecipe recipe) {
		return recipes.add(recipe);
	}

	public static boolean removeRecipe(AnvilRecipe recipe) {
		return recipes.remove(recipe);
	}
}
