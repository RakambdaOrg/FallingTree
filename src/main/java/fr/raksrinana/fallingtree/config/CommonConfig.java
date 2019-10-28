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
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedTools;
	public final ForgeConfigSpec.BooleanValue ignoreDurabilityLoss;
	public final ForgeConfigSpec.BooleanValue preserveTools;
	public final ForgeConfigSpec.BooleanValue reverseSneaking;
	public final ForgeConfigSpec.IntValue maxTreeSize;
	
	
	public CommonConfig(ForgeConfigSpec.Builder builder){
		builder.comment("Falling Tree configuration");
		whitelistedLogs = builder.comment("List of blocks considered as logs and will be destroyed all at once").defineList("logs_whitelisted", Lists.newArrayList("minecraft:acacia_log", "minecraft:birch_log", "minecraft:dark_oak_log", "minecraft:jungle_log", "minecraft:oak_log", "minecraft:spruce_log"), Objects::nonNull);
		whitelistedTools = builder.comment("List of tools that can be used to chop down a tree").defineList("tools_whitelisted", Lists.newArrayList("minecraft:wooden_axe", "minecraft:golden_axe", "minecraft:stone_axe", "minecraft:iron_axe", "minecraft:diamond_axe"), Objects::nonNull);
		ignoreDurabilityLoss = builder.comment("Ignore the durability loss of breaking all the logs. If set to true, no harm will be done to the tool").define("ignore_durability", false);
		maxTreeSize = builder.comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut.").defineInRange("max_log_count", 100, 1, Integer.MAX_VALUE);
		preserveTools = builder.comment("When set to true, when a tree is broken and the tool is about to break we will just break one block and not the whole tree.").define("preserve_tools", false);
		reverseSneaking = builder.comment("When set to true, a tree will only be chopped down if the player is sneaking").define("reverse_sneaking", false);
	}
	
	public Stream<Block> getWhitelistedLogs(){
		return whitelistedLogs.get().stream().map(CommonConfig::getBlock).filter(Objects::nonNull);
	}
	
	public Stream<Item> getWhitelistedTools(){
		return whitelistedTools.get().stream().map(CommonConfig::getItem).filter(Objects::nonNull);
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
