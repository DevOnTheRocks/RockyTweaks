package rocks.gameonthe.rockycore.proxy;

import crafttweaker.mc1120.commands.CTChatCommand;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rocks.gameonthe.rockycore.crafttweaker.merchant.MerchantCommand;

public class CommonProxy {

  public void preInit(FMLPreInitializationEvent event) {
    // noop
  }

  public void init(FMLInitializationEvent event) {
    CTChatCommand.registerCommand(new MerchantCommand());
  }
}
