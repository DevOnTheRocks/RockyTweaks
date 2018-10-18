package rocks.devonthe.rockycore.proxy;

import crafttweaker.mc1120.commands.CTChatCommand;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import rocks.devonthe.rockycore.crafttweaker.merchant.MerchantCommand;

public class CommonProxy {

  public void init(FMLInitializationEvent event) {
    CTChatCommand.registerCommand(new MerchantCommand());
    // noop
  }
}
