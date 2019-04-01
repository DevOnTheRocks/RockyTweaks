package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.rockytweaks.Anvil")
@ZenRegister
public class AnvilRecipeHandler {

  protected static final String name = "Anvil";
  private static final List<AnvilRecipe> recipes = Lists.newArrayList();
  private static final List<AnvilRecipeGroup> groups = Lists.newArrayList();
  private static final List<AnvilRestriction> blacklist = Lists.newArrayList();

  static {
    new AnvilListener();
  }

  @ZenMethod
  public static void addRecipe(IItemStack left, IItemStack right, IItemStack output, int cost) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    Preconditions.checkNotNull(output);
    Preconditions.checkArgument(cost > 0);
    CraftTweakerAPI.apply(new Add(new AnvilRecipe(toStack(left), toStack(right), toStack(output), cost)));
  }

  private static class Add extends BaseListAddition<AnvilRecipe> {

    public Add(AnvilRecipe recipe) {
      super(AnvilRecipeHandler.name, AnvilRecipeHandler.recipes);
      this.recipes.add(recipe);
    }

    @Override
    public void apply() {
      if (!this.recipes.isEmpty()) {
        for (AnvilRecipe recipe : this.recipes) {
          if (recipe != null) {
            if (AnvilRecipeHandler.recipes.add(recipe)) {
              this.successful.add(recipe);
              recipe.setValid(true);
            } else {
              LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(recipe)));
            }
          } else {
            LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
          }
        }
      }
    }

    @Override
    public String getRecipeInfo(AnvilRecipe recipe) {
      return LogHelper.getStackDescription(recipe.getOutput());
    }
  }

  @ZenMethod
  public static void addRecipes(IItemStack left, IItemStack[] right, IItemStack[] output, int[] cost) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    Preconditions.checkNotNull(output);
    Preconditions.checkArgument(right.length == output.length);
    Preconditions.checkArgument(Arrays.stream(cost).allMatch(i -> i > 0));
    CraftTweakerAPI.apply(new AddGroup(new AnvilRecipeGroup(toStack(left), toStacks(right), toStacks(output), cost)));
  }

  private static class AddGroup extends BaseListAddition<AnvilRecipeGroup> {

    public AddGroup(AnvilRecipeGroup group) {
      super(AnvilRecipeHandler.name, AnvilRecipeHandler.groups);
      this.recipes.add(group);
    }

    @Override
    public void apply() {
      if (!this.recipes.isEmpty()) {
        for (AnvilRecipeGroup group : this.recipes) {
          if (group != null) {
            if (AnvilRecipeHandler.recipes.addAll(group.getRecipes())) {
              this.successful.add(group);
            } else {
              LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(group)));
            }
          } else {
            LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
          }
        }
      }
    }

    @Override
    public String getRecipeInfo(AnvilRecipeGroup group) {
      return LogHelper.getStackDescription(group.getLeft());
    }
  }

  @ZenMethod
  public static void remove(IIngredient[] input) {
    Preconditions.checkNotNull(input);
    Preconditions.checkArgument(input.length >= 1 && input.length <= 2);
    CraftTweakerAPI.apply(new Remove(new AnvilRestriction(input)));
  }

  @ZenMethod
  public static void remove(IIngredient output) {
    Preconditions.checkNotNull(output);
    CraftTweakerAPI.apply(new Remove(new AnvilRestriction(output)));
  }

  private static class Remove extends BaseListRemoval<AnvilRestriction> {

    public Remove(AnvilRestriction restriction) {
      super(AnvilRecipeHandler.name, AnvilRecipeHandler.blacklist);
      this.recipes.add(restriction);
    }

    @Override
    public void apply() {
      if (!this.recipes.isEmpty()) {
        for (AnvilRestriction restriction : this.recipes) {
          if (restriction != null) {
            if (AnvilRecipeHandler.blacklist.add(restriction)) {
              this.successful.add(restriction);
            } else {
              LogHelper.logError(String.format("Error blacklisting anvil input %s", this.getRecipeInfo(restriction)));
            }
          } else {
            LogHelper.logError("Error adding anvil input: null object");
          }
        }
      }
    }

    @Override
    public String getRecipeInfo(AnvilRestriction restriction) {
      return LogHelper.getStackDescription(restriction);
    }
  }

  private static List<ItemStack> toStacks(IItemStack[] iStacks) {
    if (iStacks == null) {
      return null;
    }
    List<ItemStack> stacks = Lists.newArrayList();
    for (IItemStack stack : iStacks) {
      stacks.add(toStack(stack));
    }
    return stacks;
  }

  public static List<AnvilRecipe> getRecipes() {
    return recipes;
  }

  public static List<AnvilRestriction> getBlacklist() {
    return blacklist;
  }
}
