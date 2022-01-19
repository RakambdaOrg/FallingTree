package fr.raksrinana.fallingtree.common.tree.builder;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.tree.Tree;
import fr.raksrinana.fallingtree.common.tree.TreePartType;
import fr.raksrinana.fallingtree.common.tree.builder.position.AbovePositionFetcher;
import fr.raksrinana.fallingtree.common.tree.builder.position.AboveYFetcher;
import fr.raksrinana.fallingtree.common.tree.builder.position.BasicPositionFetcher;
import fr.raksrinana.fallingtree.common.tree.builder.position.IPositionFetcher;
import fr.raksrinana.fallingtree.common.wrapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.Optional.empty;

@RequiredArgsConstructor
@Slf4j
public class TreeBuilder{
	private static final EnumSet<DirectionCompat> ALL_DIRECTIONS = EnumSet.allOf(DirectionCompat.class);
	
	private final FallingTreeCommon<?> mod;
	
	@NotNull
	public Optional<Tree> getTree(@NotNull IPlayer player, @NotNull ILevel level, @NotNull IBlockPos originPos) throws TreeTooBigException{
		var originBlock = level.getBlockState(originPos).getBlock();
		if(!mod.isLogBlock(originBlock)){
			return empty();
		}
		
		var maxScanSize = mod.getConfiguration().getTrees().getMaxScanSize();
		var toAnalyzePos = new PriorityQueue<ToAnalyzePos>();
		var analyzedPos = new HashSet<ToAnalyzePos>();
		var tree = new Tree(level, originPos);
		toAnalyzePos.add(new ToAnalyzePos(getFirstPositionFetcher(), originPos, originBlock, originPos, originBlock, TreePartType.LOG, 0));
		
		var boundingBoxSearch = getBoundingBoxSearch(originPos);
		var adjacentPredicate = getAdjacentPredicate();
		
		try{
			checkAdjacent(adjacentPredicate, level, originPos);
			
			while(!toAnalyzePos.isEmpty()){
				var analyzingPos = toAnalyzePos.remove();
				tree.addPart(analyzingPos.toTreePart());
				analyzedPos.add(analyzingPos);
				
				if(tree.getSize() > maxScanSize){
					log.debug("Tree at {} reached max scan size of {}", tree.getHitPos(), maxScanSize);
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
			log.debug("Didn't cut tree at {}, reason: {}", originPos, e.getMessage());
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.search_aborted").append(e.getComponent()));
			return empty();
		}
		
		if(mod.getConfiguration().getTrees().getBreakMode().isCheckLeavesAround()){
			var aroundRequired = mod.getConfiguration().getTrees().getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(level, topLog) < aroundRequired)
					.orElse(true)){
				log.debug("Tree at {} doesn't have enough leaves around top most log", originPos);
				return empty();
			}
		}
		
		return Optional.of(tree);
	}
	
	private static void postProcess(@NotNull Tree tree){
		tree.getTopMostLog().ifPresent(topMostLog -> tree.removePartsHigherThan(topMostLog.getY() + 1, TreePartType.NETHER_WART));
	}
	
	@NotNull
	private Predicate<IBlock> getAdjacentPredicate(){
		var allowedList = mod.getConfiguration().getTrees().getAllowedAdjacentBlockBlocks(mod);
		var base = mod.getConfiguration().getTrees().getAllAllowedAdjacentBlockBlocks(mod);
		
		if(allowedList.isEmpty()){
			return block -> true;
		}
		return switch(mod.getConfiguration().getTrees().getAdjacentStopMode()){
			case STOP_ALL -> block -> {
				var isAllowed = allowedList.contains(block) || base.contains(block);
				if(!isAllowed){
					throw new AdjacentAbortSearchException(block, mod);
				}
				return true;
			};
			case STOP_BRANCH -> block -> {
				var isAllowed = allowedList.contains(block) || base.contains(block);
				if(!isAllowed){
					log.debug("Found block {} that isn't allowed in the adjacent blocks, branch will be ignored further", block);
					return false;
				}
				return true;
			};
		};
	}
	
