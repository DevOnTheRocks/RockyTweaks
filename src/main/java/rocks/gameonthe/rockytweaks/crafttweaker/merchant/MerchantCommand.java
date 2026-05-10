package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import com.blamejared.mtlib.helpers.StringHelper;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;

import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.commons.lang3.text.StrBuilder;

import static rocks.gameonthe.rockytweaks.crafttweaker.merchant.VillagerHelper.TREASURE_MAP_TRADE;

public class MerchantCommand extends CraftTweakerCommand {

  private final List<String> arguments = Lists.newArrayList("professions", "careers", "trades");

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
            VillagerHelper.getProfessionCareers(p).forEach(c -> builder.append(" - ").append(c.getName()).appendNewLine());
          });
        } else {
          builder.append(profession.getRegistryName()).appendNewLine();
          VillagerHelper.getProfessionCareers(profession).forEach(c -> builder.append(" - ").append(c.getName()).appendNewLine());
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
        }
        if (profession == null) {
          VillagerHelper.getVillagerProfessions().forEach(p -> {
            builder.append(p.getRegistryName()).appendNewLine();
            VillagerHelper.getProfessionCareers(p).forEach(c -> {
              builder.append(" - ").append(c.getName()).appendNewLine();

              int idx = 1;
              for (List<EntityVillager.ITradeList> levelTrades : VillagerHelper.getCareerTrades(c)) {
                if (!levelTrades.isEmpty()) builder.append("    - Level ").append(idx++).appendNewLine();

                levelTrades.forEach(tradeItem -> {
                  builder.append("       * ").append(buildTradeString(tradeItem)).appendNewLine();
                });
              }
            });
          });
        } else if (career == null) {
          builder.append(profession.getRegistryName()).appendNewLine();
          VillagerHelper.getProfessionCareers(profession).forEach(c -> {
            builder.append(" - ").append(c.getName()).appendNewLine();

            int idx = 1;
            for (List<EntityVillager.ITradeList> levelTrades : VillagerHelper.getCareerTrades(c)) {
              if (!levelTrades.isEmpty()) builder.append("    - Level ").append(idx++).appendNewLine();

              levelTrades.forEach(tradeItem -> {
                builder.append("       * ").append(buildTradeString(tradeItem)).appendNewLine();
              });
            }
          });
        } else {
          builder.append(profession.getRegistryName()).appendNewLine();
          builder.append(" - ").append(career.getName()).appendNewLine();

          int idx = 1;
          for (List<EntityVillager.ITradeList> levelTrades : VillagerHelper.getCareerTrades(career)) {
            if (!levelTrades.isEmpty()) builder.append("    - Level ").append(idx++).appendNewLine();

            levelTrades.forEach(tradeItem -> {
              builder.append("       * ").append(buildTradeString(tradeItem)).appendNewLine();
            });
          }
        }
        CraftTweakerAPI.logCommand(builder.build());
        sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir."));
      } else {
        sender.sendMessage(new TextComponentString("I can't even... (╯°□°）╯︵ ┻━┻"));
      }
    }
  }

  private String buildTradeString(EntityVillager.ITradeList tradeItem) {
    if(tradeItem instanceof EntityVillager.EmeraldForItems) {
      EntityVillager.EmeraldForItems specialized = (EntityVillager.EmeraldForItems) tradeItem;
      return "[EmeraldForItems] buy: " + specialized.buyingItem.getRegistryName().toString() +
              ", emeralds: " + specialized.price.getFirst() + "/" + specialized.price.getSecond();
    }

    if(tradeItem instanceof MerchantTradeItem) {
      MerchantTradeItem specialized = (MerchantTradeItem) tradeItem;
      return "[MerchantTradeItem] chance: " + specialized.chance +
              ", buy1: " + specialized.recipe.getItemToBuy().serializeNBT().toString() +
              ", buy2: " + (specialized.recipe.hasSecondItemToBuy()?specialized.recipe.getSecondItemToBuy().serializeNBT().toString():"null") +
              ", sell: " + specialized.recipe.getItemToSell().serializeNBT().toString();
    }

    if(tradeItem instanceof EntityVillager.ItemAndEmeraldToItem) {
      EntityVillager.ItemAndEmeraldToItem specialized = (EntityVillager.ItemAndEmeraldToItem) tradeItem;
      return "[ItemAndEmeraldToItem] buy: " + specialized.buyingItemStack.serializeNBT().toString() +
              ", buyEmeralds: " + specialized.buyingPriceInfo.getFirst() + "/" + specialized.buyingPriceInfo.getSecond() +
              ", sell: " + specialized.sellingItemstack.serializeNBT().toString() +
              ", sellEmeralds: " + specialized.sellingPriceInfo.getFirst() + "/" + specialized.sellingPriceInfo.getSecond();
    }

    if(tradeItem instanceof EntityVillager.ListEnchantedBookForEmeralds) {
      return "[ListEnchantedBookForEmeralds] fixedBuy: book + emeralds" +
              ", fixedSell: enchanted_book + enchantment?";
    }

    if(tradeItem instanceof EntityVillager.ListEnchantedItemForEmeralds) {
      EntityVillager.ListEnchantedItemForEmeralds specialized = (EntityVillager.ListEnchantedItemForEmeralds) tradeItem;
      return "[ListEnchantedItemForEmeralds] emeralds: " + specialized.priceInfo.getFirst() + "/" + specialized.priceInfo.getSecond() +
              ", sell: " + specialized.enchantedItemStack.serializeNBT().toString() + " + enchantment?";
    }

    if(tradeItem instanceof EntityVillager.ListItemForEmeralds) {
      EntityVillager.ListItemForEmeralds specialized = (EntityVillager.ListItemForEmeralds) tradeItem;
      return "[ListItemForEmeralds] buy: " + specialized.itemToBuy.serializeNBT().toString() +
              ", emeralds: " + specialized.priceInfo.getFirst() + "/" + specialized.priceInfo.getSecond();
    }

    if(TREASURE_MAP_TRADE.isInstance(tradeItem)) {
      try {
        Field value = tradeItem.getClass().getDeclaredField("value");
        value.setAccessible(true);
        EntityVillager.PriceInfo price = (EntityVillager.PriceInfo) value.get(tradeItem);

        Field destinationType = tradeItem.getClass().getDeclaredField("destinationType");
        destinationType.setAccessible(true);
        MapDecoration.Type mapType = (MapDecoration.Type) destinationType.get(tradeItem);
        return "[TreasureMapForEmeralds] emeralds: " + price.getFirst() + "/" + price.getSecond() +
                ", fixedSell: minecraft:compass" +
                ", buyMapFilled: " + mapType.name();
      } catch (Exception e) {
        return "[TreasureMapForEmeralds] Malformed???";
      }
    }

    return "[Unknown Type] ???";
  }
}
