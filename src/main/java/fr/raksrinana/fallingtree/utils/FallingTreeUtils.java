package fr.raksrinana.fallingtree.utils;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.utils.TreePartType.*;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.AXES;
import static net.minecraft.block.Blocks.SHROOMLIGHT;
import static net.minecraft.tag.BlockTags.*;
import static net.minecraft.util.registry.Registry.BLOCK;
import static net.minecraft.util.registry.Registry.ITEM;

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
			Identifier identifier = new Identifier(name);
			if(isTag){
				return TagRegistry.item(identifier).values().stream();
			}
			return Stream.of(ITEM.get(identifier));
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
			Identifier identifier = new Identifier(name);
			if(isTag){
				return TagRegistry.block(identifier).values().stream();
			}
			return Stream.of(BLOCK.get(identifier));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static boolean isLeafBlock(Block block){
		boolean isWhitelistedBlock = block.isIn(LEAVES)
				|| FallingTree.config.getTreesConfiguration().getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			boolean isBlacklistedBlock = FallingTree.config.getTreesConfiguration().getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(PlayerEntity player){
		ToolConfiguration toolConfiguration = FallingTree.config.getToolsConfiguration();
		Item heldItem = player.getMainHandStack().getItem();
		boolean isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| heldItem.isIn(AXES)
				|| toolConfiguration.getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			boolean isBlacklistedTool = toolConfiguration.getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
	
	public static TreePartType getTreePart(Block checkBlock){
		if(isLogBlock(checkBlock)){
			return LOG;
		}
		if(isNetherWartOrShroomlight(checkBlock)){
			return NETHER_WART;
		}
		if(isLeafNeedBreakBlock(checkBlock)){
			return LEAF_NEED_BREAK;
		}
		return OTHER;
	}
	
	public static boolean isLeafNeedBreakBlock(Block block){
		return FallingTree.config.getTreesConfiguration()
				.getWhitelistedNonDecayLeaves().stream()
				.anyMatch(log -> log.equals(block));
	}
	
	public static boolean isPlayerInRightState(PlayerEntity player){
		if(player.abilities.creativeMode && !FallingTree.config.isBreakInCreative()){
			return false;
		}
		if(FallingTree.config.isReverseSneaking() != player.isSneaking()){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	public static boolean isLogBlock(Block block){
		boolean isWhitelistedBlock = block.isIn(LOGS)
				|| FallingTree.config.getTreesConfiguration().getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			boolean isBlacklistedBlock = FallingTree.config.getTreesConfiguration().getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return block.isIn(WART_BLOCKS) || block.is(SHROOMLIGHT);
	}
}
