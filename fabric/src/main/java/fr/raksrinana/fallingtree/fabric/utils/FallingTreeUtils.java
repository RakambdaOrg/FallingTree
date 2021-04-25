package fr.raksrinana.fallingtree.fabric.utils;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.config.ToolConfiguration;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.AXES;
import static net.minecraft.tags.BlockTags.*;
import static net.minecraft.world.level.block.Blocks.SHROOMLIGHT;

public class FallingTreeUtils{
	public static Set<Item> getAsItems(Collection<? extends String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.filter(val -> !val.isEmpty())
				.flatMap(FallingTreeUtils::getItem)
				.filter(Objects::nonNull)
				.collect(toSet());
	}
	
	public static Stream<Item> getItem(String name){
		try{
			boolean isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			ResourceLocation identifier = new ResourceLocation(name);
			if(isTag){
				return TagRegistry.item(identifier).getValues().stream();
			}
			return Stream.of(Registry.ITEM.get(identifier));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static Set<Block> getAsBlocks(Collection<? extends String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.filter(val -> !val.isEmpty())
				.flatMap(FallingTreeUtils::getBlock)
				.filter(Objects::nonNull)
				.collect(toSet());
	}
	
	public static Stream<Block> getBlock(String name){
		try{
			boolean isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			ResourceLocation identifier = new ResourceLocation(name);
			if(isTag){
				return TagRegistry.block(identifier).getValues().stream();
			}
			return Stream.of(Registry.BLOCK.get(identifier));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static boolean isLeafBlock(Block block){
		boolean isWhitelistedBlock = block.is(LEAVES)
				|| FallingTree.config.getTreesConfiguration().getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			boolean isBlacklistedBlock = FallingTree.config.getTreesConfiguration().getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(Player player){
		ToolConfiguration toolConfiguration = FallingTree.config.getToolsConfiguration();
		Item heldItem = player.getMainHandItem().getItem();
		boolean isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| heldItem.is(AXES)
				|| toolConfiguration.getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			boolean isBlacklistedTool = toolConfiguration.getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
	
	public static TreePartType getTreePart(Block checkBlock){
		if(isLogBlock(checkBlock)){
			return TreePartType.LOG;
		}
		if(isNetherWartOrShroomlight(checkBlock)){
			return TreePartType.NETHER_WART;
		}
		if(isLeafNeedBreakBlock(checkBlock)){
			return TreePartType.LEAF_NEED_BREAK;
		}
		return TreePartType.OTHER;
	}
	
	public static boolean isLeafNeedBreakBlock(Block block){
		return FallingTree.config.getTreesConfiguration()
				.getWhitelistedNonDecayLeaves().stream()
				.anyMatch(log -> log.equals(block));
	}
	
	public static boolean isPlayerInRightState(Player player){
		if(player.isCreative() && !FallingTree.config.isBreakInCreative()){
			return false;
		}
		if(FallingTree.config.isReverseSneaking() != player.isCrouching()){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	public static boolean isLogBlock(Block block){
		boolean isWhitelistedBlock = block.is(LOGS)
				|| FallingTree.config.getTreesConfiguration().getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			boolean isBlacklistedBlock = FallingTree.config.getTreesConfiguration().getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return block.is(WART_BLOCKS) || block.is(SHROOMLIGHT);
	}
}
