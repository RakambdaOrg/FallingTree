package fr.raksrinana.fallingtree.config;

import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.gui.entries.BooleanListEntry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import static fr.raksrinana.fallingtree.config.Config.toTooltips;

public class CommonConfig{
	private static final String[] DESC_REVERSE_SNEAKING = {
			"When set to true, a tree will,",
			"only be chopped down if the player is sneaking."
	};
	private static final String[] DESC_BREAK_IN_CREATIVE = {
			"When set to true, the mod will,",
			"cut down trees in creative too."
	};
	private final TreeConfiguration trees;
	private final ToolConfiguration tools;
	private final ForgeConfigSpec.BooleanValue reverseSneaking;
	private final ForgeConfigSpec.BooleanValue breakInCreative;
	
	public CommonConfig(ForgeConfigSpec.Builder builder){
		builder.comment("Falling Tree configuration");
		builder.push("trees");
		trees = new TreeConfiguration(builder);
		builder.pop();
		builder.push("tools");
		tools = new ToolConfiguration(builder);
		builder.pop();
		reverseSneaking = builder.comment(DESC_REVERSE_SNEAKING).define("reverse_sneaking", false);
		breakInCreative = builder.comment(DESC_BREAK_IN_CREATIVE).define("break_in_creative", false);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void fillConfigScreen(ConfigBuilder builder){
		BooleanListEntry reverseSneakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.reverseSneaking"), isReverseSneaking())
				.setDefaultValue(false)
				.setTooltip(toTooltips(DESC_REVERSE_SNEAKING))
				.setSaveConsumer(reverseSneaking::set)
				.build();
		BooleanListEntry breakInCreativeEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.breakInCreative"), isBreakInCreative())
				.setDefaultValue(false)
				.setTooltip(toTooltips(DESC_BREAK_IN_CREATIVE))
				.setSaveConsumer(breakInCreative::set)
				.build();
		
		ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("text.autoconfig.fallingtree.category.default"));
		general.addEntry(reverseSneakingEntry);
		general.addEntry(breakInCreativeEntry);
		
		trees.fillConfigScreen(builder);
		tools.fillConfigScreen(builder);
	}
	
	public ToolConfiguration getToolsConfiguration(){
		return this.tools;
	}
	
	public TreeConfiguration getTreesConfiguration(){
		return this.trees;
	}
	
	public boolean isReverseSneaking(){
		return this.reverseSneaking.get();
	}
	
	public boolean isBreakInCreative(){
		return this.breakInCreative.get();
	}
}
