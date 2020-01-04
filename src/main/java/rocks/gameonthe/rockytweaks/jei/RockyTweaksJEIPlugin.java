package rocks.gameonthe.rockytweaks.jei;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.google.common.collect.Lists;
import crafttweaker.api.item.IItemStack;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.ingredients.Ingredients;
import net.minecraft.item.ItemStack;
import rocks.gameonthe.rockytweaks.RockyTweaks;
import rocks.gameonthe.rockytweaks.crafttweaker.anvil.AnvilRecipe;
import rocks.gameonthe.rockytweaks.crafttweaker.anvil.AnvilRecipeHandler;

@JEIPlugin
public class RockyTweaksJEIPlugin implements IModPlugin {

  private IModRegistry registry;
  private List<IRecipeWrapper> recipeWrappers;

  @Override
  public void register(@Nonnull IModRegistry modRegistry) {
    registry = modRegistry;
    recipeWrappers = getRecipeWrappers(AnvilRecipeHandler.getRecipes());
    registry.addRecipes(recipeWrappers, VanillaRecipeCategoryUid.ANVIL);
    RockyTweaks.logger.info("Registered {} anvil recipes with JEI.", AnvilRecipeHandler.getRecipes().size());
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    final IRecipeRegistry recipeRegistry = jeiRuntime.getRecipeRegistry();
    final IRecipeCategory<IRecipeWrapper> anvilRecipeCategory = recipeRegistry.getRecipeCategory(VanillaRecipeCategoryUid.ANVIL);

    if (anvilRecipeCategory == null) {
      return;
    }

    if (AnvilRecipeHandler.isRemoveAll()) {
      recipeRegistry.getRecipeWrappers(anvilRecipeCategory).stream()
          .filter(r -> !recipeWrappers.contains(r))
          .forEach(recipeRegistry::hideRecipe);
    } else if (!AnvilRecipeHandler.getBlacklist().isEmpty()) {
      hideBlacklistedRecipes(recipeRegistry, anvilRecipeCategory);
    }
  }

  private IVanillaRecipeFactory getRecipeFactory() {
    return registry.getJeiHelpers().getVanillaRecipeFactory();
  }

  private List<IRecipeWrapper> getRecipeWrappers(List<AnvilRecipe> recipes) {
    List<IRecipeWrapper> wrapperList = Lists.newArrayList();
    for (AnvilRecipe recipe : recipes) {
      if (recipe.isValid()) {
        final List<ItemStack> right = Arrays.asList(InputHelper.toStacks(recipe.getRight().getItemArray()));
        final List<ItemStack> output = Lists.newArrayListWithCapacity(right.size());
        for (int i = 0; i < right.size(); i++) {
          output.add(recipe.getOutputStack());
        }

        for (IItemStack left : recipe.getLeft().getItems()) {
          wrapperList.add(getRecipeFactory().createAnvilRecipe(InputHelper.toStack(left), right, output));
        }
      }
    }
    return wrapperList;
  }

  private void hideBlacklistedRecipes(IRecipeRegistry recipeRegistry,
      IRecipeCategory<IRecipeWrapper> anvilRecipeCategory) {
    recipeRegistry.getRecipeWrappers(anvilRecipeCategory).stream()
        .collect(Collectors.toMap(
            r -> r,
            r -> {
              IIngredients ingredients = new Ingredients();
              r.getIngredients(ingredients);
              return ingredients;
            }
        ))
        .forEach((wrapper, ingredients) -> {
          final List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
          final ItemStack left = inputs.get(0).get(0);
          final List<ItemStack> right = inputs.get(1);
          final List<ItemStack> output = ingredients.getOutputs(ItemStack.class).get(0);
          if (AnvilRecipeHandler.getBlacklist().stream()
              .anyMatch(restriction -> restriction.isBlacklisted(left, right, output))) {
            LogHelper
                .logInfo(String.format("Hiding blacklisted recipe from JEI:%n\tRight: %s%n\tLeft: %s%n\tOutput: %s",
                    LogHelper.getStackDescription(left),
                    LogHelper.getStackDescription(right),
                    LogHelper.getStackDescription(output)
                ));
            recipeRegistry.hideRecipe(wrapper);
          }
        });
  }
}
