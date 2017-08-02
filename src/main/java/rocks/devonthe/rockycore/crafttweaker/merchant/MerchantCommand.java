package rocks.devonthe.rockycore.crafttweaker.merchant;

import com.blamejared.mtlib.helpers.StringHelper;
import com.google.common.collect.Lists;
import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.commons.lang3.text.StrBuilder;

import java.util.List;

public class MerchantCommand implements ICommandFunction {

	private final List<String> arguments = Lists.newArrayList("professions", "careers"); // TODO: Add "trades"

	@Override
	public void execute(String[] args, IPlayer sender) {
		if (args.length == 0 || !arguments.contains(args[0])) {
			sender.sendChat("Invalid arguments for command. Valid arguments: " + StringHelper.join(arguments, ", "));
		} else {
			if (args[0].equalsIgnoreCase("professions")) {
				StrBuilder builder = new StrBuilder("List of Merchant Professions:").appendNewLine();
				VillagerHelper.getVillagerProfessions().forEach(p -> builder.append(p.getRegistryName()).appendNewLine());
				MineTweakerAPI.logCommand(builder.build());
				sender.sendChat("List generated; see minetweaker.log in your minecraft dir.");
			} else if (args[0].equalsIgnoreCase("careers")) {
				StrBuilder builder = new StrBuilder("List of Merchant Careers:").appendNewLine();
				VillagerRegistry.VillagerProfession profession = null;
				if (args.length > 1) {
					if (VillagerHelper.getProfession(args[1]).isPresent()) {
						profession = VillagerHelper.getProfession(args[1]).get();
					} else {
						sender.sendChat("Invalid profession.");
					}
				}
				if (profession == null) {
					VillagerHelper.getVillagerProfessions().forEach(p -> {
						builder.append(p.getRegistryName()).appendNewLine();
						VillagerHelper.getVillagerCareers(p).forEach(c -> builder.append(" - ").append(c.getName()).appendNewLine());
					});
				} else {
					builder.append(profession.getRegistryName()).appendNewLine();
					VillagerHelper.getVillagerCareers(profession).forEach(c -> builder.append(" - ").append(c.getName()).appendNewLine());
				}
				MineTweakerAPI.logCommand(builder.build());
				sender.sendChat("List generated; see minetweaker.log in your minecraft dir.");
			} else {
				sender.sendChat("I can't even... (╯°□°）╯︵ ┻━┻");
			}
		}
	}
}
