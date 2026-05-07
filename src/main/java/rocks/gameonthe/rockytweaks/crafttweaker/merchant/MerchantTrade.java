package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry;


import java.util.Random;

public class MerchantTrade {

  private VillagerRegistry.VillagerProfession profession;
  private VillagerRegistry.VillagerCareer career;
  private MerchantRecipe recipe;
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

  public VillagerRegistry.VillagerProfession getProfession() {
    return profession;
  }

  public VillagerRegistry.VillagerCareer getCareer() {
    return career;
  }

  public MerchantRecipe getRecipe(Random random) {
    if (random == null) {
      return recipe;
    } else {
      float randomValue = MathHelper.nextFloat(random, 0.0f, 100.0f);
      if (randomValue < this.chance) {
        return recipe;
      } else {
        return null;
      }
    }
  }

  public int getLevel() {
    return level;
  }

  public void register() {
    profession.getCareer(VillagerHelper.getVillagerCareers(profession).indexOf(career))
        .addTrade(getLevel(), (EntityVillager.ITradeList) (merchant, recipeList, random) -> {
          if(recipe != null) {
            MerchantRecipe recipeChance = getRecipe(random);
            if (recipeChance != null) {
              recipeList.add(recipeChance);
            }
          }
        });
  }
}
