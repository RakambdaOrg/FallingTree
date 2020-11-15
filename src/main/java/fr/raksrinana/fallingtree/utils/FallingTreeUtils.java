package fr.raksrinana.fallingtree.utils;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FallingTreeUtils{
	public static Set<Item> getAsItems(Collection<? extends String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.flatMap(FallingTreeUtils::getItem)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
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
			return Stream.of(Registry.ITEM.get(identifier));
		}
		catch(Exception e){
			return Stream.empty();
		}
	}
	
	public static Set<Block> getAsBlocks(Collection<? extends String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.flatMap(FallingTreeUtils::getBlock)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
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
			return Stream.of(Registry.BLOCK.get(identifier));
		}
		catch(Exception e){
			return Stream.empty();
		}
	}
	
	public static boolean isLogBlock(Block block){
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LOGS)
				|| FallingTree.config.getTreesConfiguration().getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = FallingTree.config.getTreesConfiguration().getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return block.isIn(BlockTags.WART_BLOCKS) || block.is(Blocks.SHROOMLIGHT);
	}
	
	public static boolean isLeafBlock(Block block){
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LEAVES)
				|| FallingTree.config.getTreesConfiguration().getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = FallingTree.config.getTreesConfiguration().getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
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
	
	public static boolean canPlayerBreakTree(PlayerEntity player){
		final ToolConfiguration toolConfiguration = FallingTree.config.getToolsConfiguration();
		final Item heldItem = player.getMainHandStack().getItem();
		final boolean isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| heldItem.isIn(FabricToolTags.AXES)
				|| toolConfiguration.getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = toolConfiguration.getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
}
