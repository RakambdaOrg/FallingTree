package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.config.CommonConfig;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import fr.raksrinana.fallingtree.config.TreeConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TreeHandler{
	@Nonnull
	public static Optional<Tree> getTree(@Nonnull World world, @Nonnull BlockPos blockPos){
		if(!isTreeBlock(world, blockPos)){
			return Optional.empty();
		}
		Queue<BlockPos> toAnalyzePos = new LinkedList<>();
		Set<BlockPos> analyzedPos = new HashSet<>();
		Block logBlock = world.getBlockState(blockPos).getBlock();
		Tree tree = new Tree(world, blockPos);
		toAnalyzePos.add(blockPos);
		while(!toAnalyzePos.isEmpty()){
			BlockPos analyzingPos = toAnalyzePos.remove();
			tree.addLog(analyzingPos);
			analyzedPos.add(analyzingPos);
			Collection<BlockPos> nearbyPos = neighborLogs(world, logBlock, analyzingPos, analyzedPos);
			nearbyPos.removeAll(analyzedPos);
			toAnalyzePos.addAll(nearbyPos.stream().filter(pos -> !toAnalyzePos.contains(pos)).collect(Collectors.toList()));
		}
		return Optional.of(tree);
	}
	
	public static boolean isTreeBlock(@Nonnull World world, @Nonnull BlockPos blockPos){
		final Block block = world.getBlockState(blockPos).getBlock();
		final boolean isWhitelistedBlock = block instanceof BlockLog || TreeConfiguration.getWhitelistedLogs().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = TreeConfiguration.getBlacklistedLogs().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	@Nonnull
	private static Collection<BlockPos> neighborLogs(@Nonnull World world, @Nonnull Block logBlock, @Nonnull BlockPos blockPos, @Nonnull Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		final BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					checkPos.setPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
					if(!analyzedPos.contains(checkPos) && isSameLog(world, checkPos, logBlock)){
						neighborLogs.add(checkPos.toImmutable());
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static boolean isSameLog(@Nonnull World world, @Nonnull BlockPos blockPos, @Nullable Block logBlock){
		return world.getBlockState(blockPos).getBlock().equals(logBlock);
	}
	
	public static boolean destroy(@Nonnull Tree tree, @Nonnull EntityPlayer player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		int toolUsesLeft = (!tool.isItemStackDamageable() || ToolConfiguration.isIgnoreDurabilityLoss()) ? Integer.MAX_VALUE : tool.getMaxDamage() - tool.getItemDamage();
		if(ToolConfiguration.isPreserve()){
			toolUsesLeft--;
		}
		if(toolUsesLeft < 1){
			return false;
		}
		final boolean isFullyBroken = ToolConfiguration.isIgnoreDurabilityLoss() || (tool.getMaxDamage() - tool.getItemDamage()) >= tree.getLogCount();
		tree.getLogs().stream().limit(toolUsesLeft).forEachOrdered(logBlock -> {
			if(!ToolConfiguration.isIgnoreDurabilityLoss()){
				tool.onBlockDestroyed(world, world.getBlockState(logBlock), logBlock, player);
			}
			world.destroyBlock(logBlock, true);
		});
		if(isFullyBroken){
			final int radius = TreeConfiguration.getLavesBreakingForceRadius();
			if(radius > 0){
				tree.getLogs().stream().max(Comparator.comparingInt(BlockPos::getY)).ifPresent(topLog -> {
					BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
					for(int dx = -radius; dx < radius; dx++){
						for(int dy = -radius; dy < radius; dy++){
							for(int dz = -radius; dz < radius; dz++){
								checkPos.setPos(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
								final IBlockState checkState = world.getBlockState(checkPos);
								final Block checkBlock = checkState.getBlock();
								if(checkBlock instanceof BlockLeaves){
									checkBlock.dropBlockAsItem(world, checkPos, checkState, 0);
									world.destroyBlock(checkPos, false);
								}
							}
						}
					}
				});
			}
		}
		return true;
	}
	
	public static boolean canPlayerBreakTree(@Nonnull EntityPlayer player){
		final ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
		final boolean isWhitelistedTool = heldItem.getItem() instanceof ItemAxe || ToolConfiguration.getWhitelisted().anyMatch(tool -> tool.equals(heldItem.getItem()));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = ToolConfiguration.getBlacklisted().anyMatch(tool -> tool.equals(heldItem.getItem()));
			return !isBlacklistedTool;
		}
		return false;
	}
}
