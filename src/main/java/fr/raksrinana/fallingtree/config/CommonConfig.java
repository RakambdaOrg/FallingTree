package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import javax.tools.Tool;
import java.util.Collection;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsItems;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
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
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.getModID().equals(FallingTree.MOD_ID)){
			ConfigManager.sync(FallingTree.MOD_ID, Config.Type.INSTANCE);
		}
	}
}
