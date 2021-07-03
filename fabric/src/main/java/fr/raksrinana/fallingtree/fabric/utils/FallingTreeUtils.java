package fr.raksrinana.fallingtree.fabric.utils;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.AXES;
import static net.minecraft.Util.NIL_UUID;
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
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var identifier = new ResourceLocation(name);
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
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var identifier = new ResourceLocation(name);
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
		var isWhitelistedBlock = LEAVES.contains(block)
				|| config.getTrees().getWhitelistedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			var isBlacklistedBlock = config.getTrees().getBlacklistedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(Player player){
		var toolConfiguration = config.getTools();
		var heldItem = player.getMainHandItem().getItem();
		var isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| AXES.contains(heldItem)
				|| toolConfiguration.getWhitelistedItems().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			var isBlacklistedTool = toolConfiguration.getBlacklistedItems().stream().anyMatch(tool -> tool.equals(heldItem));
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
		if(isLeafBlock(checkBlock)){
			return TreePartType.LEAF;
		}
		return TreePartType.OTHER;
	}
	
	public static boolean isLeafNeedBreakBlock(Block block){
		return config.getTrees()
				.getWhitelistedNonDecayLeaveBlocks().stream()
				.anyMatch(log -> log.equals(block));
	}
	
	public static boolean isPlayerInRightState(Player player){
		if(player.isCreative() && !config.isBreakInCreative()){
			return false;
		}
		if(config.isReverseSneaking() != player.isCrouching()){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	public static boolean isLogBlock(Block block){
		var isWhitelistedBlock = LOGS.contains(block)
				|| config.getTrees().getWhitelistedLogBlocks().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			var isBlacklistedBlock = config.getTrees().getBlacklistedLogBlocks().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return WART_BLOCKS.contains(block) || block.equals(SHROOMLIGHT);
	}
	
	public static void notifyPlayer(Player player, Component text){
		if(player instanceof ServerPlayer serverPlayer){
			switch(config.getNotificationMode()){
				case CHAT -> player.sendMessage(text, NIL_UUID);
				case ACTION_BAR -> serverPlayer.sendMessage(text, ChatType.GAME_INFO, NIL_UUID);
			}
		}
		else{
			player.sendMessage(text, NIL_UUID);
		}
	}
}