	@NotNull
	private Predicate<IBlockPos> getBoundingBoxSearch(@NotNull IBlockPos originPos){
		var radius = mod.getConfiguration().getTrees().getSearchAreaRadius();
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
	
	@NotNull
	private IPositionFetcher getFirstPositionFetcher(){
		var detectionMode = mod.getConfiguration().getTrees().getDetectionMode();
		return switch(detectionMode){
			case ABOVE_CUT -> AbovePositionFetcher.getInstance(mod);
			case ABOVE_Y -> AboveYFetcher.getInstance(mod);
			case WHOLE_TREE -> BasicPositionFetcher.getInstance(mod);
		};
	}
	
	@NotNull
	private Collection<ToAnalyzePos> filterPotentialPos(@NotNull Predicate<IBlockPos> boundingBoxSearch,
			@NotNull Predicate<IBlock> adjacentPredicate,
			@NotNull ILevel level,
			@NotNull IBlockPos originPos,
			@NotNull IBlock originBlock,
			@NotNull ToAnalyzePos parent,
			@NotNull Collection<ToAnalyzePos> potentialPos,
			@NotNull Collection<ToAnalyzePos> analyzedPos){
		return potentialPos.stream()
				.filter(pos -> !analyzedPos.contains(pos))
				.filter(pos -> shouldIncludeInChain(boundingBoxSearch, originPos, originBlock, parent, pos))
				.filter(pos -> checkAdjacent(adjacentPredicate, level, pos.checkPos()))
				.collect(Collectors.toList());
	}
	
	private static boolean checkAdjacent(@NotNull Predicate<IBlock> adjacentPredicate, @NotNull ILevel level, IBlockPos pos){
		return EnumSet.allOf(DirectionCompat.class).stream()
				.map(pos::relative)
				.map(level::getBlockState)
				.map(IBlockState::getBlock)
				.allMatch(adjacentPredicate);
	}
	
	private long getLeavesAround(@NotNull ILevel level, @NotNull IBlockPos blockPos){
		return ALL_DIRECTIONS.stream()
				.map(blockPos::relative)
				.filter(testPos -> {
					var blockState = level.getBlockState(testPos);
					var block = blockState.getBlock();
					var isLeaf = mod.isLeafBlock(block) || mod.isNetherWartOrShroomlight(block) || mod.isLeafNeedBreakBlock(block);
					if(!isLeaf){
						return false;
					}
					
					if(mod.getConfiguration().getTrees().isIncludePersistentLeavesInRequiredCount()){
						return true;
					}
					
					return !blockState.hasLeafPersistentFlag().orElse(false);
				})
				.count();
	}
	
	private boolean shouldIncludeInChain(@NotNull Predicate<IBlockPos> boundingBoxSearch, @NotNull IBlockPos originPos, @NotNull IBlock originBlock, @NotNull ToAnalyzePos parent, @NotNull ToAnalyzePos check){
		if(parent.treePartType() == TreePartType.LOG && isSameTree(originBlock, check) && boundingBoxSearch.test(check.checkPos())){
			return true;
		}
		if(mod.getConfiguration().getTrees().isBreakNetherTreeWarts()){
			if(check.treePartType() == TreePartType.NETHER_WART){
				var checkBlockPos = check.checkPos();
				var dx = Math.abs(originPos.getX() - checkBlockPos.getX());
				var dz = Math.abs(originPos.getZ() - checkBlockPos.getZ());
				return dx <= 4 && dz <= 4;
			}
		}
		return check.treePartType() == TreePartType.LEAF_NEED_BREAK;
	}
	
	private boolean isSameTree(@NotNull IBlock parentLogBlock, @NotNull ToAnalyzePos check){
		if(mod.getConfiguration().getTrees().isAllowMixedLogs()){
			return check.treePartType() == TreePartType.LOG;
		}
		else{
			return check.checkBlock().equals(parentLogBlock);
		}
	}
}
