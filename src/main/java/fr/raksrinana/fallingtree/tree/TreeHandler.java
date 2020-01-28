package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TreeHandler{
	public static boolean isTreeBlock(@Nonnull IWorld world, @Nonnull BlockPos blockPos){
		final Block block = world.getBlockState(blockPos).getBlock();
		final boolean isWhitelistedBlock = block.isIn(BlockTags.LOGS) || Config.COMMON.getWhitelistedLogs().anyMatch(log -> log.equals(block));
		if(isWhitelistedBlock){
			final boolean isBlacklistedBlock = Config.COMMON.getBlacklistedLogs().anyMatch(log -> log.equals(block));
			return !isBlacklistedBlock;
		}
		return false;
	}
	
	@Nonnull
	public static Optional<Tree> getTree(@Nonnull IWorld world, @Nonnull BlockPos blockPos){
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
	
	@Nonnull
	private static Collection<BlockPos> neighborLogs(@Nonnull IWorld world, @Nonnull Block logBlock, @Nonnull BlockPos blockPos, @Nonnull Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					BlockPos pos = blockPos.add(x, y, z);
					if(!analyzedPos.contains(pos) && isSameLog(world, pos, logBlock)){
						neighborLogs.add(blockPos.add(x, y, z));
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static boolean isSameLog(@Nonnull IWorld world, @Nonnull BlockPos blockPos, @Nullable Block logBlock){
		return world.getBlockState(blockPos).getBlock().equals(logBlock);
	}
	
	public static boolean destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		int toolUsesLeft = (!tool.isDamageable() || Config.COMMON.ignoreDurabilityLoss.get()) ? Integer.MAX_VALUE : tool.getMaxDamage() - tool.getDamage();
		if(Config.COMMON.preserveTools.get()){
			toolUsesLeft--;
		}
		if(toolUsesLeft < 1){
			return false;
		}
		final boolean isFullyBroken = Config.COMMON.ignoreDurabilityLoss.get() || (tool.getMaxDamage() - tool.getDamage()) >= tree.getLogCount();
		tree.getLogs().stream().limit(toolUsesLeft).forEachOrdered(logBlock -> {
			if(!Config.COMMON.ignoreDurabilityLoss.get() && tree.getWorld() instanceof World){
				tool.onBlockDestroyed((World) tree.getWorld(), tree.getWorld().getBlockState(logBlock), logBlock, player);
			}
			tree.getWorld().destroyBlock(logBlock, true);
		});
		if(isFullyBroken){
			final int radius = Config.COMMON.forceBreakLeavesRadius.get();
			if(radius > 0){
				tree.getLogs().stream().max(Comparator.comparingInt(BlockPos::getY)).ifPresent(topLog -> {
					BlockPos.Mutable checkPos = new BlockPos.Mutable();
					for(int dx = -radius; dx < radius; dx++){
						for(int dy = -radius; dy < radius; dy++){
							for(int dz = -radius; dz < radius; dz++){
								checkPos.setPos(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
								final BlockState checkState = tree.getWorld().getBlockState(checkPos);
								if(BlockTags.LEAVES.contains(checkState.getBlock())){
									tree.getWorld().destroyBlock(checkPos, true);
								}
							}
						}
					}
				});
			}
		}
		return true;
	}
	
	public static boolean canPlayerBreakTree(@Nonnull PlayerEntity player){
		final ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
		final boolean isWhitelistedTool = heldItem.getItem() instanceof AxeItem || Config.COMMON.getWhitelistedTools().anyMatch(tool -> tool.equals(heldItem.getItem()));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = Config.COMMON.getBlacklistedTools().anyMatch(tool -> tool.equals(heldItem.getItem()));
			return !isBlacklistedTool;
		}
		return false;
	}
}
