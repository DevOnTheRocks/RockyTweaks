package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import java.util.List;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.rockytweaks.Merchant")
@ZenRegister
public class MerchantTradeHandler {

  protected static final String name = "Merchant";
  private static List<MerchantTrade> trades = Lists.newArrayList();

  @ZenMethod
  public static void addTrade(String profession, String career, IItemStack buy1, IItemStack buy2, IItemStack sell, int level) {
    Preconditions.checkNotNull(profession);
    Preconditions.checkArgument(VillagerHelper.getProfession(profession).isPresent());
    VillagerRegistry.VillagerProfession p1 = VillagerHelper.getProfession(profession).get();
    Preconditions.checkNotNull(career);
    Preconditions.checkArgument(VillagerHelper.getCareer(p1, career).isPresent());
    Preconditions.checkNotNull(buy1);
    Preconditions.checkNotNull(sell);
    Preconditions.checkArgument(level > 0);
    CraftTweakerAPI.apply(new MerchantTradeHandler.Add(
        new MerchantTrade(p1, VillagerHelper.getCareer(p1, career).get(), toStack(buy1), toStack(buy2), toStack(sell), level)
    ));
  }

  @ZenMethod
  public static void addTrade(String profession, String career, IItemStack buy1, IItemStack sell, int level) {
    addTrade(profession, career, buy1, null, sell, level);
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
              LogHelper.logError(String.format("Error adding %s Recipe for %s", this.name, this.getRecipeInfo(trade)));
            }
          } else {
            LogHelper.logError(String.format("Error adding %s Recipe: null object", this.name));
          }
        }
      }
    }

    @Override
    public String getRecipeInfo(MerchantTrade trade) {
      return LogHelper.getStackDescription(trade.getRecipe());
    }
  }

}
