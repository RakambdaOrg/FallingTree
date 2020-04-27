package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Config(modid = FallingTree.MOD_ID, category = "tools")
public class ToolConfiguration{
	@Config.Name("whitelisted")
	@Config.Comment("Additional list of tools (those marked with the axe tag will already be whitelisted) that can be used to chop down a tree")
	public static String[] whitelisted = {};
	@Config.Name("blacklisted")
	@Config.Comment("List of tools that should not be considered as tools (this wins over the whitelist)")
	public static  String[] blacklisted = {};
	@Config.Name("ignore_durability")
	@Config.Comment("Ignore the durability loss of breaking all the logs. If set to true, no harm will be done to the tool")
	public static  boolean ignoreDurabilityLoss = false;
	@Config.Name("preserve")
	@Config.Comment("When set to true, when a tree is broken and the tool is about to break we will just break one block and not the whole tree.")
	public static  boolean preserve = false;
	
	public static Stream<Item> getBlacklisted(){
		return Arrays.stream(blacklisted).map(CommonConfig::getItem).filter(Objects::nonNull);
	}
	
	public static Stream<Item> getWhitelisted(){
		return Arrays.stream(whitelisted).map(CommonConfig::getItem).filter(Objects::nonNull);
	}
	
	public static boolean isIgnoreDurabilityLoss(){
		return ignoreDurabilityLoss;
	}
	
	public static boolean isPreserve(){
		return preserve;
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
