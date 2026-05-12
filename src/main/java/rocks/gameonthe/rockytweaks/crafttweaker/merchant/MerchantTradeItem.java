package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

public class MerchantTradeItem implements EntityVillager.ITradeList {

    public final MerchantRecipe recipe;
    public final float chance;

    public MerchantTradeItem(MerchantRecipe recipe, float chance) {
        this.recipe = recipe;
        this.chance = chance;
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

    @Override
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
        if(recipe != null) {
            MerchantRecipe recipeChance = getRecipe(random);
            if (recipeChance != null) {
                recipeList.add(recipeChance);
            }
        }
    }
}