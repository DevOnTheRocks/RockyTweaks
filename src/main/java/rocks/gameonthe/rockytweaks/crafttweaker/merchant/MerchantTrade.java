package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import com.blamejared.mtlib.helpers.LogHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry;


import java.util.Random;

public class MerchantTrade {

  private VillagerRegistry.VillagerProfession profession;
  private VillagerRegistry.VillagerCareer career;
  public final MerchantRecipe recipe;
  private int level;
  private float chance;

  public MerchantTrade(VillagerRegistry.VillagerProfession profession, VillagerRegistry.VillagerCareer career, ItemStack buy1, ItemStack buy2,
                       ItemStack sell, int level, float chance) {
    this(profession, career, new MerchantRecipe(buy1, buy2, sell), level, chance);
  }

  public MerchantTrade(VillagerRegistry.VillagerProfession profession, VillagerRegistry.VillagerCareer career, ItemStack buy1, ItemStack buy2,
      ItemStack sell, int level) {
    this(profession, career, new MerchantRecipe(buy1, buy2, sell), level);
  }

  public MerchantTrade(VillagerRegistry.VillagerProfession profession, VillagerRegistry.VillagerCareer career, MerchantRecipe recipe, int level) {
    this.profession = profession;
    this.career = career;
    this.recipe = recipe;
    this.level = level;
    this.chance = 100.0f;
  }

  public MerchantTrade(VillagerRegistry.VillagerProfession profession, VillagerRegistry.VillagerCareer career, MerchantRecipe recipe, int level, float chance) {
    this.profession = profession;
    this.career = career;
    this.recipe = recipe;
    this.level = level;
    this.chance = chance;
  }

  public int getLevel() {
    return level;
  }

  public void register() {
    try {
      VillagerHelper.getCareerRef(VillagerHelper.getProfessionName(profession).toString(), career.getName())
              .addTrade(getLevel(), toTradeItem());
    } catch (NullPointerException e) {
      LogHelper.logError(String.format("Got invalid career: %s", e.getMessage()));
    }
  }

  private EntityVillager.ITradeList toTradeItem() {
    return (EntityVillager.ITradeList) new MerchantTradeItem(this.recipe, this.chance);
  }
}
