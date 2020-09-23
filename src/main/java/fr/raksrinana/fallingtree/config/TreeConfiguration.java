package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.ConfigManager;
import java.util.Collection;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
@Config(modid = FallingTree.MOD_ID, category = "trees")
public class TreeConfiguration{
	@Config.Name("logs_whitelisted")
	@Config.Comment({
			"Additional list of blocks considered as logs and that will be destroyed by the mod.",
			"INFO: Blocks marked with the log tag will already be whitelisted."
	})
	public static String[] whitelistedLogs = {};
	@Config.Name("logs_blacklisted")
	@Config.Comment({
			"List of blocks that should not be considered as logs.",
			"INFO: This wins over the whitelist."
	})
	public static String[] blacklistedLogs = {};
	@Config.Name("leaves_whitelisted")
	@Config.Comment({
			"Additional list of blocks considered as leaves.",
			"INFO: Blocks marked with the leaves tag will already be whitelisted."
	})
	public static String[] whitelistedLeaves = {};
	@Config.Name("leaves_blacklisted")
	@Config.Comment({
			"List of blocks that should not be considered as leaves.",
			"INFO: This wins over the whitelist."
	})
	public static String[] blacklistedLeaves = {};
	@Config.Name("break_mode")
	@Config.Comment({
			"How to break the tree.",
			"Instantaneous will break it in one go.",
			"Shift down will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them."
	})
	public static BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Config.Name("logs_max_count")
	@Config.Comment({
			"The maximum size of a tree. If there's more logs than this value the tree won't be cut.",
			"INFO: Only in INSTANTANEOUS mode."
	})
	@Config.RangeInt(min = 1)
	public static int maxSize = 100;
	@Config.Name("minimum_leaves_around_required")
	@Config.Comment({
			"The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree.",
			"INFO: Only in INSTANTANEOUS mode."
	})
	@Config.RangeInt(min = 0, max = 5)
	public static int minimumLeavesAroundRequired = 0;
	@Config.Name("leaves_breaking")
	@Config.Comment({
			"When set to true, leaves that should naturally break will be broken instantly."
	})
	public static boolean leavesBreaking = true;
	@Config.Name("leaves_breaking_force_radius")
	@Config.Comment({
			"Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed.",
			"The radius is applied from one of the top most log blocks.",
			"INFO: break_leaves must be activated for this to take effect.",
			"INFO: Only in INSTANTANEOUS mode."
	})
	@Config.RangeInt(min = 0, max = 10)
	public static int leavesBreakingForceRadius = 0;
	@Config.Name("allow_mixed_logs")
	@Config.Comment({
			"When set to true this allow to have any kind of log in a tree trunk.",
			"Otherwise (false) the trunk will be considered as being only one kind of log."
	})
	public static boolean allowMixedLogs = false;
	
	public static Collection<Block> getBlacklistedLeaves(){
		return getAsBlocks(blacklistedLeaves);
	}
	
	public static Collection<Block> getBlacklistedLogs(){
		return getAsBlocks(blacklistedLogs);
	}
	
	public static int getLeavesBreakingForceRadius(){
		return leavesBreakingForceRadius;
	}
	
	public static int getMaxSize(){
		return maxSize;
	}
	
	public static int getMinimumLeavesAroundRequired(){
		return minimumLeavesAroundRequired;
	}
	
	public static Collection<Block> getWhitelistedLeaves(){
		return getAsBlocks(whitelistedLeaves);
	}
	
	public static Collection<Block> getWhitelistedLogs(){
		return getAsBlocks(whitelistedLogs);
	}
	
	public static boolean isLeavesBreaking(){
		return leavesBreaking;
	}
	
	public static BreakMode getBreakMode(){
		return breakMode;
	}
	
	public static boolean isAllowMixedLogs(){
		return allowMixedLogs;
	}
	
	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event){
		if(event.getModID().equals(FallingTree.MOD_ID)){
			ConfigManager.sync(FallingTree.MOD_ID, Config.Type.INSTANCE);
		}
	}
}
