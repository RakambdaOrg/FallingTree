package fr.raksrinana.fallingtree.fabric.utils;

import fr.raksrinana.fallingtree.fabric.config.ConfigCache;
import fr.raksrinana.fallingtree.fabric.config.Configuration;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.fabric.FallingTree.CHOPPER_ENCHANTMENT;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.minecraft.Util.NIL_UUID;
import static net.minecraft.tags.BlockTags.LEAVES;
import static net.minecraft.tags.BlockTags.WART_BLOCKS;
import static net.minecraft.world.level.block.Blocks.SHROOMLIGHT;

public class FallingTreeUtils{
	public static Set<Item> getAsItems(Collection<? extends String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.filter(val -> !val.isEmpty())
				.flatMap(FallingTreeUtils::getItem)
				.filter(Objects::nonNull)
				.filter(item -> !Items.AIR.equals(item))
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
				return TagFactory.ITEM.create(identifier).getValues().stream();
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
				.filter(block -> !Blocks.AIR.equals(block))
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
				return TagFactory.BLOCK.create(identifier).getValues().stream();
			}
			return Stream.of(Registry.BLOCK.get(identifier));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static boolean isLeafBlock(Block block){
		var isAllowedBlock = LEAVES.contains(block)
		                         || Configuration.getInstance().getTrees().getAllowedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
		if(isAllowedBlock){
			var isDeniedBlock = Configuration.getInstance().getTrees().getDeniedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
			return !isDeniedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(Player player, BlockState aimedBlockState){
		var toolConfiguration = Configuration.getInstance().getTools();
		var heldItemStack = player.getMainHandItem();
		var heldItem = heldItemStack.getItem();
		
		var isAllowedTool = toolConfiguration.isIgnoreTools()
		                        || heldItem.getDestroySpeed(heldItemStack, aimedBlockState) > 1.0f
		                        || toolConfiguration.getAllowedItems().stream().anyMatch(tool -> tool.equals(heldItem));
		if(!isAllowedTool){
			return false;
		}
		
		var isDeniedTool = toolConfiguration.getDeniedItems().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isDeniedTool){
			return false;
		}
		
		var isToolChopperEnchanted = EnchantmentHelper.getItemEnchantmentLevel(CHOPPER_ENCHANTMENT, heldItemStack) > 0;
		if(toolConfiguration.isRequireEnchant() && !isToolChopperEnchanted){
			return false;
		}
		
		return true;
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
		return Configuration.getInstance().getTrees()
				.getAllowedNonDecayLeaveBlocks().stream()
				.anyMatch(log -> log.equals(block));
	}
	
	public static boolean isPlayerInRightState(Player player, BlockState aimedBlockState){
		if(player.isCreative() && !Configuration.getInstance().isBreakInCreative()){
			return false;
		}
		if(Configuration.getInstance().isReverseSneaking() != player.isCrouching()){
			return false;
		}
		return canPlayerBreakTree(player, aimedBlockState);
	}
	
	public static boolean isLogBlock(Block block){
		var isAllowedBlock = ConfigCache.getInstance().getDefaultLogs().stream().anyMatch(log -> log.equals(block))
		                         || Configuration.getInstance().getTrees().getAllowedLogBlocks().stream().anyMatch(log -> log.equals(block));
		if(isAllowedBlock){
			var isDeniedBlock = Configuration.getInstance().getTrees().getDeniedLogBlocks().stream().anyMatch(log -> log.equals(block));
			return !isDeniedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return WART_BLOCKS.contains(block) || block.equals(SHROOMLIGHT);
	}
	
	public static void notifyPlayer(Player player, Component text){
		if(player instanceof ServerPlayer serverPlayer){
			switch(Configuration.getInstance().getNotificationMode()){
				case CHAT -> player.sendMessage(text, NIL_UUID);
				case ACTION_BAR -> serverPlayer.sendMessage(text, ChatType.GAME_INFO, NIL_UUID);
			}
		}
		else{
			player.sendMessage(text, NIL_UUID);
		}
	}
}
