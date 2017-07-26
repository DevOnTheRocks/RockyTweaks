package rocks.devonthe.rockycore.crafttweaker.anvil;

import net.minecraft.item.ItemStack;

public class AnvilRecipe {

	private ItemStack left;
	private ItemStack right;
	private ItemStack output;
	private int cost;
	private boolean valid = false;

	public AnvilRecipe(ItemStack left, ItemStack right, ItemStack output, int cost) {
		this.left = left;
		this.right = right;
		this.output = output;
		this.cost = cost;
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

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}
}
