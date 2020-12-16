package fr.raksrinana.fallingtree.tree.builder;

import fr.raksrinana.fallingtree.config.Config;
import fr.raksrinana.fallingtree.config.DetectionMode;
import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.builder.position.AbovePositionFetcher;
import fr.raksrinana.fallingtree.tree.builder.position.AboveYFetcher;
import fr.raksrinana.fallingtree.tree.builder.position.BasicPositionFetcher;
import fr.raksrinana.fallingtree.tree.builder.position.IPositionFetcher;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.*;
import static fr.raksrinana.fallingtree.config.DetectionMode.ABOVE_CUT;
import static fr.raksrinana.fallingtree.config.DetectionMode.ABOVE_Y;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.*;
import static fr.raksrinana.fallingtree.utils.TreePartType.LOG;
import static fr.raksrinana.fallingtree.utils.TreePartType.WART;
import static java.util.Optional.empty;

public class TreeBuilder{
	private static final EnumSet<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);
	
	public static Optional<Tree> getTree(World world, BlockPos originPos){
		Block originBlock = world.getBlockState(originPos).getBlock();
		if(!isLogBlock(originBlock)){
			return empty();
		}
		
		Queue<ToAnalyzePos> toAnalyzePos = new PriorityQueue<>();
		Set<ToAnalyzePos> analyzedPos = new HashSet<>();
		Tree tree = new Tree(world, originPos);
		toAnalyzePos.add(new ToAnalyzePos(getFirstPositionFetcher(), originPos, originBlock, originPos, originBlock, LOG, 0));
		
		while(!toAnalyzePos.isEmpty()){
			ToAnalyzePos analyzingPos = toAnalyzePos.remove();
			tree.addPart(analyzingPos.toTreePart());
			analyzedPos.add(analyzingPos);
			
			Collection<ToAnalyzePos> potentialPositions = analyzingPos.getPositionFetcher().getPositions(world, originPos, analyzingPos);
			Collection<ToAnalyzePos> nextPositions = filterPotentialPos(originPos, originBlock, analyzingPos, potentialPositions, analyzedPos);
			
			nextPositions.removeAll(analyzedPos);
			nextPositions.removeAll(toAnalyzePos);
			toAnalyzePos.addAll(nextPositions);
		}
		
		if(Config.COMMON.getTreesConfiguration().getBreakMode().shouldCheckLeavesAround()){
			int aroundRequired = Config.COMMON.getTreesConfiguration().getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(world, topLog) < aroundRequired)
					.orElse(true)){
				return empty();
			}
		}
		
		return Optional.of(tree);
	}
	
	private static IPositionFetcher getFirstPositionFetcher(){
		DetectionMode detectionMode = Config.COMMON.getTreesConfiguration().getDetectionMode();
		if(detectionMode == ABOVE_CUT){
			return AbovePositionFetcher.getInstance();
		}
		if(detectionMode == ABOVE_Y){
			return AboveYFetcher.getInstance();
		}
		return BasicPositionFetcher.getInstance();
	}
	
	private static Collection<ToAnalyzePos> filterPotentialPos(BlockPos originPos, Block originBlock, ToAnalyzePos parent, Collection<ToAnalyzePos> potentialPos, Collection<ToAnalyzePos> analyzedPos){
		List<ToAnalyzePos> filtered = new LinkedList<>();
		potentialPos.forEach(pos -> {
			if(!analyzedPos.contains(pos) && shouldIncludeInChain(originPos, originBlock, parent, pos)){
				filtered.add(pos);
			}
		});
		return filtered;
	}
	
	private static long getLeavesAround(World world, BlockPos blockPos){
		return ALL_DIRECTIONS.stream()
				.map(blockPos::offset)
				.filter(testPos -> {
					Block block = world.getBlockState(testPos).getBlock();
					return isLeafBlock(block) || isNetherWartOrShroomlight(block);
				})
				.count();
	}
	
	private static boolean shouldIncludeInChain(BlockPos originPos, Block originBlock, ToAnalyzePos parent, ToAnalyzePos check){
		if(parent.getTreePartType() == LOG && isSameTree(originBlock, check)){
			return true;
		}
		if(Config.COMMON.getTreesConfiguration().isBreakNetherTreeWarts()){
			if(check.getTreePartType() == WART){
				BlockPos checkBlockPos = check.getCheckPos();
				int dx = Math.abs(originPos.getX() - checkBlockPos.getX());
				int dz = Math.abs(originPos.getZ() - checkBlockPos.getZ());
				return dx <= 4 && dz <= 4;
			}
		}
		return false;
	}
	
	private static boolean isSameTree(Block parentLogBlock, ToAnalyzePos check){
		if(Config.COMMON.getTreesConfiguration().isAllowMixedLogs()){
			return check.getTreePartType() == LOG;
		}
		else{
			return check.getCheckBlock().equals(parentLogBlock);
		}
	}
}
