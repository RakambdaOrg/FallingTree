package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsItems;

public class ToolConfiguration{
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelisted;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklisted;
	private final ForgeConfigSpec.BooleanValue preserve;
	private final ForgeConfigSpec.BooleanValue ignoreTools;
	private final ForgeConfigSpec.IntValue damageMultiplicand;
	private final ForgeConfigSpec.DoubleValue speedMultiplicand;
	
	public ToolConfiguration(ForgeConfigSpec.Builder builder){
		ignoreTools = builder.comment("When set to true, the mod will be activated no matter what you have in your hand (or empty hand).",
				"INFO: Blacklist still can be use to restrict some tools.")
				.define("ignore_tools", false);
		whitelisted = builder.comment("Additional list of tools that can be used to chop down a tree.",
				"INFO: Items marked with the axe tag will already be whitelisted.")
				.defineList("whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklisted = builder.comment("List of tools that should not be considered as tools.",
				"INFO: This wins over the whitelist.")
				.defineList("blacklisted", Lists.newArrayList(), Objects::nonNull);
		damageMultiplicand = builder.comment("Defines the number of times the damage is applied to the tool.",
				"ie: if set to 1 then breaking 5 logs will give 5 damage.",
				"ie: if set to 2 then breaking 5 logs will give 10 damage.",
				"If set to 0, it'll still apply 1 damage for every cut.",
				"INFO: This only applies when the tree is cut when using the mod.")
				.defineInRange("damage_multiplicand", 1, 0, Integer.MAX_VALUE);
		speedMultiplicand = builder.comment("Applies a speed modifier when breaking the tree.",
				"0 will disable this, so the speed will be the default one of breaking a block.",
				"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs.",
				"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs",
				"INFO: Only in INSTANTANEOUS mode.",
				"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there.")
				.defineInRange("speed_multiplicand", 0d, 0d, 50d);
		preserve = builder.comment("When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability.")
				.define("preserve", false);
	}
	
	public Collection<Item> getBlacklisted(){
		return getAsItems(blacklisted.get());
	}
	
	public Collection<Item> getWhitelisted(){
		return getAsItems(whitelisted.get());
	}
	
	public boolean isPreserve(){
		return this.preserve.get();
	}
	
	public boolean isIgnoreTools(){
		return this.ignoreTools.get();
	}
	
	public int getDamageMultiplicand(){
		return this.damageMultiplicand.get();
	}
	
	public double getSpeedMultiplicand(){
		return this.speedMultiplicand.get();
	}
}
