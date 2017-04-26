package rocks.devonthe.rockycore.proxy;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import rocks.devonthe.rockycore.crafttweaker.CraftTweakerModule;

public class CommonProxy {

	public void init(FMLInitializationEvent event) {
		if (Loader.isModLoaded("crafttweaker")) new CraftTweakerModule();
	}
}
