package rocks.devonthe.rockycore.crafttweaker.anvil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;

public class AnvilListener {

	public AnvilListener() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		AnvilRecipeHandler.getRecipes().stream()
			.filter(recipe -> matches(event.getLeft(), recipe.getLeft()) && greaterThanOrEqual(event.getRight(), recipe.getRight()))
			.sorted(Comparator.comparing(AnvilRecipe::getRightCount).reversed())
			.findFirst()
			.ifPresent(recipe -> {
				event.setCost(recipe.getCost());
				event.setMaterialCost(recipe.getRight().getCount());
				event.setOutput(recipe.getOutput());
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
