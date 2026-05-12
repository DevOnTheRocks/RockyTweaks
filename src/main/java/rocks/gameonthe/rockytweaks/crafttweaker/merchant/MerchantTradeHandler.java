package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static rocks.gameonthe.rockytweaks.crafttweaker.merchant.VillagerHelper.TREASURE_MAP_TRADE;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.rockytweaks.Merchant")
@ZenRegister
public class MerchantTradeHandler {

  protected static final String name = "Merchant";
  private static final List<MerchantTrade> trades = Lists.newArrayList();

  @ZenMethod
  public static void addTrade(String profession, String career, IItemStack buy1, IItemStack buy2, IItemStack sell, int level) {
    Preconditions.checkNotNull(profession, "Profession is required");
    Preconditions.checkArgument(VillagerHelper.getProfession(profession).isPresent(), "Profession not found");
    VillagerRegistry.VillagerProfession p1 = VillagerHelper.getProfession(profession).get();
    Preconditions.checkNotNull(career, "Career is required");
    Preconditions.checkArgument(VillagerHelper.getCareer(p1, career).isPresent(), "Career not found");
    Preconditions.checkNotNull(buy1, "Input is required");
    Preconditions.checkNotNull(sell, "Output is required");
    Preconditions.checkArgument(level > 0, "Level is required");
    CraftTweakerAPI.apply(new MerchantTradeHandler.Add(
        new MerchantTrade(p1, VillagerHelper.getCareer(p1, career).get(), toStack(buy1), toStack(buy2), toStack(sell), level)
    ));
  }

  @ZenMethod
  public static void addTradeChance(String profession, String career, IItemStack buy1, IItemStack buy2, IItemStack sell, int level, float chance) {
    Preconditions.checkNotNull(profession);
    Preconditions.checkArgument(VillagerHelper.getProfession(profession).isPresent());
    VillagerRegistry.VillagerProfession p1 = VillagerHelper.getProfession(profession).get();
    Preconditions.checkNotNull(career);
    Preconditions.checkArgument(VillagerHelper.getCareer(p1, career).isPresent());
    Preconditions.checkNotNull(buy1);
    Preconditions.checkNotNull(sell);
    Preconditions.checkArgument(level > 0);
    Preconditions.checkArgument(chance > 0.0f);
    Preconditions.checkArgument(chance <= 100.0f);
    CraftTweakerAPI.apply(new MerchantTradeHandler.Add(
            new MerchantTrade(p1, VillagerHelper.getCareer(p1, career).get(), toStack(buy1), toStack(buy2), toStack(sell), level, chance)
    ));
  }

  @ZenMethod
  public static void addTradeChance(String profession, String career, IItemStack buy1, IItemStack sell, int level, float chance) {
    addTradeChance(profession, career, buy1, null, sell, level, chance);
  }

  @ZenMethod
  public static void addTrade(String profession, String career, IItemStack buy1, IItemStack sell, int level) {
    addTrade(profession, career, buy1, null, sell, level);
  }

  @ZenMethod
  public static void clearTrades(String profession, String career) {
    Preconditions.checkNotNull(profession);

    Optional<VillagerRegistry.VillagerProfession> professionOption = VillagerHelper.getProfession(profession);
    Preconditions.checkArgument(professionOption.isPresent());
    VillagerRegistry.VillagerProfession professionObject = professionOption.get();

    Preconditions.checkArgument(VillagerHelper.getCareer(professionObject, career).isPresent());
    Optional<VillagerRegistry.VillagerCareer> careerOption = VillagerHelper.getCareer(professionObject, career);
    Preconditions.checkArgument(careerOption.isPresent());
    VillagerRegistry.VillagerCareer careerObject = careerOption.get();

    List<List<EntityVillager.ITradeList>> allTrades = VillagerHelper.getCareerTrades(careerObject);

    if (allTrades != null) {
      for (List<EntityVillager.ITradeList> tradeLevel : allTrades)  {
        if (tradeLevel != null) {
          tradeLevel.clear();
        }
      }
    }
  }

