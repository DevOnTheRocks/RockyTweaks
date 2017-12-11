package rocks.devonthe.rockycore.crafttweaker.anvil;

import java.util.Comparator;
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
    AnvilRecipeHandler.getRecipes().stream()
        .filter(recipe -> recipe.isValid() && matches(event.getLeft(), recipe.getLeft())
            && greaterThanOrEqual(event.getRight(),
            recipe.getRight()))
        .sorted(Comparator.comparing(AnvilRecipe::getRightCount).reversed())
        .findFirst()
        .ifPresent(recipe -> {
          event.setCost(recipe.getCost());
          event.setMaterialCost(recipe.getRight().getCount());
          event.setOutput(recipe.getOutput());
        });
  }

  @SubscribeEvent
  public void onAnvilCraft(AnvilRepairEvent event) {
    AnvilRecipeHandler.getRecipes().stream()
        .filter(recipe -> recipe.isValid() && matches(event.getItemInput(), recipe.getLeft())
            && greaterThanOrEqual(
            event.getIngredientInput(), recipe.getRight()))
        .sorted(Comparator.comparing(AnvilRecipe::getRightCount).reversed())
        .findFirst()
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
    return stack1.getItem().equals(stack2.getItem())
        && stack1.hasTagCompound() == stack2.hasTagCompound()
        && (!(stack1.hasTagCompound()) || stack1.getTagCompound().equals(stack2.getTagCompound()));
  }

  private boolean greaterThanOrEqual(ItemStack stack1, ItemStack stack2) {
    return matches(stack1, stack2)
        && stack1.getCount() >= stack2.getCount();
  }
}
