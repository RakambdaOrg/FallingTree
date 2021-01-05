package fr.raksrinana.fallingtree.utils;

import fr.raksrinana.fallingtree.config.Config;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.utils.TreePartType.*;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static net.minecraft.block.Blocks.SHROOMLIGHT;
import static net.minecraft.tags.BlockTags.*;
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
			boolean isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			ResourceLocation resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(ItemTags.getCollection().get(resourceLocation))
						.map(ITag::getAllElements)
						.map(Collection::stream)
						.orElse(empty());
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
			boolean isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			ResourceLocation resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(BlockTags.getCollection().get(resourceLocation))
						.map(ITag::getAllElements)
						.map(Collection::stream)
						.orElse(empty());
			}
			return Stream.of(BLOCKS.getValue(resourceLocation));
		}
		catch(Exception e){
			return empty();
		}
	}
	
	public static boolean isLeafBlock(@Nonnull Block block){
		boolean isWhitelistedBlock = block.isIn(LEAVES)
				|| Config.COMMON.getTreesConfiguration().getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			boolean isBlacklistedBlock = Config.COMMON.getTreesConfiguration().getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static TreePartType getTreePart(Block checkBlock){
		if(isLogBlock(checkBlock)){
			return LOG;
		}
		if(isNetherWartOrShroomlight(checkBlock)){
			return WART;
		}
		return OTHER;
	}
	
	public static boolean isLogBlock(Block block){
		boolean isWhitelistedBlock = block.isIn(LOGS)
				|| Config.COMMON.getTreesConfiguration().getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			boolean isBlacklistedBlock = Config.COMMON.getTreesConfiguration().getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isNetherWartOrShroomlight(Block block){
		return block.isIn(WART_BLOCKS) || block.equals(SHROOMLIGHT);
	}
}
