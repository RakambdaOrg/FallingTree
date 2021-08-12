package fr.raksrinana.fallingtree.forge.config;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
public class ToolConfiguration{
	private static final String[] DESC_IGNORE_TOOLS = {
			"When set to true, the mod will be activated no matter what you have in your hand.",
			"INFO: Blacklist still can be use to restrict some tools."
	};
	private static final String[] DESC_WHITELISTED = {
			"Additional list of tools that can be used to chop down a tree.",
			"INFO: Items marked with the axe tag will already be whitelisted."
	};
	private static final String[] DESC_BLACKLISTED = {
			"List of tools that should not be considered as tools.",
			"INFO: This wins over the whitelist."
	};
	private static final String[] DESC_DAMAGE_MULTIPLICAND = {
			"Defines the number of times the damage is applied to the tool.",
			"ie: if set to 1 then breaking 5 logs will give 5 damage.",
			"ie: if set to 2 then breaking 5 logs will give 10 damage.",
			"If set to 0, it'll still apply 1 damage for every cut.",
			"INFO: This only applies when the tree is cut when using the mod."
	};
	private static final String[] DESC_DAMAGE_ROUNDING = {
			"How damage taken should be rounded if it isn't a whole number.",
			"ROUNDING will round to the closest whole number.",
			"ROUND_DOWN will round down.",
			"ROUND_UP will round up.",
			"PROBABILISTIC will treat decimal fraction as a probability of rounding up.",
			"ie: 9.45 will have 45% chance of being rounded up to 10 and 55% chance of being rounded down to 9."
	};
	private static final String[] DESC_SPEED_MULTIPLICAND = {
			"Applies a speed modifier when breaking the tree.",
			"0 will disable this, so the speed will be the default one of breaking a block.",
			"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs.",
			"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs",
			"INFO: Only in INSTANTANEOUS mode.",
			"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there."
	};
	private static final String[] DESC_PRESERVE = {
			"When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability.",
			"INFO: Only in instantaneous mode"
	};
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelisted;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklisted;
	private final ForgeConfigSpec.BooleanValue preserve;
	private final ForgeConfigSpec.BooleanValue ignoreTools;
	private final ForgeConfigSpec.DoubleValue damageMultiplicand;
	private final ForgeConfigSpec.EnumValue<DamageRounding> damageRounding;
	private final ForgeConfigSpec.DoubleValue speedMultiplicand;
	
	public ToolConfiguration(ForgeConfigSpec.Builder builder){
		ignoreTools = builder.comment(DESC_IGNORE_TOOLS)
				.define("ignore_tools", false);
		whitelisted = builder.comment(DESC_WHITELISTED)
				.defineList("whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklisted = builder.comment(DESC_BLACKLISTED)
				.defineList("blacklisted", Lists.newArrayList(), Objects::nonNull);
		damageMultiplicand = builder.comment(DESC_DAMAGE_MULTIPLICAND)
				.defineInRange("damage_multiplicand", 1d, 0d, 100d);
		damageRounding = builder.comment(DESC_DAMAGE_ROUNDING)
				.defineEnum("damage_rounding", DamageRounding.ROUND_DOWN);
		speedMultiplicand = builder.comment(DESC_SPEED_MULTIPLICAND)
				.defineInRange("speed_multiplicand", 0d, 0d, 50d);
		preserve = builder.comment(DESC_PRESERVE)
				.define("preserve", false);
	}
	
	public Collection<Item> getBlacklistedItems(){
		return ConfigCache.getInstance().getToolsBlacklisted(this::getBlacklisted);
	}
	
	public Collection<Item> getWhitelistedItems(){
		return ConfigCache.getInstance().getToolsWhitelisted(this::getWhitelisted);
	}
	
	public List<String> getBlacklisted(){
		return (List<String>) blacklisted.get();
	}
	
	public List<String> getWhitelisted(){
		return (List<String>) whitelisted.get();
	}
	
	public void setBlacklisted(List<String> value){
		blacklisted.set(value);
	}
	
	public void setDamageMultiplicand(Double value){
		damageMultiplicand.set(value);
	}
	
	public void setDamageRounding(DamageRounding value){
		damageRounding.set(value);
	}
	
	public void setIgnoreTools(Boolean value){
		ignoreTools.set(value);
	}
	
	public void setPreserve(Boolean value){
		preserve.set(value);
	}
	
	public void setSpeedMultiplicand(Double value){
		speedMultiplicand.set(value);
	}
	
	public void setWhitelisted(List<String> value){
		whitelisted.set(value);
	}
	
	public boolean isPreserve(){
		return preserve.get();
	}
	
	public boolean isIgnoreTools(){
		return ignoreTools.get();
	}
	
	public double getDamageMultiplicand(){
		return damageMultiplicand.get();
	}
	
	public DamageRounding getDamageRounding(){
		return damageRounding.get();
	}
	
	public double getSpeedMultiplicand(){
		return speedMultiplicand.get();
	}
}
