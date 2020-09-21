package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import java.util.*;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.FallingTreeUtils.isLeafBlock;
import static fr.raksrinana.fallingtree.FallingTreeUtils.isTreeBlock;

public class TreeHandler{
	public static Optional<Tree> getTree(World world, BlockPos blockPos){
		Block logBlock = world.getBlockState(blockPos).getBlock();
		if(!isTreeBlock(logBlock)){
			return Optional.empty();
		}
		Queue<BlockPos> toAnalyzePos = new LinkedList<>();
		Set<BlockPos> analyzedPos = new HashSet<>();
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
		
		if(FallingTree.config.getTreesConfiguration().getBreakMode().shouldCheckLeavesAround()){
			int aroundRequired = FallingTree.config.getTreesConfiguration().getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(world, topLog) < aroundRequired)
					.orElse(true)){
				return Optional.empty();
			}
		}
		
		return Optional.of(tree);
	}
	
	private static Collection<BlockPos> neighborLogs(World world, Block logBlock, BlockPos blockPos, Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		final BlockPos.Mutable checkPos = new BlockPos.Mutable();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					checkPos.set(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
					if(!analyzedPos.contains(checkPos) && isSameTree(world, checkPos, logBlock)){
						neighborLogs.add(checkPos.toImmutable());
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static long getLeavesAround(World world, BlockPos blockPos){
		return Arrays.stream(Direction.values())
				.map(blockPos::offset)
				.filter(testPos -> isLeafBlock(world.getBlockState(testPos).getBlock()))
				.count();
	}
	
	private static boolean isSameTree(World world, BlockPos checkBlockPos, Block parentLogBlock){
		Block checkBlock = world.getBlockState(checkBlockPos).getBlock();
		if(FallingTree.config.getTreesConfiguration().isAllowMixedLogs()){
			return isTreeBlock(checkBlock);
		}
		else{
			return checkBlock.equals(parentLogBlock);
		}
	}
	
	public static boolean destroyInstant(Tree tree, PlayerEntity player, ItemStack tool){
		final World world = tree.getWorld();
		final int damageMultiplicand = FallingTree.config.getToolsConfiguration().getDamageMultiplicand();
		final int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(FallingTree.config.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendSystemMessage(new TranslatableText("chat.falling_tree.prevented_break_tool"), Util.NIL_UUID);
				return false;
			}
			if(tree.getLogCount() >= rawWeightedUsesLeft){
				rawWeightedUsesLeft = Math.ceil(rawWeightedUsesLeft) - 1;
			}
		}
		final boolean isTreeFullyBroken = damageMultiplicand == 0 || rawWeightedUsesLeft >= tree.getLogCount();
		tree.getLogs().stream().limit((int) rawWeightedUsesLeft).forEachOrdered(logBlock -> {
			final BlockState logState = world.getBlockState(logBlock);
			logState.getBlock().afterBreak(world, player, logBlock, logState, world.getBlockEntity(logBlock), tool);
			world.removeBlock(logBlock, false);
		});
		int toolDamage = (damageMultiplicand * (int) Math.min(tree.getLogCount(), rawWeightedUsesLeft)) - 1;
		if(toolDamage > 0){
			tool.damage(toolDamage, player, (entity) -> {});
		}
		if(isTreeFullyBroken){
			final int radius = FallingTree.config.getTreesConfiguration().getLeavesBreakingForceRadius();
			if(radius > 0){
				tree.getLogs().stream().max(Comparator.comparingInt(BlockPos::getY)).ifPresent(topLog -> {
					BlockPos.Mutable checkPos = new BlockPos.Mutable();
					for(int dx = -radius; dx < radius; dx++){
						for(int dy = -radius; dy < radius; dy++){
							for(int dz = -radius; dz < radius; dz++){
								checkPos.set(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
								final BlockState checkState = world.getBlockState(checkPos);
								final Block checkBlock = checkState.getBlock();
								if(isLeafBlock(checkBlock)){
									Block.dropStacks(checkState, world, checkPos);
									world.removeBlock(checkPos, false);
								}
							}
						}
					}
				});
			}
		}
		return true;
	}
	
	public static boolean destroyShift(Tree tree, PlayerEntity player, ItemStack tool){
		final World world = tree.getWorld();
		final int damageMultiplicand = FallingTree.config.getToolsConfiguration().getDamageMultiplicand();
		final int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(FallingTree.config.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendSystemMessage(new TranslatableText("chat.falling_tree.prevented_break_tool"), Util.NIL_UUID);
				return false;
			}
		}
		tree.getTopMostFurthestLog().ifPresent(logBlock -> {
			final BlockState logState = world.getBlockState(logBlock);
			logState.getBlock().afterBreak(world, player, tree.getHitPos(), logState, world.getBlockEntity(logBlock), tool);
			world.removeBlock(logBlock, false);
		});
		int toolDamage = damageMultiplicand;
		if(toolDamage > 0){
			tool.damage(toolDamage, player, (entity) -> {});
		}
		return false;
	}
}
