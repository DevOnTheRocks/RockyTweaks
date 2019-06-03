package rocks.gameonthe.rockycore.crafttweaker.anvil;

import java.util.Comparator;

import net.minecraft.item.Item;
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
        .filter(recipe -> recipe.isValid() && matches(event.getLeft(), recipe.getLeft())
            && greaterThanOrEqual(event.getRight(), recipe.getRight())).max(Comparator.comparing(AnvilRecipe::getRightCount))
        .ifPresent(recipe -> {
          event.setCanceled(false);
          event.setCost(recipe.getCost());
          event.setMaterialCost(recipe.getRight().getCount());
          event.setOutput(recipe.getOutput());
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
        .filter(recipe -> recipe.isValid() && matches(event.getItemInput(), recipe.getLeft())
            && greaterThanOrEqual(event.getIngredientInput(), recipe.getRight())).max(Comparator.comparing(AnvilRecipe::getRightCount))
        .ifPresent(recipe -> {
          if (event.getItemInput().getCount() > recipe.getLeft().getCount()) {
            ItemStack itemStack = event.getItemInput().copy();
            itemStack.shrink(recipe.getLeft().getCount());
            boolean drop = !event.getEntityPlayer().inventory.addItemStackToInventory(itemStack);
            if (drop) {
              event.getEntityPlayer().dropItem(itemStack, true, false);
            }
          }
        });
  }

  private boolean matches(ItemStack stack1, ItemStack stack2) {
    Item item1 = stack1.getItem();
    return item1.equals(stack2.getItem())
        && (item1.isDamageable() || stack1.getMetadata() == stack2.getMetadata())
        && stack1.hasTagCompound() == stack2.hasTagCompound()
        && (!(stack1.hasTagCompound()) || stack1.getTagCompound().equals(stack2.getTagCompound()));
  }

  private boolean greaterThanOrEqual(ItemStack stack1, ItemStack stack2) {
    return matches(stack1, stack2)
        && stack1.getCount() >= stack2.getCount();
  }
}
