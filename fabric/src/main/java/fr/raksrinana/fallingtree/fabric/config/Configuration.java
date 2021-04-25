package fr.raksrinana.fallingtree.fabric.config;

import fr.raksrinana.fallingtree.fabric.config.validator.Validators;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.world.InteractionResult;

@Config(name = "fallingtree")
public class Configuration implements ConfigData{
	@Tooltip(count = 2)
	@Comment("When set to true, a tree will only be chopped down if the player is sneaking.")
	public boolean reverseSneaking = false;
	@Tooltip(count = 2)
	@Comment("When set to true, the mod will cut down trees in creative too.")
	public boolean breakInCreative = false;
	@Category("trees")
	@TransitiveObject
	public TreeConfiguration trees = new TreeConfiguration();
	@Category("tools")
	@TransitiveObject
	public ToolConfiguration tools = new ToolConfiguration();
	
	public static Configuration register(){
		ConfigHolder<Configuration> configHolder = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		configHolder.registerSaveListener((configHolder1, configuration) -> {
			ConfigCache.getInstance().invalidate();
			return InteractionResult.PASS;
		});
		configHolder.registerLoadListener((configHolder1, configuration) -> {
			ConfigCache.getInstance().invalidate();
			return InteractionResult.PASS;
		});
		
		return configHolder.getConfig();
	}
	
	@Override
	public void validatePostLoad() throws ValidationException{
		Validators.runValidators(Configuration.class, this, "general");
		Validators.runValidators(ToolConfiguration.class, tools, "tools");
		Validators.runValidators(TreeConfiguration.class, trees, "trees");
	}
	
	public ToolConfiguration getToolsConfiguration(){
		return tools;
	}
	
	public TreeConfiguration getTreesConfiguration(){
		return trees;
	}
	
	public boolean isBreakInCreative(){
		return breakInCreative;
	}
	
	public boolean isReverseSneaking(){
		return reverseSneaking;
	}
}
