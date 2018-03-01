package rocks.devonthe.rockycore.crafttweaker.merchant;

import com.blamejared.mtlib.helpers.StringHelper;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.commons.lang3.text.StrBuilder;

public class MerchantCommand extends CraftTweakerCommand {

  private final List<String> arguments = Lists.newArrayList("professions", "careers"); // TODO: Add "trades"

  public MerchantCommand() {
    super("merchant");
    setDescription(new TextComponentString("Provides a list of valid merchant professions or careers"));
  }

  @Override
  protected void init() {
  }

  @Override
  public List<String> getSubSubCommand(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return arguments;
  }

  @Override
  public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
    if (args.length == 0 || !arguments.contains(args[0])) {
      sender.sendMessage(new TextComponentString("Invalid arguments for command. Valid arguments: " + StringHelper.join(arguments, ", ")));
    } else {
      if (args[0].equalsIgnoreCase("professions")) {
        StrBuilder builder = new StrBuilder("List of Merchant Professions:").appendNewLine();
        VillagerHelper.getVillagerProfessions().forEach(p -> builder.append(p.getRegistryName()).appendNewLine());
        CraftTweakerAPI.logCommand(builder.build());
        sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir."));
      } else if (args[0].equalsIgnoreCase("careers")) {
        StrBuilder builder = new StrBuilder("List of Merchant Careers:").appendNewLine();
        VillagerRegistry.VillagerProfession profession = null;
        if (args.length > 1) {
          if (VillagerHelper.getProfession(args[1]).isPresent()) {
            profession = VillagerHelper.getProfession(args[1]).get();
          } else {
            sender.sendMessage(new TextComponentString("Invalid profession."));
          }
        }
        if (profession == null) {
          VillagerHelper.getVillagerProfessions().forEach(p -> {
            builder.append(p.getRegistryName()).appendNewLine();
            VillagerHelper.getVillagerCareers(p).forEach(c -> builder.append(" - ").append(c.getName()).appendNewLine());
          });
        } else {
          builder.append(profession.getRegistryName()).appendNewLine();
          VillagerHelper.getVillagerCareers(profession).forEach(c -> builder.append(" - ").append(c.getName()).appendNewLine());
        }
        CraftTweakerAPI.logCommand(builder.build());
        sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir."));
      } else if (args[0].equalsIgnoreCase("trades")) {
        StrBuilder builder = new StrBuilder("List of Merchant Trades:").appendNewLine();
        VillagerRegistry.VillagerProfession profession = null;
        VillagerRegistry.VillagerCareer career = null;
        if (args.length > 1) {
          if (VillagerHelper.getProfession(args[1]).isPresent()) {
            profession = VillagerHelper.getProfession(args[1]).get();
          } else {
            sender.sendMessage(new TextComponentString("Invalid profession."));
          }
          if (args.length > 2) {
            if (VillagerHelper.getCareer(profession, args[2]).isPresent()) {
              career = VillagerHelper.getCareer(profession, args[2]).get();
            } else {
              sender.sendMessage(new TextComponentString("Invalid career."));
            }
          }
          if (profession == null) {
            VillagerHelper.getVillagerProfessions().forEach(p -> {
              VillagerHelper.getVillagerCareers(p).forEach(c -> {
                // TODO: Get the Merchant Recipes ¯\_(ツ)_/¯
              });
            });
          }
        }
        CraftTweakerAPI.logCommand(builder.build());
      } else {
        sender.sendMessage(new TextComponentString("I can't even... (╯°□°）╯︵ ┻━┻"));
      }
    }
  }
}
