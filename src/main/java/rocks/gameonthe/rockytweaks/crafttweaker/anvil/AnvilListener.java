package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilListener {

  public AnvilListener() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onAnvilUpdate(AnvilUpdateEvent event) {
    handleAnvilRemovals(event);
    handleAnvilAdditions(event);
  }

  private void handleAnvilAdditions(AnvilUpdateEvent event) {
    AnvilRecipeHandler.getRecipes().stream()
        .filter(recipe -> recipe.isValid()
            && matches(recipe.getLeft(), event.getLeft())
            && greaterThanOrEqual(recipe.getRight(), event.getRight()))
        .max(Comparator.comparing(AnvilRecipe::getRightCount))
        .ifPresent(recipe -> {
          event.setCanceled(false);
          event.setCost(recipe.getCost());
          event.setMaterialCost(recipe.getRightStack().getCount());
          event.setOutput(getAnvilOutput(recipe, event));
        });
  }

  private void handleAnvilRemovals(AnvilUpdateEvent event) {
    if (AnvilRecipeHandler.getBlacklist().stream().anyMatch(r -> r.isBlacklisted(event.getLeft(), event.getRight(), event.getOutput()))) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onAnvilCraft(AnvilRepairEvent event) {
    AnvilRecipeHandler.getRecipes().stream()
        .filter(recipe -> recipe.isValid()
            && matches(recipe.getLeft(), event.getItemInput())
            && greaterThanOrEqual(recipe.getRight(), event.getIngredientInput()))
        .max(Comparator.comparing(AnvilRecipe::getRightCount))
        .ifPresent(recipe -> {
          if (event.getItemInput().getCount() > recipe.getLeftStack().getCount()) {
            ItemStack itemStack = event.getItemInput().copy();
            itemStack.shrink(recipe.getLeftStack().getCount());
            if (!event.getEntityPlayer().inventory.addItemStackToInventory(itemStack)) {
              event.getEntityPlayer().dropItem(itemStack, true, false);
            }
          }
        });
  }

  private boolean matches(IIngredient iItemStack, ItemStack itemStack) {
    return iItemStack.matches(InputHelper.toIItemStack(itemStack));

//    Item item1 = stack1.getItem();
//    return item1.equals(stack2.getItem())
//        && (item1.isDamageable() || stack1.getMetadata() == stack2.getMetadata())
//        && stack1.hasTagCompound() == stack2.hasTagCompound()
//        && (stack1.getTagCompound() == null || stack1.getTagCompound().equals(stack2.getTagCompound()));
  }

  private boolean greaterThanOrEqual(IIngredient iItemStack, ItemStack itemStack) {
    return matches(iItemStack, itemStack) && iItemStack.getAmount() >= itemStack.getCount();
  }

  private ItemStack getAnvilOutput(AnvilRecipe recipe, AnvilUpdateEvent event) {
    if (recipe.getFunction() != null) {
      Map<String, IItemStack> inputs = new HashMap<String, IItemStack>() {{
        put("left", InputHelper.toIItemStack(event.getLeft()));
        put("right", InputHelper.toIItemStack(event.getRight()));
      }};
      IItemStack out = null;
      try {
        out = recipe.getFunction().process(recipe.getOutput(), inputs, null);
      } catch(Exception exception) {
        CraftTweakerAPI.logError("Could not execute RecipeFunction: ", exception);
      }
      return InputHelper.toStack(out);
    }
    return InputHelper.toStack(recipe.getOutput());
  }
}
