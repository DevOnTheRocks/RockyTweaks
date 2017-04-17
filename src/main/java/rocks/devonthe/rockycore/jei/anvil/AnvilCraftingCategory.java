package rocks.devonthe.rockycore.jei.anvil;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class AnvilCraftingCategory extends BlankRecipeCategory<AnvilCraftingWrapper> {

	private final IDrawable background;

	public AnvilCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(
			new ResourceLocation("jei","textures/gui/container/anvil.png"), 16, 40, 145, 37
		);
	}

	@Override public String getUid() {
		return VanillaRecipeCategoryUid.ANVIL;
	}

	@Override public String getTitle() {
		return Blocks.ANVIL.getLocalizedName();
	}

	@Override public IDrawable getBackground() {
		return background;
	}

	@Override public void setRecipe(IRecipeLayout recipeLayout, AnvilCraftingWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 10, 6);
		guiItemStacks.init(1, true, 59, 6);
		guiItemStacks.init(2, false, 117, 6);

		guiItemStacks.set(ingredients);
	}
}
