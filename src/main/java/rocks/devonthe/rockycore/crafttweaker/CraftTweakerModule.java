package rocks.devonthe.rockycore.crafttweaker;

import minetweaker.MineTweakerAPI;
import rocks.devonthe.rockycore.RockyCore;
import rocks.devonthe.rockycore.crafttweaker.anvil.AnvilRecipeHandler;
import rocks.devonthe.rockycore.crafttweaker.merchant.MerchantTradeHandler;

public class CraftTweakerModule {

	public CraftTweakerModule() {
		MineTweakerAPI.registerClass(AnvilRecipeHandler.class);
		MineTweakerAPI.registerClass(MerchantTradeHandler.class);
		RockyCore.logger.info("Successfully Integrated with CraftTweaker!");
	}
}
