package rocks.gameonthe.rockycore.crafttweaker.anvil;

import static crafttweaker.api.minecraft.CraftTweakerMC.getIItemStack;

import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;

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
        IIngredient i = input[0];

        return i.matches(getIItemStack(left)) || i.matches(getIItemStack(right));
      } else if (this.input.length == 2) {
        IIngredient i1 = input[0];
        IIngredient i2 = input[1];

        return i1.matches(getIItemStack(left)) && i2.matches(getIItemStack(right))
            || i1.matches(getIItemStack(right)) && i2.matches(getIItemStack(left));
      }
    } else if (this.output != null) {
      return this.output.matches(getIItemStack(output));
    }
    return false;
  }
}
