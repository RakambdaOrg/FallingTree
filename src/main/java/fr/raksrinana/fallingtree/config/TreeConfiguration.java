package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Config(modid = FallingTree.MOD_ID, category = "trees")
public class TreeConfiguration{
	@Config.Name("logs_whitelisted")
	@Config.Comment("Additional list of blocks (those marked with the log tag will already be whitelisted) considered as logs and that will be destroyed all at once")
	public static String[] whitelistedLogs = {};
	@Config.Name("logs_blacklisted")
	@Config.Comment("List of blocks that should not be considered as logs (this wins over the whitelist)")
	public static String[] blacklistedLogs = {};
	@Config.Name("logs_max_count")
	@Config.Comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut.")
	@Config.RangeInt(min = 1)
	public static int maxSize = 100;
	@Config.Name("leaves_breaking")
	@Config.Comment("When set to true, leaves that should naturally break will be broken instantly")
	public static boolean lavesBreaking = false;
	@Config.Name("leaves_breaking_force_radius")
	@Config.Comment("Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed. The radius is applied from one of the top most log blocks. break_leaves must be activated for this to take effect.")
	@Config.RangeInt(min = 0, max = 10)
	public static int lavesBreakingForceRadius = 0;
	
	public static Stream<Block> getBlacklistedLogs(){
		return Arrays.stream(blacklistedLogs).map(CommonConfig::getBlock).filter(Objects::nonNull);
	}
	
	public static int getLavesBreakingForceRadius(){
		return lavesBreakingForceRadius;
	}
	
	public static int getMaxSize(){
		return maxSize;
	}
	
	public static Stream<Block> getWhitelistedLogs(){
		return Arrays.stream(whitelistedLogs).map(CommonConfig::getBlock).filter(Objects::nonNull);
	}
	
	public static boolean isLavesBreaking(){
		return lavesBreaking;
	}
	
	@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
	private static class Handler{
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
			if(event.getModID().equals(FallingTree.MOD_ID)){
				ConfigManager.sync(FallingTree.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
