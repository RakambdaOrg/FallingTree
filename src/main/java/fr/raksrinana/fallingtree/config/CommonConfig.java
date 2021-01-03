package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = FallingTree.MOD_ID)
public class CommonConfig{
	@Name("reverse_sneaking")
	@Comment("When set to true, a tree will only be chopped down if the player is sneaking.")
	public static boolean reverseSneaking = false;
	@Name("break_in_creative")
	@Comment("When set to true, the mod will cut down trees in creative too.")
	public static boolean breakInCreative = false;
	
	public static boolean isReverseSneaking(){
		return CommonConfig.reverseSneaking;
	}
	
	public static boolean isBreakInCreative(){
		return CommonConfig.breakInCreative;
	}
}
