package fr.raksrinana.fallingtree.fabric.tree.builder;

import fr.raksrinana.fallingtree.fabric.config.ConfigCache;
import fr.raksrinana.fallingtree.fabric.tree.Tree;
import fr.raksrinana.fallingtree.fabric.tree.builder.position.AbovePositionFetcher;
import fr.raksrinana.fallingtree.fabric.tree.builder.position.AboveYFetcher;
import fr.raksrinana.fallingtree.fabric.tree.builder.position.BasicPositionFetcher;
import fr.raksrinana.fallingtree.fabric.tree.builder.position.IPositionFetcher;
import fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils;
import fr.raksrinana.fallingtree.fabric.utils.TreePartType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static java.util.Optional.empty;

public class TreeBuilder{
	private static final EnumSet<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);
	
	public static Optional<Tree> getTree(Level level, BlockPos originPos) throws TreeTooBigException{
		var originBlock = level.getBlockState(originPos).getBlock();
		if(!FallingTreeUtils.isLogBlock(originBlock)){
			return empty();
		}
		
		var maxLogCount = config.getTreesConfiguration().getMaxSize();
		var toAnalyzePos = new PriorityQueue<ToAnalyzePos>();
		var analyzedPos = new HashSet<ToAnalyzePos>();
		var tree = new Tree(level, originPos);
		toAnalyzePos.add(new ToAnalyzePos(getFirstPositionFetcher(), originPos, originBlock, originPos, originBlock, TreePartType.LOG, 0));
		
		var boundingBoxSearch = getBoundingBoxSearch(originPos);
		var adjacentPredicate = getAdjacentPredicate();
		
		try{
			while(!toAnalyzePos.isEmpty()){
				var analyzingPos = toAnalyzePos.remove();
				tree.addPart(analyzingPos.toTreePart());
				analyzedPos.add(analyzingPos);
				
				if(tree.getLogCount() > maxLogCount){
					throw new TreeTooBigException();
				}
				
				var potentialPositions = analyzingPos.getPositionFetcher().getPositions(level, originPos, analyzingPos);
				var nextPositions = filterPotentialPos(boundingBoxSearch, adjacentPredicate, level, originPos, originBlock, analyzingPos, potentialPositions, analyzedPos);
				
				nextPositions.removeAll(analyzedPos);
				nextPositions.removeAll(toAnalyzePos);
				toAnalyzePos.addAll(nextPositions);
			}
		}
		catch(AbortSearchException e){
			return empty();
		}
		
		if(config.getTreesConfiguration().getBreakMode().shouldCheckLeavesAround()){
			var aroundRequired = config.getTreesConfiguration().getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(level, topLog) < aroundRequired)
					.orElse(true)){
				return empty();
			}
		}
		
		return Optional.of(tree);
	}
	
	private static Predicate<Block> getAdjacentPredicate(){
		var whitelist = config.getTreesConfiguration().getWhitelistedAdjacentBlocks();
		var base = ConfigCache.getInstance().getAdjacentBlocksBase();
		
		if(whitelist.isEmpty()){
			return block -> true;
		}
		return switch(config.getTreesConfiguration().getAdjacentStopMode()){
			case STOP_ALL -> block -> {
				var whitelisted = whitelist.contains(block) || base.contains(block);
				if(!whitelisted){
					throw new AbortSearchException("Found block " + block + " that isn't whitelisted");
				}
				return true;
			};
			case STOP_BRANCH -> block -> whitelist.contains(block) || base.contains(block);
		};
	}
	
	private static Predicate<BlockPos> getBoundingBoxSearch(BlockPos originPos){
		var radius = config.getTreesConfiguration().getSearchAreaRadius();
		if(radius < 0){
			return pos -> true;
		}
		
		var minX = originPos.getX() - radius;
		var maxX = originPos.getX() + radius;
		var minZ = originPos.getZ() - radius;
		var maxZ = originPos.getZ() + radius;
		
		return pos -> minX <= pos.getX()
				&& maxX >= pos.getX()
				&& minZ <= pos.getZ()
				&& maxZ >= pos.getZ();
	}
	
	private static IPositionFetcher getFirstPositionFetcher(){
		var detectionMode = config.getTreesConfiguration().getDetectionMode();
		return switch(detectionMode){
			case ABOVE_CUT -> AbovePositionFetcher.getInstance();
			case ABOVE_Y -> AboveYFetcher.getInstance();
			case WHOLE_TREE -> BasicPositionFetcher.getInstance();
		};
	}
	
	private static Collection<ToAnalyzePos> filterPotentialPos(Predicate<BlockPos> boundingBoxSearch,
			Predicate<Block> adjacentPredicate,
			Level level,
			BlockPos originPos,
			Block originBlock,
			ToAnalyzePos parent,
			Collection<ToAnalyzePos> potentialPos,
			Collection<ToAnalyzePos> analyzedPos){
		return potentialPos.stream()
				.filter(pos -> !analyzedPos.contains(pos))
				.filter(pos -> shouldIncludeInChain(boundingBoxSearch, originPos, originBlock, parent, pos))
				.filter(pos -> EnumSet.allOf(Direction.class).stream()
						.map(direction -> pos.getCheckPos().relative(direction))
						.map(level::getBlockState)
						.map(BlockState::getBlock)
						.allMatch(adjacentPredicate))
				.collect(Collectors.toList());
	}
	
	private static long getLeavesAround(Level world, BlockPos blockPos){
		return ALL_DIRECTIONS.stream()
				.map(blockPos::relative)
				.filter(testPos -> {
					var block = world.getBlockState(testPos).getBlock();
					return FallingTreeUtils.isLeafBlock(block) || FallingTreeUtils.isNetherWartOrShroomlight(block) || FallingTreeUtils.isLeafNeedBreakBlock(block);
				})
				.count();
	}
	
	private static boolean shouldIncludeInChain(Predicate<BlockPos> boundingBoxSearch, BlockPos originPos, Block originBlock, ToAnalyzePos parent, ToAnalyzePos check){
		if(parent.getTreePartType() == TreePartType.LOG && isSameTree(originBlock, check) && boundingBoxSearch.test(check.getCheckPos())){
			return true;
		}
		if(config.getTreesConfiguration().isBreakNetherTreeWarts()){
			if(check.getTreePartType() == TreePartType.NETHER_WART){
				var checkBlockPos = check.getCheckPos();
				var dx = Math.abs(originPos.getX() - checkBlockPos.getX());
				var dz = Math.abs(originPos.getZ() - checkBlockPos.getZ());
				return dx <= 4 && dz <= 4;
			}
		}
		return check.getTreePartType() == TreePartType.LEAF_NEED_BREAK;
	}
	
	private static boolean isSameTree(Block parentLogBlock, ToAnalyzePos check){
		if(config.getTreesConfiguration().isAllowMixedLogs()){
			return check.getTreePartType() == TreePartType.LOG;
		}
		else{
			return check.getCheckBlock().equals(parentLogBlock);
		}
	}
}
