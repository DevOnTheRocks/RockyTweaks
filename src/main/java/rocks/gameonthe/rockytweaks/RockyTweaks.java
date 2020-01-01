package rocks.gameonthe.rockytweaks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rocks.gameonthe.rockytweaks.proxy.CommonProxy;

@Mod(
    name = ModInfo.NAME,
    modid = ModInfo.MODID,
    version = ModInfo.VERSION,
    acceptedMinecraftVersions = ModInfo.MC_VERSION,
    dependencies = ModInfo.DEPENDENCIES
)
public class RockyTweaks {

  @Mod.Instance(ModInfo.MODID)
  public RockyTweaks instance;

  @SidedProxy(clientSide = "rocks.gameonthe.rockytweaks.proxy.ClientProxy", serverSide = "rocks.gameonthe.rockytweaks.proxy.CommonProxy")
  public static CommonProxy proxy;

  public static final Logger logger = LogManager.getLogger(ModInfo.MODID);

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger.info("Starting PreInitialization.");
    proxy.preInit(event);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    logger.info("Starting Initialization.");
    proxy.init(event);
  }
}
