package rocks.gameonthe.rockycore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rocks.gameonthe.rockycore.proxy.CommonProxy;

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

  @SidedProxy(clientSide = "rocks.gameonthe.rockycore.proxy.ClientProxy", serverSide = "rocks.gameonthe.rockycore.proxy.CommonProxy")
  public static CommonProxy proxy;

  public static Logger logger = LogManager.getLogger(ModInfo.MODID);

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
