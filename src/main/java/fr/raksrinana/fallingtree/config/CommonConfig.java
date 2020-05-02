package fr.raksrinana.fallingtree.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig{
	private final TreeConfiguration trees;
	private final ToolConfiguration tools;
	private final ForgeConfigSpec.BooleanValue reverseSneaking;
	
	public CommonConfig(ForgeConfigSpec.Builder builder){
		builder.comment("Falling Tree configuration");
		builder.push("trees");
		trees = new TreeConfiguration(builder);
		builder.pop();
		builder.push("tools");
		tools = new ToolConfiguration(builder);
		builder.pop();
		reverseSneaking = builder.comment("When set to true, a tree will only be chopped down if the player is sneaking").define("reverse_sneaking", false);
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
}
