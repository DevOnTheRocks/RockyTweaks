package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import com.blamejared.mtlib.helpers.InputHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import crafttweaker.api.item.IItemStack;
import java.util.List;
import net.minecraft.item.ItemStack;

public class AnvilRecipeGroup {

  private IItemStack left;
  private List<AnvilRecipe> recipes = Lists.newArrayList();

  public AnvilRecipeGroup(IItemStack left, IItemStack[] right, IItemStack[] output, int[] cost) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    Preconditions.checkNotNull(output);
    Preconditions.checkNotNull(cost);
    Preconditions.checkArgument(right.length == output.length && right.length == cost.length);
    this.left = left;
    for (int i = 0; i < right.length; i++) {
      // this.recipes.add(new AnvilRecipe(left, right[i], output[i], cost[i]));
    }
  }

  public ItemStack getLeft() {
    return InputHelper.toStack(left);
  }

  public List<AnvilRecipe> getRecipes() {
    return recipes;
  }
}
