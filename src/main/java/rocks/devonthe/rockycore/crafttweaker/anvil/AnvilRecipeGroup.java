package rocks.devonthe.rockycore.crafttweaker.anvil;

import static rocks.devonthe.rockycore.jei.RockyCoreJEIPlugin.getRecipeRegistry;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AnvilRecipeGroup {

	private ItemStack left;
	private List<AnvilRecipe> recipes = Lists.newArrayList();
	private IRecipeWrapper wrapper;

	public AnvilRecipeGroup(ItemStack left, List<ItemStack> right, List<ItemStack> output, int[] cost) {
		Preconditions.checkNotNull(left);
		Preconditions.checkNotNull(right);
		Preconditions.checkNotNull(output);
		Preconditions.checkNotNull(cost);
		Preconditions.checkArgument(right.size() == output.size() && right.size() == cost.length);
		this.left = left;
		for (int i = 0; i < right.size(); i++) {
			this.recipes.add(new AnvilRecipe(left, right.get(i), output.get(i), cost[i]));
		}
		this.wrapper = getRecipeRegistry().createAnvilRecipe(left, right, output);
	}

	public ItemStack getLeft() {
		return left;
	}

	public List<AnvilRecipe> getRecipes() {
		return recipes;
	}

	public IRecipeWrapper getWrapper() {
		return wrapper;
	}
}
