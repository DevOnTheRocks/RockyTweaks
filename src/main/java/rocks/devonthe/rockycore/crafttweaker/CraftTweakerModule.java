package rocks.devonthe.rockycore.crafttweaker;

import minetweaker.MineTweakerAPI;
import rocks.devonthe.rockycore.RockyCore;
import rocks.devonthe.rockycore.crafttweaker.anvil.AnvilRecipeHandler;

public class CraftTweakerModule {

	public CraftTweakerModule() {
		MineTweakerAPI.registerClass(AnvilRecipeHandler.class);
		RockyCore.logger.info("Successfully Integrated with CraftTweaker!");
	}
}
