package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import static crafttweaker.api.minecraft.CraftTweakerMC.getIngredient;

import crafttweaker.api.item.IIngredient;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class AnvilRestriction {

  private final IIngredient[] input;
  private final IIngredient output;

  public AnvilRestriction(IIngredient[] input) {
    this.input = input;
    this.output = null;
  }

  public AnvilRestriction(IIngredient output) {
    this.input = null;
    this.output = output;
  }

  public boolean isBlacklisted(ItemStack left, ItemStack right, ItemStack output) {
    if (this.input != null) {
      if (this.input.length == 1) {
        Ingredient i = getIngredient(input[0]);

        return i.apply(left) || i.apply(right);
      } else if (this.input.length == 2) {
        Ingredient i1 = getIngredient(input[0]);
        Ingredient i2 = getIngredient(input[1]);

        return i1.apply(left) && i2.apply(right)
            || i1.apply(right) && i2.apply(left);
      }
    } else if (this.output != null) {
      return getIngredient(this.output).apply(output);
    }
    return false;
  }

  public boolean isBlacklisted(ItemStack left, List<ItemStack> right, List<ItemStack> output) {
    for (int i = 0; i < right.size(); i++) {
      if (isBlacklisted(left, right.get(i), right.size() == output.size() ? output.get(i) : output.get(0))) {
        return true;
      }
    }
    return false;
  }
}
