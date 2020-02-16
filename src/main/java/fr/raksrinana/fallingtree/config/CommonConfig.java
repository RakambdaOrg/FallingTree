package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CommonConfig{
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedLogs;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedLogs;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedTools;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedTools;
	public final ForgeConfigSpec.BooleanValue ignoreDurabilityLoss;
	public final ForgeConfigSpec.BooleanValue preserveTools;
	public final ForgeConfigSpec.BooleanValue reverseSneaking;
	public final ForgeConfigSpec.BooleanValue breakLeaves;
	public final ForgeConfigSpec.IntValue maxTreeSize;
	public final ForgeConfigSpec.IntValue forceBreakLeavesRadius;
	
	
	public CommonConfig(ForgeConfigSpec.Builder builder){
		builder.comment("Falling Tree configuration");
		whitelistedLogs = builder.comment("Additional list of blocks (those marked with the log tag will already be whitelisted) considered as logs and that will be destroyed all at once").defineList("logs_whitelisted", Lists.newArrayList(), Objects::nonNull);
		whitelistedTools = builder.comment("Additional list of tools (those marked with the axe tag will already be whitelisted) that can be used to chop down a tree").defineList("tools_whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklistedLogs = builder.comment("List of blocks that should not be considered as logs (this wins over the whitelist)").defineList("logs_blacklisted", Lists.newArrayList(), Objects::nonNull);
		blacklistedTools = builder.comment("List of tools that should not be considered as tools (this wins over the whitelist)").defineList("tools_blacklisted", Lists.newArrayList(), Objects::nonNull);
		ignoreDurabilityLoss = builder.comment("Ignore the durability loss of breaking all the logs. If set to true, no harm will be done to the tool").define("ignore_durability", false);
		maxTreeSize = builder.comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut.").defineInRange("max_log_count", 100, 1, Integer.MAX_VALUE);
		preserveTools = builder.comment("When set to true, when a tree is broken and the tool is about to break we will just break one block and not the whole tree.").define("preserve_tools", false);
		reverseSneaking = builder.comment("When set to true, a tree will only be chopped down if the player is sneaking").define("reverse_sneaking", false);
		breakLeaves = builder.comment("When set to true, leaves that should naturally break will be broken instantly").define("break_leaves", false);
		forceBreakLeavesRadius = builder.comment("Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed. The radius is applied from one of the top most log blocks. break_leaves must be activated for this to take effect.").defineInRange("force_break_leaves_radius", 0, 0, 10);
	}
	
	public Stream<Block> getWhitelistedLogs(){
		return whitelistedLogs.get().stream().map(CommonConfig::getBlock).filter(Objects::nonNull);
	}
	
	public Stream<Item> getWhitelistedTools(){
		return whitelistedTools.get().stream().map(CommonConfig::getItem).filter(Objects::nonNull);
	}
	
	public Stream<Block> getBlacklistedLogs(){
		return blacklistedLogs.get().stream().map(CommonConfig::getBlock).filter(Objects::nonNull);
	}
	
	public Stream<Item> getBlacklistedTools(){
		return blacklistedTools.get().stream().map(CommonConfig::getItem).filter(Objects::nonNull);
	}
	
	@Nullable
	private static Block getBlock(String name){
		try{
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
		}
		catch(Exception e){
			return null;
		}
	}
	
	@Nullable
	private static Item getItem(String name){
		try{
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
		}
		catch(Exception e){
			return null;
		}
	}
}
