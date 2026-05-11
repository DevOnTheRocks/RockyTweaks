package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
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
    AnvilRecipe recipe = AnvilRecipeHandler.getRecipes().stream()
        .filter(r -> r.isValid()
            && matches(r.getLeft(), event.getLeft())
            && greaterThanOrEqual(r.getRight(), event.getRight()))
        .max(Comparator.comparing(AnvilRecipe::getRightCount))
        .orElse(null);
    if (recipe != null) {
      event.setCanceled(false);
      event.setCost(recipe.getCost());
      event.setMaterialCost(recipe.getRightStack().getCount());
      event.setOutput(getAnvilOutput(recipe, event));
    } else if (AnvilRecipeHandler.isRemoveAll()) {
      event.setCanceled(true);
    }
  }

  private void handleAnvilRemovals(AnvilUpdateEvent event) {
    if (AnvilRecipeHandler.getBlacklist().stream()
        .anyMatch(r -> r.isBlacklisted(event.getLeft(), event.getRight(), event.getOutput()))) {
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
          if (event.getItemInput().getCount() > recipe.getLeft().getAmount()) {
            ItemStack itemStack = event.getItemInput().copy();
            itemStack.shrink(recipe.getLeft().getAmount());
            addToPlayerInventoryOrDrop(event.getEntityPlayer(), itemStack);
          }
          processInputTransform(event.getEntityPlayer(), event.getItemInput(), recipe.getLeft());
          processInputTransform(event.getEntityPlayer(), event.getIngredientInput(), recipe.getRight());
        });
  }

  private void processInputTransform(EntityPlayer player, ItemStack actual, IIngredient recipe) {
    if (recipe.hasNewTransformers()) {
      IItemStack transform = recipe.applyNewTransform(InputHelper.toIItemStack(actual));
      addToPlayerInventoryOrDrop(player, transform);
    }
  }

  private boolean matches(IIngredient iItemStack, ItemStack itemStack) {
    return iItemStack.matches(InputHelper.toIItemStack(itemStack));
  }

  private boolean greaterThanOrEqual(IIngredient iItemStack, ItemStack itemStack) {
    return matches(iItemStack.amount(itemStack.getCount()), itemStack) && itemStack.getCount() >= iItemStack.getAmount();
  }

  private ItemStack getAnvilOutput(AnvilRecipe recipe, AnvilUpdateEvent event) {
    if (recipe.getFunction() != null) {
      Map<String, IItemStack> inputs = new HashMap<>();
      inputs.put("left", InputHelper.toIItemStack(event.getLeft()));
      inputs.put("right", InputHelper.toIItemStack(event.getRight()));
      IItemStack out = null;

      try {
        out = recipe.getFunction().process(recipe.getOutput(), inputs, null);
      } catch (Exception exception) {
        CraftTweakerAPI.logError("Could not execute RecipeFunction: ", exception);
      }
      return InputHelper.toStack(out).copy();
    }
    return InputHelper.toStack(recipe.getOutput()).copy();
  }

  private void addToPlayerInventoryOrDrop(EntityPlayer player, ItemStack itemStack) {
    if (itemStack.isEmpty()) {
      return;
    }

    itemStack = itemStack.copy();
    if (!player.inventory.addItemStackToInventory(itemStack)) {
      player.dropItem(itemStack, true, false);
    }
  }

  private void addToPlayerInventoryOrDrop(EntityPlayer player, IItemStack itemStack) {
    addToPlayerInventoryOrDrop(player, InputHelper.toStack(itemStack));
  }
}
