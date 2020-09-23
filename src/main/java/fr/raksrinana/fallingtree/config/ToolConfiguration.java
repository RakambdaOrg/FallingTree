package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.ConfigManager;
import java.util.Collection;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsItems;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
@Config(modid = FallingTree.MOD_ID, category = "tools")
public class ToolConfiguration{
	@Config.Name("whitelisted")
	@Config.Comment({
			"Additional list of tools that can be used to chop down a tree.",
			"INFO: Items marked with the axe tag will already be whitelisted."
	})
	public static String[] whitelisted = {};
	@Config.Name("blacklisted")
	@Config.Comment({
			"List of tools that should not be considered as tools.",
			"INFO: This wins over the whitelist."
	})
	public static String[] blacklisted = {};
	@Config.Name("preserve")
	@Config.Comment({
			"When set to true, when a tree is broken and the tool is about to break we will just break enough blocks so that the tool is left at 1 of durability."
	})
	public static boolean preserve = false;
	@Config.Name("ignore_tools")
	@Config.Comment({
			"When set to true, the mod will be activated no matter what you have in your hand (or empty hand).",
			"INFO: Blacklist still can be use to restrict some tools."
	})
	public static boolean ignoreTools = false;
	@Config.Name("damage_multiplicand")
	@Config.Comment({
			"Defines the number of times the damage is applied to the tool.",
			"ie: if set to 1 then breaking 5 logs will give 5 damage.",
			"ie: if set to 2 then breaking 5 logs will give 10 damage.",
			"If set to 0, it'll still apply 1 damage for every cut.",
			"INFO: This only applies when the tree is cut when using the mod."
	})
	@Config.RangeInt(min = 0)
	public static int damageMultiplicand = 1;
	@Config.Name("speed_multiplicand")
	@Config.Comment({
			"Applies a speed modifier when breaking the tree.",
			"0 will disable this, so the speed will be the default one of breaking a block.",
			"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs.",
			"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs",
			"INFO: Only in INSTANTANEOUS mode.",
			"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there."
	})
	@Config.RangeDouble(min = 0, max = 50)
	public static double speedMultiplicand = 0;
	
	public static Collection<Item> getBlacklisted(){
		return getAsItems(blacklisted);
	}
	
	public static Collection<Item> getWhitelisted(){
		return getAsItems(whitelisted);
	}
	
	public static boolean isPreserve(){
		return preserve;
	}
	
	public static int getDamageMultiplicand(){
		return damageMultiplicand;
	}
	
	public static boolean isIgnoreTools(){
		return ignoreTools;
	}
	
	public static double getSpeedMultiplicand(){
		return speedMultiplicand;
	}
	
	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event){
		if(event.getModID().equals(FallingTree.MOD_ID)){
			ConfigManager.sync(FallingTree.MOD_ID, Config.Type.INSTANCE);
		}
	}
}
