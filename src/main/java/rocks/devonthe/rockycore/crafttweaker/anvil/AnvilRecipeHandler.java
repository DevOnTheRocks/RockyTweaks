package rocks.devonthe.rockycore.crafttweaker.anvil;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import rocks.devonthe.rockycore.RockyCore;
import rocks.devonthe.rockycore.jei.RockyCoreJEIPlugin;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.rockycore.Anvil")
public class AnvilRecipeHandler {

	protected static final String name = "Anvil";
	private static ArrayList<AnvilRecipe> recipes = Lists.newArrayList();
	private static ArrayList<AnvilRecipeGroup> groups = Lists.newArrayList();

	static {
		new AnvilListener();
	}

	@ZenMethod
	public static void addRecipe(IItemStack left, IItemStack right, IItemStack output, int cost) {
		Preconditions.checkNotNull(left);
		Preconditions.checkNotNull(right);
		Preconditions.checkNotNull(output);
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
							RockyCoreJEIPlugin.addRecipe(recipe.getWrapper(), VanillaRecipeCategoryUid.ANVIL);
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
							RockyCoreJEIPlugin.removeRecipe(recipe.getWrapper(), VanillaRecipeCategoryUid.ANVIL);
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

	@ZenMethod
	public static void addRecipes(IItemStack left, IItemStack[] right, IItemStack[] output, int[] cost) {
		Preconditions.checkNotNull(left);
		Preconditions.checkNotNull(right);
		Preconditions.checkNotNull(output);
		Preconditions.checkArgument(right.length == output.length);
		MineTweakerAPI.apply(new AddGroup(new AnvilRecipeGroup(toStack(left), toStacks(right), toStacks(output), cost)));
	}

	private static class AddGroup extends BaseListAddition<AnvilRecipeGroup> {

		public AddGroup(AnvilRecipeGroup group) {
			super(AnvilRecipeHandler.name, AnvilRecipeHandler.groups);
			this.recipes.add(group);
		}

		@Override public void apply() {
			if (!this.recipes.isEmpty()) {
				for (AnvilRecipeGroup group : this.recipes) {
					if (group != null) {
						if (AnvilRecipeHandler.recipes.addAll(group.getRecipes())) {
							this.successful.add(group);
							RockyCoreJEIPlugin.addRecipe(group.getWrapper(), VanillaRecipeCategoryUid.ANVIL);
						} else {
							LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(group)));
						}
					} else {
						LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
					}
				}
			}
		}

		@Override public void undo() {
			if (!this.successful.isEmpty()) {
				for (AnvilRecipeGroup group : this.successful) {
					if (group != null) {
						if (AnvilRecipeHandler.recipes.removeAll(group.getRecipes())) {
							RockyCore.logger.info("Removed recipe group: " + group.getLeft());
							RockyCoreJEIPlugin.removeRecipe(group.getWrapper(), VanillaRecipeCategoryUid.ANVIL);
						} else {
							LogHelper.logError(String.format("Error removing %s Recipes for %s", this.name, this.getRecipeInfo(group)));
						}
					} else {
						LogHelper.logError(String.format("Error removing %s Recipes: null object", this.name));
					}
				}
			}
		}

		@Override public String getRecipeInfo(AnvilRecipeGroup group) {
			return LogHelper.getStackDescription(group.getLeft());
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

	private static List<ItemStack> toStacks(IItemStack[] iStacks) {
		if (iStacks == null) return null;
		List<ItemStack> stacks = Lists.newArrayList();
		for (IItemStack stack : iStacks) {
			stacks.add(toStack(stack));
		}
		return stacks;
	}
}
