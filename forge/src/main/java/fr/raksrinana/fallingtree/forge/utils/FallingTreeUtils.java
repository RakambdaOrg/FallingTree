package fr.raksrinana.fallingtree.forge.utils;

import fr.raksrinana.fallingtree.forge.config.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.forge.utils.TreePartType.*;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.minecraft.block.Blocks.SHROOMLIGHT;
import static net.minecraft.tags.BlockTags.*;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Util.NIL_UUID;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class FallingTreeUtils{
	public static Set<Item> getAsItems(Collection<? extends String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.filter(val -> !val.isEmpty())
				.flatMap(FallingTreeUtils::getItem)
				.filter(Objects::nonNull)
				.collect(toSet());
	}
	
	@Nonnull
	public static Stream<Item> getItem(String name){
		try{
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(ItemTags.getAllTags().getTag(resourceLocation))
						.stream()
						.map(ITag::getValues)
						.flatMap(Collection::stream);
			}
			return Stream.of(ITEMS.getValue(resourceLocation));
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
	
	@Nonnull
	public static Stream<Block> getBlock(String name){
		try{
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(BlockTags.getAllTags().getTag(resourceLocation))
						.stream()
						.map(ITag::getValues)
						.flatMap(Collection::stream);
			}
			return Stream.of(BLOCKS.getValue(resourceLocation));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static boolean isLeafBlock(@Nonnull Block block){
		var isWhitelistedBlock = block.is(LEAVES)
				|| Config.COMMON.getTrees().getWhitelistedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			var isBlacklistedBlock = Config.COMMON.getTrees().getBlacklistedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(PlayerEntity player){
		var toolConfiguration = Config.COMMON.getTools();
		var heldItem = player.getItemInHand(MAIN_HAND).getItem();
		var isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| heldItem instanceof AxeItem
				|| toolConfiguration.getWhitelistedItems().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			var isBlacklistedTool = toolConfiguration.getBlacklistedItems().stream().anyMatch(tool -> tool.equals(heldItem));
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
		return Config.COMMON.getTrees()
				.getWhitelistedNonDecayLeaveBlocks()
				.stream()
				.anyMatch(log -> log.equals(block));
	}
	
	public static boolean isLogBlock(Block block){
		var isWhitelistedBlock = block.is(LOGS)
				|| Config.COMMON.getTrees().getWhitelistedLogBlocks().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			var isBlacklistedBlock = Config.COMMON.getTrees().getBlacklistedLogBlocks().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return block.is(WART_BLOCKS) || block.equals(SHROOMLIGHT);
	}
	
	public static void notifyPlayer(PlayerEntity player, ITextComponent text){
		if(player instanceof ServerPlayerEntity serverPlayer){
			switch(Config.COMMON.getNotificationMode()){
				case CHAT -> player.sendMessage(text, NIL_UUID);
				case ACTION_BAR -> serverPlayer.sendMessage(text, ChatType.GAME_INFO, NIL_UUID);
			}
		}
		else{
			player.sendMessage(text, NIL_UUID);
		}
	}
}
