package rocks.gameonthe.rockytweaks.crafttweaker.anvil;

import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.recipes.ICraftingInfo;
import crafttweaker.api.recipes.ICraftingInventory;
import crafttweaker.api.world.IWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AnvilCraftingInfo implements ICraftingInfo {

  private final EntityPlayer player;
  private final World world;

  public AnvilCraftingInfo(EntityPlayer player, World world) {
    this.player = player;
    this.world = world;
  }

  @Override
  public ICraftingInventory getInventory() {
    return null;
  }

  @Override
  public IPlayer getPlayer() {
    return CraftTweakerMC.getIPlayer(player);
  }

  @Override
  public int getDimension() {
    return getWorld().getDimension();
  }

  @Override
  public IWorld getWorld() {
    return CraftTweakerMC.getIWorld(world);
  }
}
