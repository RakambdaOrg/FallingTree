package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FallingTreeUtils{
	public static Set<Item> getAsItems(Collection<? extends String> names){
		return names.stream().map(FallingTreeUtils::getItem).filter(Objects::nonNull).collect(Collectors.toSet());
	}
	
	@Nullable
	public static Item getItem(String name){
		try{
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static Set<Block> getAsBlocks(Collection<? extends String> names){
		return names.stream().map(FallingTreeUtils::getBlock).filter(Objects::nonNull).collect(Collectors.toSet());
	}
	
	@Nullable
	public static Block getBlock(String name){
		try{
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static boolean isTreeBlock(@Nonnull Block block){
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LOGS) || Config.COMMON.getTreesConfiguration().getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = Config.COMMON.getTreesConfiguration().getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isLeafBlock(@Nonnull Block block){
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LEAVES) || Config.COMMON.getTreesConfiguration().getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = Config.COMMON.getTreesConfiguration().getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(@Nonnull PlayerEntity player){
		final Item heldItem = player.getHeldItem(Hand.MAIN_HAND).getItem();
		final boolean isWhitelistedTool = heldItem instanceof AxeItem || Config.COMMON.getToolsConfiguration().getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = Config.COMMON.getToolsConfiguration().getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
}
