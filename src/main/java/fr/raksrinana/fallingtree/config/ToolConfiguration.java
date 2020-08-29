package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.config.validator.Max;
import fr.raksrinana.fallingtree.config.validator.Min;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static fr.raksrinana.fallingtree.FallingTreeUtils.getAsItems;

@SuppressWarnings("FieldCanBeLocal")
public class ToolConfiguration{
	@Comment("Additional list of tools that can be used to chop down a tree. " +
			"INFO: Items marked with the axe tag will already be whitelisted.")
	public List<String> whitelisted = new ArrayList<>();
	@Comment("List of tools that should not be considered as tools. " +
			"INFO: This wins over the whitelist.")
	public List<String> blacklisted = new ArrayList<>();
	@Comment("When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability.")
	public boolean preserve = false;
	@Comment("When set to true, the mod will be activated no matter what you have in your hand (or empty hand). " +
			"INFO: Blacklist still can be use to restrict some tools.")
	public boolean ignoreTools = false;
	@Comment("Defines the number of times the damage is applied to the tool. " +
			"ie: if set to 1 then breaking 5 logs will give 5 damage. " +
			"ie: if set to 2 then breaking 5 logs will give 10 damage. " +
			"If set to 0, it'll still apply 1 damage for every cut. " +
			"INFO: This only applies when the tree is cut when using the mod.")
	@Min(0)
	public int damageMultiplicand = 1;
	@Comment("Applies a speed modifier when breaking the tree. " +
			"0 will disable this, so the speed will be the default one of breaking a block. " +
			"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs. " +
			"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs. " +
			"INFO: Only in INSTANTANEOUS mode. " +
			"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there.")
	@Min(0)
	@Max(50)
	public double speedMultiplicand = 0;
	
	public Collection<Item> getBlacklisted(){
		return getAsItems(blacklisted);
	}
	
	public int getDamageMultiplicand(){
		return this.damageMultiplicand;
	}
	
	public double getSpeedMultiplicand(){
		return this.speedMultiplicand;
	}
	
	public Collection<Item> getWhitelisted(){
		return getAsItems(whitelisted);
	}
	
	public boolean isIgnoreTools(){
		return this.ignoreTools;
	}
	
	public boolean isPreserve(){
		return this.preserve;
	}
}
