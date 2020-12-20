package fr.raksrinana.fallingtree.config;

import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.gui.entries.BooleanListEntry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.LinkedList;
import java.util.List;

public class CommonConfig{
	private static final String[] DESC_REVERSE_SNEAKING = {
			"When set to true, a tree will, only be chopped down if the player is sneaking."
	};
	private static final String[] DESC_BREAK_IN_CREATIVE = {
			"When set to true, the mod will, cut down trees in creative too."
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
				.startBooleanToggle(new TranslationTextComponent(getFieldName("reverseSneaking")), isReverseSneaking())
				.setDefaultValue(false)
				.setTooltip(getTooltips("reverseSneaking", 2))
				.setSaveConsumer(reverseSneaking::set)
				.build();
		BooleanListEntry breakInCreativeEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("breakInCreative")), isBreakInCreative())
				.setDefaultValue(false)
				.setTooltip(getTooltips("breakInCreative", 2))
				.setSaveConsumer(breakInCreative::set)
				.build();
		
		ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("text.autoconfig.fallingtree.category.default"));
		general.addEntry(reverseSneakingEntry);
		general.addEntry(breakInCreativeEntry);
		
		trees.fillConfigScreen(builder);
		tools.fillConfigScreen(builder);
	}
	
	private String getFieldName(String fieldName){
		return "text.autoconfig.fallingtree.option." + fieldName;
	}
	
	private ITextComponent[] getTooltips(String fieldName, int count){
		String tooltipKey = getFieldName(fieldName) + ".@Tooltip";
		List<String> keys = new LinkedList<>();
		if(count <= 1){
			keys.add(tooltipKey);
		}
		else{
			for(int i = 0; i < count; i++){
				keys.add(tooltipKey + "[" + i + "]");
			}
		}
		return keys.stream()
				.map(TranslationTextComponent::new)
				.toArray(ITextComponent[]::new);
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
