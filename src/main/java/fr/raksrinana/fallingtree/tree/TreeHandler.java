package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.FallingTreeUtils.isLeafBlock;
import static fr.raksrinana.fallingtree.FallingTreeUtils.isTreeBlock;

public class TreeHandler{
	@Nonnull
	public static Optional<Tree> getTree(@Nonnull World world, @Nonnull BlockPos blockPos){
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
		
		int aroundRequired = Config.COMMON.getTreesConfiguration().getMinimumLeavesAroundRequired();
		if(tree.getTopMostLog().map(topLog -> getLeavesAround(world, topLog) >= aroundRequired).orElseGet(() -> aroundRequired == 0)){
			return Optional.of(tree);
		}
		
		return Optional.empty();
	}
	
	private static long getLeavesAround(@Nonnull IWorld world, @Nonnull BlockPos blockPos){
		return Arrays.stream(Direction.values())
				.map(blockPos::offset)
				.filter(testPos -> isLeafBlock(world.getBlockState(testPos).getBlock()))
				.count();
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
	
	public static boolean destroyInstant(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		final int damageMultiplicand = Config.COMMON.getToolsConfiguration().getDamageMultiplicand();
		final int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslationTextComponent("chat.falling_tree.prevented_break_tool"), Util.field_240973_b_);
				return false;
			}
			if(tree.getLogCount() >= rawWeightedUsesLeft){
				rawWeightedUsesLeft = Math.ceil(rawWeightedUsesLeft) - 1;
			}
		}
		final boolean isTreeFullyBroken = damageMultiplicand == 0 || rawWeightedUsesLeft >= tree.getLogCount();
		tree.getLogs().stream().limit((int) rawWeightedUsesLeft).forEachOrdered(logBlock -> {
			final BlockState logState = world.getBlockState(logBlock);
			player.addStat(Stats.ITEM_USED.get(logState.getBlock().asItem()));
			logState.getBlock().harvestBlock(world, player, logBlock, logState, world.getTileEntity(logBlock), tool);
			world.destroyBlock(logBlock, false);
		});
		int toolDamage = (damageMultiplicand * (int) Math.min(tree.getLogCount(), rawWeightedUsesLeft)) - 1;
		if(toolDamage > 0){
			tool.damageItem(toolDamage, player, (entity) -> {});
		}
		if(isTreeFullyBroken){
			final int radius = Config.COMMON.getTreesConfiguration().getLavesBreakingForceRadius();
			if(radius > 0){
				tree.getLogs().stream().max(Comparator.comparingInt(BlockPos::getY)).ifPresent(topLog -> {
					BlockPos.Mutable checkPos = new BlockPos.Mutable();
					for(int dx = -radius; dx < radius; dx++){
						for(int dy = -radius; dy < radius; dy++){
							for(int dz = -radius; dz < radius; dz++){
								checkPos.setPos(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
								final BlockState checkState = world.getBlockState(checkPos);
								final Block checkBlock = checkState.getBlock();
								if(isLeafBlock(checkBlock)){
									Block.spawnDrops(checkState, world, checkPos);
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
	
	public static boolean destroyShift(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		final int damageMultiplicand = Config.COMMON.getToolsConfiguration().getDamageMultiplicand();
		final int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslationTextComponent("chat.falling_tree.prevented_break_tool"), Util.field_240973_b_);
				return false;
			}
		}
		tree.getTopMostFurthestLog().ifPresent(logBlock -> {
			final BlockState logState = world.getBlockState(logBlock);
			player.addStat(Stats.ITEM_USED.get(logState.getBlock().asItem()));
			logState.getBlock().harvestBlock(world, player, tree.getHitPos(), logState, world.getTileEntity(logBlock), tool);
			world.destroyBlock(logBlock, false);
		});
		int toolDamage = damageMultiplicand;
		if(toolDamage > 0){
			tool.damageItem(toolDamage, player, (entity) -> {});
		}
		return true;
	}
}
