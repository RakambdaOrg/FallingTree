package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.StringListListEntry;
import net.minecraft.item.Item;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsItems;

public class ToolConfiguration{
	private static final String[] DESC_IGNORE_TOOLS = {
			"When set to true, the mod will be activated no matter what you have in your hand (or empty hand).",
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
	private static final String[] DESC_SPEED_MULTIPLICAND = {
			"Applies a speed modifier when breaking the tree.",
			"0 will disable this, so the speed will be the default one of breaking a block.",
			"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs.",
			"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs",
			"INFO: Only in INSTANTANEOUS mode.",
			"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there."
	};
	private static final String DESC_PRESERVE = "When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability.";
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelisted;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklisted;
	private final ForgeConfigSpec.BooleanValue preserve;
	private final ForgeConfigSpec.BooleanValue ignoreTools;
	private final ForgeConfigSpec.IntValue damageMultiplicand;
	private final ForgeConfigSpec.DoubleValue speedMultiplicand;
	
	public ToolConfiguration(ForgeConfigSpec.Builder builder){
		ignoreTools = builder.comment(DESC_IGNORE_TOOLS)
				.define("ignore_tools", false);
		whitelisted = builder.comment(DESC_WHITELISTED)
				.defineList("whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklisted = builder.comment(DESC_BLACKLISTED)
				.defineList("blacklisted", Lists.newArrayList(), Objects::nonNull);
		damageMultiplicand = builder.comment(DESC_DAMAGE_MULTIPLICAND)
				.defineInRange("damage_multiplicand", 1, 0, Integer.MAX_VALUE);
		speedMultiplicand = builder.comment(DESC_SPEED_MULTIPLICAND)
				.defineInRange("speed_multiplicand", 0d, 0d, 50d);
		preserve = builder.comment(DESC_PRESERVE)
				.define("preserve", false);
	}
	
	public void fillConfigScreen(ConfigBuilder builder){
		BooleanListEntry ignoreToolsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.tools.ignoreTools"), isIgnoreTools())
				.setDefaultValue(false)
				.setTooltip(new StringTextComponent(String.join("\n", DESC_IGNORE_TOOLS)))
				.setSaveConsumer(ignoreTools::set)
				.build();
		StringListListEntry whitelistedEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent("text.autoconfig.fallingtree.option.tools.whitelisted"), (List<String>) whitelisted.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(new StringTextComponent(String.join("\n", DESC_WHITELISTED)))
				.setSaveConsumer(whitelisted::set)
				.build();
		StringListListEntry blacklistedEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent("text.autoconfig.fallingtree.option.tools.blacklisted"), (List<String>) blacklisted.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(new StringTextComponent(String.join("\n", DESC_BLACKLISTED)))
				.setSaveConsumer(blacklisted::set)
				.build();
		IntegerListEntry damageMultiplicandEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent("text.autoconfig.fallingtree.option.tools.damageMultiplicand"), getDamageMultiplicand())
				.setDefaultValue(1)
				.setMin(0)
				.setTooltip(new StringTextComponent(String.join("\n", DESC_DAMAGE_MULTIPLICAND)))
				.setSaveConsumer(damageMultiplicand::set)
				.build();
		DoubleListEntry speedMultiplicandEntry = builder.entryBuilder()
				.startDoubleField(new TranslationTextComponent("text.autoconfig.fallingtree.option.tools.speedMultiplicand"), getSpeedMultiplicand())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(50)
				.setTooltip(new StringTextComponent(String.join("\n", DESC_SPEED_MULTIPLICAND)))
				.setSaveConsumer(speedMultiplicand::set)
				.build();
		BooleanListEntry preserveEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.tools.preserve"), isPreserve())
				.setDefaultValue(false)
				.setTooltip(new StringTextComponent(String.join("\n", DESC_PRESERVE)))
				.setSaveConsumer(preserve::set)
				.build();
		
		ConfigCategory tools = builder.getOrCreateCategory(new TranslationTextComponent("text.autoconfig.fallingtree.category.tools"));
		tools.addEntry(ignoreToolsEntry);
		tools.addEntry(whitelistedEntry);
		tools.addEntry(blacklistedEntry);
		tools.addEntry(damageMultiplicandEntry);
		tools.addEntry(speedMultiplicandEntry);
		tools.addEntry(preserveEntry);
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
