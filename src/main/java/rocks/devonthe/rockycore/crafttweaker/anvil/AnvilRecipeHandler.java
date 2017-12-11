package rocks.devonthe.rockycore.crafttweaker.anvil;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import java.util.List;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.rockycore.Anvil")
@ZenRegister
public class AnvilRecipeHandler {

  protected static final String name = "Anvil";
  private static List<AnvilRecipe> recipes = Lists.newArrayList();
  private static List<AnvilRecipeGroup> groups = Lists.newArrayList();

  static {
    new AnvilListener();
  }

  @ZenMethod
  public static void addRecipe(IItemStack left, IItemStack right, IItemStack output, int cost) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    Preconditions.checkNotNull(output);
    CraftTweakerAPI
        .apply(new Add(new AnvilRecipe(toStack(left), toStack(right), toStack(output), cost)));
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
              LogHelper.logError(String
                  .format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(recipe)));
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
  public static void addRecipes(IItemStack left, IItemStack[] right, IItemStack[] output,
      int[] cost) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    Preconditions.checkNotNull(output);
    Preconditions.checkArgument(right.length == output.length);
    CraftTweakerAPI.apply(
        new AddGroup(new AnvilRecipeGroup(toStack(left), toStacks(right), toStacks(output), cost)));
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
              LogHelper.logError(String
                  .format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(group)));
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

  public static List<AnvilRecipe> getRecipes() {
    return recipes;
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
}
