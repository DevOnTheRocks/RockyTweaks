package rocks.gameonthe.rockytweaks.jei;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import rocks.gameonthe.rockytweaks.RockyTweaks;
import rocks.gameonthe.rockytweaks.crafttweaker.anvil.AnvilRecipe;
import rocks.gameonthe.rockytweaks.crafttweaker.anvil.AnvilRecipeHandler;

@JEIPlugin
public class RockyTweaksJEIPlugin implements IModPlugin {

  private IModRegistry registry;

  @Override
  public void register(@Nonnull IModRegistry modRegistry) {
    registry = modRegistry;
    registry.addRecipes(getRecipeWrappers(AnvilRecipeHandler.getRecipes()), VanillaRecipeCategoryUid.ANVIL);
    RockyTweaks.logger.info(String.format("Registered %d anvil recipes with JEI.", AnvilRecipeHandler.getRecipes().size()));
  }

  private IVanillaRecipeFactory getRecipeFactory() {
    return registry.getJeiHelpers().getVanillaRecipeFactory();
  }

  private List<IRecipeWrapper> getRecipeWrappers(List<AnvilRecipe> recipes) {
    List<IRecipeWrapper> wrapperList = Lists.newArrayList();
    for (AnvilRecipe recipe : recipes) {
      if (recipe.isValid()) {
        wrapperList.add(
            getRecipeFactory().createAnvilRecipe(
                recipe.getLeft(),
                Collections.singletonList(recipe.getRight()),
                Collections.singletonList(recipe.getOutput())
            ));
      }
    }
    return wrapperList;
  }
}
