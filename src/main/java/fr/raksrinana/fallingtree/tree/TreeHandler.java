package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.config.CommonConfig;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import fr.raksrinana.fallingtree.config.TreeConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isLeafBlock;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isTreeBlock;

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
		
		if(TreeConfiguration.getBreakMode().shouldCheckLeavesAround()){
			int aroundRequired = TreeConfiguration.getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(world, topLog) < aroundRequired)
					.orElse(true)){
				return Optional.empty();
			}
		}
		
		return Optional.of(tree);
	}
	
	private static long getLeavesAround(@Nonnull World world, @Nonnull BlockPos blockPos){
		return Arrays.stream(EnumFacing.values())
				.map(blockPos::offset)
				.filter(testPos -> isLeafBlock(world.getBlockState(testPos).getBlock()))
				.count();
	}
	
	@Nonnull
	private static Collection<BlockPos> neighborLogs(@Nonnull World world, @Nonnull Block logBlock, @Nonnull BlockPos blockPos, @Nonnull Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		final BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					checkPos.setPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
					if(!analyzedPos.contains(checkPos) && isSameTree(world, checkPos, logBlock)){
						neighborLogs.add(checkPos.toImmutable());
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static boolean isSameTree(@Nonnull World world, BlockPos checkBlockPos, Block parentLogBlock){
		Block checkBlock = world.getBlockState(checkBlockPos).getBlock();
		if(TreeConfiguration.isAllowMixedLogs()){
			return isTreeBlock(checkBlock);
		}
		else{
			return checkBlock.equals(parentLogBlock);
		}
	}
	
	public static boolean destroyInstant(@Nonnull Tree tree, @Nonnull EntityPlayer player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		final int damageMultiplicand = ToolConfiguration.getDamageMultiplicand();
		final int toolUsesLeft = tool.isItemStackDamageable() ? (tool.getMaxDamage() - tool.getItemDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(ToolConfiguration.isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TextComponentTranslation("chat.falling_tree.prevented_break_tool"));
				return false;
			}
			if(tree.getLogCount() >= rawWeightedUsesLeft){
				rawWeightedUsesLeft = Math.ceil(rawWeightedUsesLeft) - 1;
			}
		}
		final boolean isTreeFullyBroken = damageMultiplicand == 0 || rawWeightedUsesLeft >= tree.getLogCount();
		tree.getLogs().stream().limit((int) rawWeightedUsesLeft).forEachOrdered(logBlock -> {
			final IBlockState logState = world.getBlockState(logBlock);
			player.addStat(StatList.getObjectBreakStats(Item.getItemFromBlock(logState.getBlock())));
			logState.getBlock().harvestBlock(world, player, logBlock, logState, world.getTileEntity(logBlock), tool);
			world.destroyBlock(logBlock, false);
		});
		int toolDamage = (damageMultiplicand * (int) Math.min(tree.getLogCount(), rawWeightedUsesLeft)) - 1;
		if(toolDamage > 0){
			tool.damageItem(toolDamage, player);
		}
		if(isTreeFullyBroken){
			final int radius = TreeConfiguration.getLeavesBreakingForceRadius();
			if(radius > 0){
				tree.getLogs().stream().max(Comparator.comparingInt(BlockPos::getY)).ifPresent(topLog -> {
					BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
					for(int dx = -radius; dx < radius; dx++){
						for(int dy = -radius; dy < radius; dy++){
							for(int dz = -radius; dz < radius; dz++){
								checkPos.setPos(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
								final IBlockState checkState = world.getBlockState(checkPos);
								final Block checkBlock = checkState.getBlock();
								if(isLeafBlock(checkBlock)){
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
	
	public static boolean destroyShift(@Nonnull Tree tree, @Nonnull EntityPlayer player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		final int damageMultiplicand = ToolConfiguration.getDamageMultiplicand();
		final int toolUsesLeft = tool.isItemStackDamageable() ? (tool.getMaxDamage() - tool.getItemDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(ToolConfiguration.isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TextComponentTranslation("chat.falling_tree.prevented_break_tool"));
				return false;
			}
		}
		tree.getTopMostFurthestLog().ifPresent(logBlock -> {
			final IBlockState logState = world.getBlockState(logBlock);
			player.addStat(StatList.getObjectBreakStats(Item.getItemFromBlock(logState.getBlock())));
			logState.getBlock().harvestBlock(world, player, tree.getHitPos(), logState, world.getTileEntity(logBlock), tool);
			world.destroyBlock(logBlock, false);
		});
		int toolDamage = damageMultiplicand;
		if(toolDamage > 0){
			tool.damageItem(toolDamage, player);
		}
		return true;
	}
}
