package fr.raksrinana.fallingtree.utils;

import fr.raksrinana.fallingtree.config.ToolConfiguration;
import fr.raksrinana.fallingtree.config.TreeConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FallingTreeUtils{
	public static Set<Item> getAsItems(String[] names){
		return Arrays.stream(names).map(FallingTreeUtils::getItem).filter(Objects::nonNull).collect(Collectors.toSet());
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
	
	public static Set<Block> getAsBlocks(String[] names){
		return Arrays.stream(names).map(FallingTreeUtils::getBlock).filter(Objects::nonNull).collect(Collectors.toSet());
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
		final boolean isWhitelistedBlock = block instanceof BlockLog || TreeConfiguration.getWhitelistedLogs().stream().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = TreeConfiguration.getBlacklistedLogs().stream().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean isLeafBlock(@Nonnull Block block){
		final boolean isWhitelistedBlock = block instanceof BlockLeaves || TreeConfiguration.getWhitelistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = TreeConfiguration.getBlacklistedLeaves().stream().anyMatch(leaf -> leaf.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	public static boolean canPlayerBreakTree(@Nonnull EntityPlayer player){
		final Item heldItem = player.getHeldItem(EnumHand.MAIN_HAND).getItem();
		final boolean isWhitelistedTool = ToolConfiguration.isIgnoreTools() || heldItem instanceof ItemAxe || ToolConfiguration.getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = ToolConfiguration.getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
}
