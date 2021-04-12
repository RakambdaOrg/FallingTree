package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.config.validator.ItemId;
import fr.raksrinana.fallingtree.config.validator.Min;
import fr.raksrinana.fallingtree.config.validator.MinMax;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
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
	@Comment("When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability.")
	public boolean preserve = false;
	@Tooltip(count = 4)
	@Comment("When set to true, the mod will be activated no matter what you have in your hand (or empty hand). \n" +
			"INFO: Blacklist still can be use to restrict some tools.")
	public boolean ignoreTools = false;
	@Tooltip(count = 7)
	@Comment("Defines the number of times the damage is applied to the tool. \n" +
			"ie: if set to 1 then breaking 5 logs will give 5 damage. \n" +
			"ie: if set to 2 then breaking 5 logs will give 10 damage. \n" +
			"If set to 0, it'll still apply 1 damage for every cut. \n" +
			"INFO: This only applies when the tree is cut when using the mod.")
	@Min(0)
	public int damageMultiplicand = 1;
	@Tooltip(count = 15)
	@Comment("Applies a speed modifier when breaking the tree. \n" +
			"0 will disable this, so the speed will be the default one of breaking a block. \n" +
			"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs. \n" +
			"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs. \n" +
			"INFO: Only in INSTANTANEOUS mode. \n" +
			"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there.")
	@MinMax(min = 0, max = 50)
	public double speedMultiplicand = 0;
	
	public Collection<Item> getBlacklisted(){
		return ConfigCache.getInstance().getToolsBlacklisted(this::getBlacklistedStr);
	}
	
	private Collection<String> getBlacklistedStr(){
		return blacklisted;
	}
	
	public Collection<Item> getWhitelisted(){
		return ConfigCache.getInstance().getToolsWhitelisted(this::getWhitelistedStr);
	}
	
	private Collection<String> getWhitelistedStr(){
		return whitelisted;
	}
	
	public int getDamageMultiplicand(){
		return this.damageMultiplicand;
	}
	
	public double getSpeedMultiplicand(){
		return this.speedMultiplicand;
	}
	
	public boolean isIgnoreTools(){
		return this.ignoreTools;
	}
	
	public boolean isPreserve(){
		return this.preserve;
	}
}
