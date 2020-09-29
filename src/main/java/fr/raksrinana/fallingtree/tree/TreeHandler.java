package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.utils.ToAnalyzePos;
import fr.raksrinana.fallingtree.utils.TreePart;
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
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.*;

public class TreeHandler{
	public static Optional<Tree> getTree(World world, BlockPos originPos){
		Block originBlock = world.getBlockState(originPos).getBlock();
		if(!isLogBlock(originBlock)){
			return Optional.empty();
		}
		Queue<ToAnalyzePos> toAnalyzePos = new PriorityQueue<>();
		Set<ToAnalyzePos> analyzedPos = new HashSet<>();
		Tree tree = new Tree(world, originPos);
		toAnalyzePos.add(new ToAnalyzePos(originPos, originBlock, originPos, originBlock, TreePart.LOG));
		while(!toAnalyzePos.isEmpty()){
			ToAnalyzePos analyzingPos = toAnalyzePos.remove();
			tree.addPart(analyzingPos.getTreePart(), analyzingPos.getCheckPos());
			analyzedPos.add(analyzingPos);
			Collection<ToAnalyzePos> nearbyPos = neighborLogs(world, originPos, originBlock, analyzingPos, analyzedPos);
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
	
	private static Collection<ToAnalyzePos> neighborLogs(World world, BlockPos originPos, Block originBlock, ToAnalyzePos parent, Collection<ToAnalyzePos> analyzedPos){
		final BlockPos blockPos = parent.getCheckPos();
		List<ToAnalyzePos> neighborLogs = new LinkedList<>();
		final BlockPos.Mutable checkPos = new BlockPos.Mutable();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					checkPos.set(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
					Block checkBlock = world.getBlockState(checkPos).getBlock();
					ToAnalyzePos analyzed = new ToAnalyzePos(blockPos, parent.getCheckBlock(), checkPos.toImmutable(), checkBlock, getTreePart(checkBlock));
					if(!analyzedPos.contains(analyzed) && shouldIncludeInChain(originPos, originBlock, parent, analyzed)){
						neighborLogs.add(analyzed);
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static TreePart getTreePart(Block checkBlock){
		if(isLogBlock(checkBlock)){
			return TreePart.LOG;
		}
		if(isNetherWartOrShroomlight(checkBlock)){
			return TreePart.WART;
		}
		return TreePart.OTHER;
	}
	
	private static long getLeavesAround(World world, BlockPos blockPos){
		return Arrays.stream(Direction.values())
				.map(blockPos::offset)
				.filter(testPos -> {
					Block block = world.getBlockState(testPos).getBlock();
					return isLeafBlock(block) || isNetherWartOrShroomlight(block);
				})
				.count();
	}
	
	private static boolean shouldIncludeInChain(BlockPos originPos, Block originBlock, ToAnalyzePos parent, ToAnalyzePos check){
		if(parent.getTreePart() == TreePart.LOG && isSameTree(originBlock, check)){
			return true;
		}
		if(FallingTree.config.trees.isBreakNetherTreeWarts()){
			if(check.getTreePart() == TreePart.WART){
				BlockPos checkBlockPos = check.getCheckPos();
				int dx = Math.abs(originPos.getX() - checkBlockPos.getX());
				int dz = Math.abs(originPos.getZ() - checkBlockPos.getZ());
				return dx <= 4 && dz <= 4;
			}
		}
		return false;
	}
	
	private static boolean isSameTree(Block parentLogBlock, ToAnalyzePos check){
		if(FallingTree.config.getTreesConfiguration().isAllowMixedLogs()){
			return check.getTreePart() == TreePart.LOG;
		}
		else{
			return check.getCheckBlock().equals(parentLogBlock);
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
			tree.getWarts().forEach(wartPos -> {
				final BlockState wartState = world.getBlockState(wartPos);
				wartState.getBlock().afterBreak(world, player, wartPos, wartState, world.getBlockEntity(wartPos), tool);
				world.removeBlock(wartPos, false);
			});
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
		tree.getTopMostFurthestPart().ifPresent(logBlock -> {
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
