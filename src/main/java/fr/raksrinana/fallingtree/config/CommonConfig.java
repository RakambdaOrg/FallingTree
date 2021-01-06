package fr.raksrinana.fallingtree.config;

import net.minecraftforge.common.ForgeConfigSpec;

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
	
	public void setBreakInCreative(Boolean value){
		breakInCreative.set(value);
	}
	
	public void setReverseSneaking(Boolean value){
		reverseSneaking.set(value);
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
