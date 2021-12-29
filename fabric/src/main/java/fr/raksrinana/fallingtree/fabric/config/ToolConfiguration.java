package fr.raksrinana.fallingtree.fabric.config;

import fr.raksrinana.fallingtree.fabric.config.validator.ItemId;
import fr.raksrinana.fallingtree.fabric.config.validator.Min;
import fr.raksrinana.fallingtree.fabric.config.validator.MinMax;
import lombok.Getter;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.world.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static fr.raksrinana.fallingtree.fabric.config.DamageRounding.ROUND_DOWN;

@Getter
public class ToolConfiguration{
	@Tooltip(count = 3)
	@Comment("Additional list of tools that can be used to chop down a tree. \n" +
			"INFO: Items marked with the axe tag will already be whitelisted.")
	@ItemId
	public List<String> whitelisted = new ArrayList<>();
	@Tooltip(count = 3)
	@Comment("List of tools that should not be considered as tools. \n" +
			"INFO: This wins over the whitelist.")
	@ItemId
	public List<String> blacklisted = new ArrayList<>();
	@Tooltip(count = 3)
	@Comment("When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability.\n" +
			"INFO: Only in instantaneous mode")
	public boolean preserve = false;
	@Tooltip(count = 4)
	@Comment("When set to true, the mod will be activated no matter what you have in your hand.\n" +
			"INFO: Blacklist still can be use to restrict some tools.")
	public boolean ignoreTools = false;
	@Tooltip(count = 2)
	@Comment("Define if the tool must be enchanted in order to be able to chop trees with it (whitelist/blacklist still applies).")
	public boolean requireEnchant = true;
	@Tooltip(count = 7)
	@Comment("""
			Defines the number of times the damage is applied to the tool.\s
			ie: if set to 1 then breaking 5 logs will give 5 damage.\s
			ie: if set to 2 then breaking 5 logs will give 10 damage.\s
			If set to 0, it'll still apply 1 damage for every cut.\s
			INFO: This only applies when the tree is cut when using the mod.
			INFO: If damage is not a whole number then damageRounding will be used to determine what the final damage is.""")
	@Min(0)
	public double damageMultiplicand = 1;
	@Tooltip(count = 8)
	@Comment("""
			How damage taken should be rounded if it isn't a whole number.\s
			ROUNDING will round to the closest whole number.\s
			ROUND_DOWN will round down.\s
			ROUND_UP will round up.\s
			PROBABILISTIC will treat decimal fraction as a probability of rounding up.\s
			ie: 9.45 will have 45% chance of being rounded up to 10 and 55% chance of being rounded down to 9.""")
	@EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
	public DamageRounding damageRounding = ROUND_DOWN;
	@Tooltip(count = 15)
	@Comment("""
			Applies a speed modifier when breaking the tree.\s
			0 will disable this, so the speed will be the default one of breaking a block.\s
			If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs.\s
			If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs.\s
			INFO: Only in INSTANTANEOUS mode.\s
			WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there.""")
	@MinMax(min = 0, max = 50)
	public double speedMultiplicand = 0;
	
	public Collection<Item> getBlacklistedItems(){
		return ConfigCache.getInstance().getToolsBlacklisted(this::getBlacklisted);
	}
	
	public Collection<Item> getWhitelistedItems(){
		return ConfigCache.getInstance().getToolsWhitelisted(this::getWhitelisted);
	}
}
