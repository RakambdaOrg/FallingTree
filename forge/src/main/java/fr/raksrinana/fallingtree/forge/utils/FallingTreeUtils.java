package fr.raksrinana.fallingtree.forge.utils;

import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.config.ConfigCache;
import fr.raksrinana.fallingtree.forge.enchant.FallingTreeEnchantments;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.forge.utils.TreePartType.*;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.minecraft.Util.NIL_UUID;
import static net.minecraft.tags.BlockTags.LEAVES;
import static net.minecraft.tags.BlockTags.WART_BLOCKS;
import static net.minecraft.world.InteractionHand.MAIN_HAND;
import static net.minecraft.world.level.block.Blocks.SHROOMLIGHT;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

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
						.map(Tag::getValues)
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
				.filter(block -> !Blocks.AIR.equals(block))
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
						.map(Tag::getValues)
						.flatMap(Collection::stream);
			}
			return Stream.of(BLOCKS.getValue(resourceLocation));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static boolean isLeafBlock(@Nonnull Block block){
		var isWhitelistedBlock = LEAVES.contains(block)
		                         || Config.COMMON.getTrees().getWhitelistedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			var isBlacklistedBlock = Config.COMMON.getTrees().getBlacklistedLeaveBlocks().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(Player player, BlockState aimedBlockState){
		var toolConfiguration = Config.COMMON.getTools();
		var heldItemStack = player.getItemInHand(MAIN_HAND);
		var heldItem = heldItemStack.getItem();
		
		var isWhitelistedTool = toolConfiguration.isIgnoreTools()
		                        || heldItem.getDestroySpeed(heldItemStack, aimedBlockState) > 1f
		                        || toolConfiguration.getWhitelistedItems().stream().anyMatch(tool -> tool.equals(heldItem));
		if(!isWhitelistedTool){
			return false;
		}
		
		var isBlacklistedTool = toolConfiguration.getBlacklistedItems().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isBlacklistedTool){
			return false;
		}
		
		var isToolChopperEnchanted = EnchantmentHelper.getItemEnchantmentLevel(FallingTreeEnchantments.chopper, heldItemStack) > 0;
		if(toolConfiguration.isRequireEnchant() && !isToolChopperEnchanted){
			return false;
		}
		
		return true;
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
		var isWhitelistedBlock = ConfigCache.getInstance().getDefaultLogs().stream().anyMatch(log -> log.equals(block))
		                         || Config.COMMON.getTrees().getWhitelistedLogBlocks().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			var isBlacklistedBlock = Config.COMMON.getTrees().getBlacklistedLogBlocks().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return WART_BLOCKS.contains(block) || block.equals(SHROOMLIGHT);
	}
	
	public static void notifyPlayer(Player player, Component text){
		if(player instanceof ServerPlayer serverPlayer){
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
