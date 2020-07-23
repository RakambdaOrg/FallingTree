package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static fr.raksrinana.fallingtree.FallingTreeUtils.getAsItems;

public class ToolConfiguration{
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelisted;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklisted;
	private final ForgeConfigSpec.BooleanValue preserve;
	private final ForgeConfigSpec.IntValue damageMultiplicand;
	
	public ToolConfiguration(ForgeConfigSpec.Builder builder){
		whitelisted = builder.comment("Additional list of tools (those marked with the axe tag will already be whitelisted) that can be used to chop down a tree").defineList("whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklisted = builder.comment("List of tools that should not be considered as tools (this wins over the whitelist)").defineList("blacklisted", Lists.newArrayList(), Objects::nonNull);
		preserve = builder.comment("When set to true, when a tree is broken and the tool is about to break we will just break one block and not the whole tree.").define("preserve", false);
		damageMultiplicand = builder.comment("Defines the number of times the damage is applied to the tool (ie: if 1 then breaking 5 logs will give 5 damage; if set to 2, breaking 5 logs will give 10 damage). This only applies when the tree is cut when using the mod.").defineInRange("damage_multiplicand", 1, 1, Integer.MAX_VALUE);
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
	
	public int getDamageMultiplicand(){
		return this.damageMultiplicand.get();
	}
}
