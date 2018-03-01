package rocks.devonthe.rockycore.jei;

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
import rocks.devonthe.rockycore.RockyCore;
import rocks.devonthe.rockycore.crafttweaker.anvil.AnvilRecipe;
import rocks.devonthe.rockycore.crafttweaker.anvil.AnvilRecipeHandler;

@JEIPlugin
public class RockyCoreJEIPlugin implements IModPlugin {

  private IModRegistry registry;

  @Override
  public void register(@Nonnull IModRegistry modRegistry) {
    registry = modRegistry;
    registry.addRecipes(getRecipeWrappers(AnvilRecipeHandler.getRecipes()), VanillaRecipeCategoryUid.ANVIL);
    RockyCore.logger.info(String.format("Registered %d anvil recipes with JEI.", AnvilRecipeHandler.getRecipes().size()));
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
