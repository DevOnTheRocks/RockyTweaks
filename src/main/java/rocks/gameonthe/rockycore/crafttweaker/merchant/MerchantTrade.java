package rocks.gameonthe.rockycore.crafttweaker.merchant;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class MerchantTrade {

  private VillagerRegistry.VillagerProfession profession;
  private VillagerRegistry.VillagerCareer career;
  private MerchantRecipe recipe;
  private int level;

  public MerchantTrade(VillagerRegistry.VillagerProfession profession, VillagerRegistry.VillagerCareer career, ItemStack buy1, ItemStack buy2,
      ItemStack sell, int level) {
    this(profession, career, new MerchantRecipe(buy1, buy2, sell), level);
  }

  public MerchantTrade(VillagerRegistry.VillagerProfession profession, VillagerRegistry.VillagerCareer career, MerchantRecipe recipe, int level) {
    this.profession = profession;
    this.career = career;
    this.recipe = recipe;
    this.level = level;
  }

  public VillagerRegistry.VillagerProfession getProfession() {
    return profession;
  }

  public VillagerRegistry.VillagerCareer getCareer() {
    return career;
  }

  public MerchantRecipe getRecipe() {
    return recipe;
  }

  public int getLevel() {
    return level;
  }

  public void register() {
    profession.getCareer(VillagerHelper.getVillagerCareers(profession).indexOf(career))
        .addTrade(getLevel(), (EntityVillager.ITradeList) (merchant, recipeList, random) -> recipeList.add(getRecipe()));
  }
}
