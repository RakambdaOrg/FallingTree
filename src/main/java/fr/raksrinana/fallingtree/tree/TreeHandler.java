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
	
	@Nonnull
	private static Collection<BlockPos> neighborLogs(@Nonnull IWorld world, @Nonnull Block logBlock, @Nonnull BlockPos blockPos, @Nonnull Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		final BlockPos.Mutable checkPos = new BlockPos.Mutable();
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
	
	private static boolean isSameLog(@Nonnull IWorld world, @Nonnull BlockPos blockPos, @Nullable Block logBlock){
		return world.getBlockState(blockPos).getBlock().equals(logBlock);
	}
	
	public static boolean destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		int toolUsesLeft = (!tool.isDamageable() || Config.COMMON.getToolsConfiguration().isIgnoreDurabilityLoss()) ? Integer.MAX_VALUE : tool.getMaxDamage() - tool.getDamage();
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			toolUsesLeft--;
		}
		if(toolUsesLeft < 1){
			return false;
		}
		final boolean isFullyBroken = Config.COMMON.getToolsConfiguration().isIgnoreDurabilityLoss() || (tool.getMaxDamage() - tool.getDamage()) >= tree.getLogCount();
		tree.getLogs().stream().limit(toolUsesLeft).forEachOrdered(logBlock -> {
			if(!Config.COMMON.getToolsConfiguration().isIgnoreDurabilityLoss()){
				tool.onBlockDestroyed(world, world.getBlockState(logBlock), logBlock, player);
			}
			world.destroyBlock(logBlock, true);
		});
		if(isFullyBroken){
			final int radius = Config.COMMON.getTreesConfiguration().getLavesBreakingForceRadius();
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
		final boolean isWhitelistedTool = heldItem.getItem() instanceof AxeItem || Config.COMMON.getToolsConfiguration().getWhitelisted().anyMatch(tool -> tool.equals(heldItem.getItem()));
		if(isWhitelistedTool){
			final boolean isBlacklistedTool = Config.COMMON.getToolsConfiguration().getBlacklisted().anyMatch(tool -> tool.equals(heldItem.getItem()));
			return !isBlacklistedTool;
		}
		return false;
	}
}
