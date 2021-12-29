package fr.raksrinana.fallingtree.forge.tree.builder;

import fr.raksrinana.fallingtree.forge.config.ConfigCache;
import fr.raksrinana.fallingtree.forge.config.Configuration;
import fr.raksrinana.fallingtree.forge.tree.Tree;
import fr.raksrinana.fallingtree.forge.tree.builder.position.AbovePositionFetcher;
import fr.raksrinana.fallingtree.forge.tree.builder.position.AboveYFetcher;
import fr.raksrinana.fallingtree.forge.tree.builder.position.BasicPositionFetcher;
import fr.raksrinana.fallingtree.forge.tree.builder.position.IPositionFetcher;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import fr.raksrinana.fallingtree.forge.utils.TreePartType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.forge.FallingTree.logger;
import static fr.raksrinana.fallingtree.forge.utils.TreePartType.LOG;
import static fr.raksrinana.fallingtree.forge.utils.TreePartType.NETHER_WART;
import static java.util.Optional.empty;

public class TreeBuilder{
	private static final EnumSet<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);
	
	public static Optional<Tree> getTree(Player player, Level level, BlockPos originPos) throws TreeTooBigException{
		var originBlock = level.getBlockState(originPos).getBlock();
		if(!FallingTreeUtils.isLogBlock(originBlock)){
			return empty();
		}
		
		var maxScanSize = Configuration.getInstance().getTrees().getMaxScanSize();
		var toAnalyzePos = new PriorityQueue<ToAnalyzePos>();
		var analyzedPos = new HashSet<ToAnalyzePos>();
		var tree = new Tree(level, originPos);
		toAnalyzePos.add(new ToAnalyzePos(getFirstPositionFetcher(), originPos, originBlock, originPos, originBlock, LOG, 0));
		
		var boundingBoxSearch = getBoundingBoxSearch(originPos);
		var adjacentPredicate = getAdjacentPredicate();
		
		try{
			checkAdjacent(adjacentPredicate, level, originPos);
			
			while(!toAnalyzePos.isEmpty()){
				var analyzingPos = toAnalyzePos.remove();
				tree.addPart(analyzingPos.toTreePart());
				analyzedPos.add(analyzingPos);
				
				if(tree.getSize() > maxScanSize){
					logger.debug("Tree at {} reached max scan size of {}", tree.getHitPos(), maxScanSize);
					throw new TreeTooBigException();
				}
				
				var potentialPositions = analyzingPos.positionFetcher().getPositions(level, originPos, analyzingPos);
				var nextPositions = filterPotentialPos(boundingBoxSearch, adjacentPredicate, level, originPos, originBlock, analyzingPos, potentialPositions, analyzedPos);
				
				nextPositions.removeAll(analyzedPos);
				nextPositions.removeAll(toAnalyzePos);
				toAnalyzePos.addAll(nextPositions);
			}
			
			postProcess(tree);
		}
		catch(AbortSearchException e){
			logger.debug("Didn't cut tree at {}, reason: {}", originPos, e.getMessage());
			FallingTreeUtils.notifyPlayer(player, new TranslatableComponent("chat.fallingtree.search_aborted").append(e.getComponent()));
			return empty();
		}
		
		if(Configuration.getInstance().getTrees().getBreakMode().isCheckLeavesAround()){
			var aroundRequired = Configuration.getInstance().getTrees().getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(level, topLog) < aroundRequired)
					.orElse(true)){
				logger.debug("Tree at {} doesn't have enough leaves around top most log", originPos);
				return empty();
			}
		}
		
		return Optional.of(tree);
	}
	
	private static void postProcess(Tree tree){
		tree.getTopMostLog().ifPresent(topMostLog -> tree.removePartsHigherThan(topMostLog.getY() + 1, NETHER_WART));
	}
	
	private static Predicate<Block> getAdjacentPredicate(){
		var allowed = Configuration.getInstance().getTrees().getAllowedAdjacentBlockBlocks();
		var base = ConfigCache.getInstance().getAdjacentBlocksBase();
		
		if(allowed.isEmpty()){
			return block -> true;
		}
		return switch(Configuration.getInstance().getTrees().getAdjacentStopMode()){
			case STOP_ALL -> block -> {
				var isAllowed = allowed.contains(block) || base.contains(block);
				if(!isAllowed){
					throw new AdjacentAbortSearchException(block);
				}
				return true;
			};
			case STOP_BRANCH -> block -> {
				var isAllowed = allowed.contains(block) || base.contains(block);
				if(!isAllowed){
					logger.debug("Found block {} that isn't allowed in the adjacent blocks, branch will be ignored further", block);
					return false;
				}
				return true;
			};
		};
	}
	
	private static Predicate<BlockPos> getBoundingBoxSearch(BlockPos originPos){
		var radius = Configuration.getInstance().getTrees().getSearchAreaRadius();
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
		var detectionMode = Configuration.getInstance().getTrees().getDetectionMode();
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
				.filter(pos -> checkAdjacent(adjacentPredicate, level, pos.checkPos()))
				.collect(Collectors.toList());
	}
	
	private static boolean checkAdjacent(Predicate<Block> adjacentPredicate, Level level, BlockPos pos){
		return EnumSet.allOf(Direction.class).stream()
				.map(pos::relative)
				.map(level::getBlockState)
				.map(BlockState::getBlock)
				.allMatch(adjacentPredicate);
	}
	
	private static long getLeavesAround(Level level, BlockPos blockPos){
		return ALL_DIRECTIONS.stream()
				.map(blockPos::relative)
				.filter(testPos -> {
					var block = level.getBlockState(testPos).getBlock();
					return FallingTreeUtils.isLeafBlock(block) || FallingTreeUtils.isNetherWartOrShroomlight(block) || FallingTreeUtils.isLeafNeedBreakBlock(block);
				})
				.count();
	}
	
	private static boolean shouldIncludeInChain(Predicate<BlockPos> boundingBoxSearch, BlockPos originPos, Block originBlock, ToAnalyzePos parent, ToAnalyzePos check){
		if(parent.treePartType() == LOG && isSameTree(originBlock, check) && boundingBoxSearch.test(check.checkPos())){
			return true;
		}
		if(Configuration.getInstance().getTrees().isBreakNetherTreeWarts()){
			if(check.treePartType() == NETHER_WART){
				var checkBlockPos = check.checkPos();
				var dx = Math.abs(originPos.getX() - checkBlockPos.getX());
				var dz = Math.abs(originPos.getZ() - checkBlockPos.getZ());
				return dx <= 4 && dz <= 4;
			}
		}
		return check.treePartType() == TreePartType.LEAF_NEED_BREAK;
	}
	
	private static boolean isSameTree(Block parentLogBlock, ToAnalyzePos check){
		if(Configuration.getInstance().getTrees().isAllowMixedLogs()){
			return check.treePartType() == LOG;
		}
		else{
			return check.checkBlock().equals(parentLogBlock);
		}
	}
}
