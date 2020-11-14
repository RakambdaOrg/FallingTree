package fr.raksrinana.fallingtree.utils;

import fr.raksrinana.fallingtree.config.Config;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
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
	
	@Nonnull
	public static Stream<Item> getItem(String name){
		try{
			boolean isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			ResourceLocation resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(ItemTags.getCollection().get(resourceLocation))
						.map(ITag::getAllElements)
						.map(Collection::stream)
						.orElse(Stream.empty());
			}
			return Stream.of(ForgeRegistries.ITEMS.getValue(resourceLocation));
		}
		catch(Exception e){
			return Stream.empty();
		}
	}
	
	public static Set<Block> getAsBlocks(Collection<? extends String> names){
		return names.stream().flatMap(FallingTreeUtils::getBlock).filter(Objects::nonNull).collect(Collectors.toSet());
	}
	
	@Nonnull
	public static Stream<Block> getBlock(String name){
		try{
			boolean isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			ResourceLocation resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(BlockTags.getCollection().get(resourceLocation))
						.map(ITag::getAllElements)
						.map(Collection::stream)
						.orElse(Stream.empty());
			}
			return Stream.of(ForgeRegistries.BLOCKS.getValue(resourceLocation));
		}
		catch(Exception e){
			return Stream.empty();
		}
	}
	
	public static boolean isLogBlock(Block block){
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LOGS)
				|| Config.COMMON.getTreesConfiguration().getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = Config.COMMON.getTreesConfiguration().getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return block.isIn(BlockTags.WART_BLOCKS) || block.equals(Blocks.SHROOMLIGHT);
	}
	
	public static boolean isLeafBlock(@Nonnull Block block){
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LEAVES)
				|| Config.COMMON.getTreesConfiguration().getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = Config.COMMON.getTreesConfiguration().getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(@Nonnull PlayerEntity player){
		final ToolConfiguration toolConfiguration = Config.COMMON.getToolsConfiguration();
		final Item heldItem = player.getHeldItem(Hand.MAIN_HAND).getItem();
		final boolean isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| heldItem instanceof AxeItem
				|| toolConfiguration.getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = toolConfiguration.getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
}
