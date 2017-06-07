package rocks.devonthe.rockycore.crafttweaker.anvil;

import static rocks.devonthe.rockycore.jei.RockyCoreJEIPlugin.getRecipeRegistry;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class AnvilRecipe {

	private ItemStack left;
	private ItemStack right;
	private ItemStack output;
	private int cost;
	private IRecipeWrapper wrapper;

	public AnvilRecipe(ItemStack left, ItemStack right, ItemStack output, int cost) {
		this.left = left;
		this.right = right;
		this.output = output;
		this.cost = cost;
		this.wrapper = (getRecipeRegistry() != null) ? getRecipeRegistry().createAnvilRecipe(left, Collections.singletonList(right), Collections
			.singletonList(output)) : null;
	}

	public ItemStack getLeft() {
		return left.copy();
	}

	public ItemStack getRight() {
		return right.copy();
	}

	public int getRightCount() {
		return right.getCount();
	}

	public ItemStack getOutput() {
		return output.copy();
	}

	public int getCost() {
		return cost;
	}

	public IRecipeWrapper getWrapper() {
		return wrapper;
	}
}