  @ZenMethod
  public static void removeTradeItem(String profession, String career, IItemStack target ) {
    Preconditions.checkNotNull(profession);

    Optional<VillagerRegistry.VillagerProfession> professionOption = VillagerHelper.getProfession(profession);
    Preconditions.checkArgument(professionOption.isPresent());
    VillagerRegistry.VillagerProfession professionObject = professionOption.get();

    Preconditions.checkArgument(VillagerHelper.getCareer(professionObject, career).isPresent());
    Optional<VillagerRegistry.VillagerCareer> careerOption = VillagerHelper.getCareer(professionObject, career);
    Preconditions.checkArgument(careerOption.isPresent());
    VillagerRegistry.VillagerCareer careerObject = careerOption.get();

    List<List<EntityVillager.ITradeList>> allTrades = VillagerHelper.getCareerTrades(careerObject);

    Preconditions.checkNotNull(target);

    String targetId = target.getDefinition().getId();
    if (allTrades != null) {
      for (List<EntityVillager.ITradeList> tradeLevel : allTrades)  {
        if (tradeLevel != null) {

          List<EntityVillager.ITradeList> removeList = new ArrayList();

          for (EntityVillager.ITradeList trade : tradeLevel)  {

            // Emerald for Items
            if (trade instanceof EntityVillager.EmeraldForItems) {
              if ( ((EntityVillager.EmeraldForItems) trade).buyingItem.equals(target) ) {
                removeList.add(trade);
              }

              if ("minecraft:emerald".equals(targetId)) removeList.add(trade);
            }

            // Our Trade Item, so we can remove it again
            if (trade instanceof MerchantTradeItem) {
              if ( compareStack(target, ((MerchantTradeItem) trade).recipe.getItemToBuy()) ) {
                removeList.add(trade);
              }
              if ( compareStack(target, ((MerchantTradeItem) trade).recipe.getSecondItemToBuy()) ) {
                removeList.add(trade);
              }
              if ( compareStack(target, ((MerchantTradeItem) trade).recipe.getItemToSell()) ) {
                removeList.add(trade);
              }
            }

            // Item and Emerald To Item
            if (trade instanceof EntityVillager.ItemAndEmeraldToItem) {
              if ( compareStack(target, ((EntityVillager.ItemAndEmeraldToItem)trade).buyingItemStack) ) {
                removeList.add(trade);
              }
              if ( compareStack(target, ((EntityVillager.ItemAndEmeraldToItem)trade).sellingItemstack) ) {
                removeList.add(trade);
              }

              if ("minecraft:emerald".equals(targetId)) removeList.add(trade);
            }

            // List Enchanted Book For Emeralds
            if (trade instanceof EntityVillager.ListEnchantedBookForEmeralds) {
              if ("minecraft:book".equals(targetId)
                      || "minecraft:emerald".equals(targetId)
                      || "minecraft:enchanted_book".equals(targetId)) {
                removeList.add(trade);
              }
            }

            // List Enchanted Item For Emeralds
            if (trade instanceof EntityVillager.ListEnchantedItemForEmeralds) {
              if ( compareStack(target, ((EntityVillager.ListEnchantedItemForEmeralds)trade).enchantedItemStack) ) {
                removeList.add(trade);
              }

              if ("minecraft:emerald".equals(targetId)) removeList.add(trade);
            }

            // List Item For Emeralds
            if (trade instanceof  EntityVillager.ListItemForEmeralds) {

              if ( compareStack(target, ((EntityVillager.ListItemForEmeralds)trade).itemToBuy) ) {
                removeList.add(trade);
              }

              if ("minecraft:emerald".equals(targetId)) removeList.add(trade);
            }

            if(TREASURE_MAP_TRADE.isInstance(trade)) {
              if ("minecraft:map_filled".equals(targetId)
                      || "minecraft:emerald".equals(targetId)
                      || "minecraft:compass".equals(targetId)) {
                removeList.add(trade);
              }
            }
          }

          tradeLevel.removeAll(removeList);
        }
      }
    }
  }

  private static boolean compareStack(IItemStack itemStack, Object other) {
    if (other instanceof Item) {
      Item otherItem = (Item) other;
      return itemStack.getDefinition().getId().equals(otherItem.getRegistryName().toString());
    }

    if (other instanceof ItemStack) {
      ItemStack otherStack = (ItemStack) other;
      return itemStack.getMetadata() == otherStack.getMetadata()
              && itemStack.getDefinition().getId().equals(otherStack.getItem().getRegistryName().toString());
    }

    return false;
  }

  private static class Add extends BaseListAddition<MerchantTrade> {

    public Add(MerchantTrade recipe) {
      super(MerchantTradeHandler.name, MerchantTradeHandler.trades);
      this.recipes.add(recipe);
    }

    @Override
    public void apply() {
      if (!this.recipes.isEmpty()) {
        for (MerchantTrade trade : this.recipes) {
          if (trade != null) {
            if (MerchantTradeHandler.trades.add(trade)) {
              this.successful.add(trade);
              trade.register();
            } else {
              LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo()));
            }
          } else {
            LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
          }
        }
      }
    }

    @Override
    public String getRecipeInfo(MerchantTrade trade) {
      return LogHelper.getStackDescription(trade.recipe);
    }
  }

}
