package rocks.devonthe.rockycore.crafttweaker.anvil;

import mezz.jei.plugins.vanilla.anvil.AnvilRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class AnvilRecipe {

	private ItemStack left;
	private ItemStack right;
	private ItemStack output;
	private int cost;
	private AnvilRecipeWrapper wrapper;

	public AnvilRecipe(ItemStack left, ItemStack right, ItemStack output, int cost) {
		this.left = left;
		this.right = right;
		this.output = output;
		this.cost = cost;
		this.wrapper = new AnvilRecipeWrapper(getLeft(), Collections.singletonList(getRight()), Collections.singletonList(getOutput()));
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

	public AnvilRecipeWrapper getWrapper() {
		return wrapper;
	}
}
