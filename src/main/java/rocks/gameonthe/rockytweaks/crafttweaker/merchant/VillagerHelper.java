package rocks.gameonthe.rockytweaks.crafttweaker.merchant;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.lang.reflect.Field;
import java.util.*;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public final class VillagerHelper {

  private VillagerHelper(){
  }

  public static Class TREASURE_MAP_TRADE = Arrays.stream(EntityVillager.class.getDeclaredClasses()).filter(clazz ->
          "net.minecraft.entity.passive.EntityVillager.TreasureMapForEmeralds".equals(clazz.getCanonicalName())
  ).findFirst().get();

  public static Collection<VillagerProfession> getVillagerProfessions() {
    return ForgeRegistries.VILLAGER_PROFESSIONS.getValuesCollection();
  }

  public static Optional<VillagerRegistry.VillagerProfession> getProfession(String profession) {
    for (VillagerRegistry.VillagerProfession p : getVillagerProfessions()) {
      if (p.getRegistryName() != null
          && (p.getRegistryName().toString().equalsIgnoreCase(profession)
          || p.getRegistryName().toString().equalsIgnoreCase("minecraft:" + profession))) {
        return Optional.of(p);
      }
    }
    return Optional.empty();
  }

  public static Optional<VillagerRegistry.VillagerCareer> getCareer(VillagerRegistry.VillagerProfession profession, String career) {
    return getProfessionCareers(profession).stream().filter(c -> c.getName().equalsIgnoreCase(career)).findAny();
  }

  public static ResourceLocation getProfessionName(VillagerRegistry.VillagerProfession profession) {
    try {
      Field namefield = profession.getClass().getDeclaredField("name");
      namefield.setAccessible(true);
      return (ResourceLocation) namefield.get(profession);
    } catch (NoSuchFieldException | SecurityException |  IllegalArgumentException | IllegalAccessException e) {
      return new ResourceLocation("");
    }
  }

  public static List<VillagerRegistry.VillagerCareer> getProfessionCareers(VillagerRegistry.VillagerProfession profession) {
    try {
      Field namefield = profession.getClass().getDeclaredField("careers");
      namefield.setAccessible(true);
      return (List<VillagerRegistry.VillagerCareer>) namefield.get(profession);
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public static List<List<EntityVillager.ITradeList>> getCareerTrades(VillagerRegistry.VillagerCareer career) {
    try {
      Field namefield = career.getClass().getDeclaredField("trades");
      namefield.setAccessible(true);
      return (List<List<EntityVillager.ITradeList>>) namefield.get(career);
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public static VillagerRegistry.VillagerCareer getCareerRef(String professionName, String careerName) {
    Optional<Optional<VillagerRegistry.VillagerCareer>> firstEntry = getVillagerProfessions().stream().filter(profession -> {
      String name = getProfessionName(profession).toString();
      return professionName.equals(name);
    }).map(profession -> getProfessionCareers(profession).stream().filter(career -> careerName.equals(career.getName())).findFirst()).findFirst();
      if (firstEntry.isPresent()) {
        Optional<VillagerRegistry.VillagerCareer> innerEntry = firstEntry.get();
          return innerEntry.orElse(null);
      } else {
        return null;
      }
  }

  public static Multimap<Integer, MerchantRecipe> getMerchantRecipes(VillagerRegistry.VillagerCareer career) {
    Multimap<Integer, MerchantRecipe> recipeMultimap = ArrayListMultimap.create();
    for (int i = 1; career.getTrades(i) != null; i++) {
      career.getTrades(i); // TODO: Turn ITradeList into MerchantRecipes
    }
    return recipeMultimap;
  }
}
