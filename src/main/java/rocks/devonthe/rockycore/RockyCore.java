package rocks.devonthe.rockycore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rocks.devonthe.rockycore.proxy.CommonProxy;

@Mod(
	name = ModInfo.NAME,
	modid = ModInfo.MODID,
	version = ModInfo.VERSION,
	acceptedMinecraftVersions = ModInfo.MC_VERSION,
	dependencies = ModInfo.DEPENDENCIES
)
public class RockyCore {

	@Mod.Instance(ModInfo.MODID)
	public RockyCore instance;

	@SidedProxy(clientSide = "rocks.devonthe.rockycore.proxy.ClientProxy", serverSide = "rocks.devonthe.rockycore.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger = LogManager.getLogger(ModInfo.MODID);

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Starting Initialization for " + ModInfo.NAME);
		proxy.init(event);
	}
}
