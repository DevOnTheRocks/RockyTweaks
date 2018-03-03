package rocks.gameonthe.rockycore.crafttweaker.merchant;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Optional;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class VillagerHelper {

  public static List<VillagerRegistry.VillagerProfession> getVillagerProfessions() {
    return ForgeRegistries.VILLAGER_PROFESSIONS.getValues();
  }

  public static List<VillagerRegistry.VillagerCareer> getVillagerCareers(VillagerRegistry.VillagerProfession profession) {
    List<VillagerRegistry.VillagerCareer> careers = Lists.newArrayList(profession.getCareer(0));
    int i = 0;
    do {
      i++;
      careers.add(profession.getCareer(i));
    } while (!careers.get(0).equals(careers.get(i)));
    careers.remove(profession.getCareer(i));
    return careers;
  }

  public static Optional<VillagerRegistry.VillagerProfession> getProfession(String profession) {
    for (VillagerRegistry.VillagerProfession p : getVillagerProfessions()) {
      if (p.getRegistryName() != null
          && p.getRegistryName().toString().equalsIgnoreCase(profession)
          || p.getRegistryName().toString().equalsIgnoreCase("minecraft:" + profession)) {
        return Optional.of(p);
      }
    }
    return Optional.empty();
  }

  public static Optional<VillagerRegistry.VillagerCareer> getCareer(VillagerRegistry.VillagerProfession profession, String career) {
    return getVillagerCareers(profession).stream().filter(c -> c.getName().equalsIgnoreCase(career)).findAny();
  }

  public static Multimap<Integer, MerchantRecipe> getMerchantRecipes(VillagerRegistry.VillagerCareer career) {
    Multimap<Integer, MerchantRecipe> recipeMultimap = ArrayListMultimap.create();
    for (int i = 1; career.getTrades(i) != null; i++) {
      career.getTrades(i); // TODO: Turn ITradeList into MerchantRecipes
    }
    return recipeMultimap;
  }
}
