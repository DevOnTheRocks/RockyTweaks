package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraft.item.ItemStack;

public class AnvilRecipe {

  private IIngredient left;
  private IIngredient right;
  private IItemStack output;
  private IRecipeFunction function;
  private int cost;
  private boolean valid = false;

  public AnvilRecipe(IIngredient left, IIngredient right, IItemStack output, int cost, IRecipeFunction function) {
    this.left = left;
    this.right = right;
    this.output = output;
    this.cost = cost;
    this.function = function;
  }

  public IIngredient getLeft() {
    return left;
  }

  public ItemStack getLeftStack() {
    return InputHelper.toStack(left.getItems().get(0));
  }

  public IIngredient getRight() {
    return right;
  }

  public ItemStack getRightStack() {
    return InputHelper.toStack(right.getItems().get(0));
  }

  public int getRightCount() {
    return right.getAmount();
  }

  public IItemStack getOutput() {
    return output;
  }

  public ItemStack getOutputStack() {
    return InputHelper.toStack(output);
  }

  public IRecipeFunction getFunction() {
    return function;
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
