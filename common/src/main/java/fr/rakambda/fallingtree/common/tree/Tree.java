package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class Tree{
	@Getter
	@NonNull
	private final ILevel level;
	@Getter
	@NonNull
	private final IBlockPos hitPos;
	@Getter
	private final Set<TreePart> parts = new LinkedHashSet<>();
	private final Map<TreePartType, Integer> partCounts = new LinkedHashMap<>();
	private final ExtremumBlocks extremumBlocks = new ExtremumBlocks();
	
	public void addPart(@NonNull TreePart treePart){
		parts.add(treePart);
		partCounts.compute(treePart.treePartType(), (key, value) -> {
			if(isNull(value)){
				return 1;
			}
			return value + 1;
		});
		extremumBlocks.update(treePart);
	}
	
	public void removePartsHigherThan(int y, @NonNull TreePartType partType){
		parts.removeIf(part -> {
			if(part.treePartType() == partType && part.blockPos().getY() > y){
				decrementPartCount(partType);
				return true;
			}
			return false;
		});
	}
	
	public int getBreakableCount(){
		return Arrays.stream(TreePartType.getValues())
				.filter(TreePartType::isBreakable)
				.mapToInt(this::getPartCount)
				.sum();
	}
	
	private int getPartCount(@NonNull TreePartType treePartType){
		return partCounts.computeIfAbsent(treePartType, key -> 0);
	}
	
	public int getSize(){
		return partCounts.values().stream().mapToInt(i -> i).sum();
	}
	
	private void decrementPartCount(@NonNull TreePartType partType){
		partCounts.computeIfPresent(partType, (type, count) -> Math.max(0, count - 1));
	}
	
	@NonNull
	public Optional<TreePart> getLastSequencePart(){
		return getParts().stream()
				.max(comparingInt(TreePart::sequence));
	}
	
	@NonNull
	public Optional<TreePart> getLastSequenceLogPart(){
		return getParts().stream()
				.filter(part -> part.treePartType().isLog())
				.max(comparingInt(TreePart::sequence));
	}
	
	@NonNull
	public Collection<TreePart> getBreakableLogs(){
		return getParts().stream()
				.filter(part -> part.treePartType().isLog())
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	@NonNull
	public Collection<TreePart> getBreakableParts(){
		return getParts().stream()
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	public int getLogCount(){
		return getPartCount(TreePartType.LOG);
	}
	
	private static class ExtremumBlocks {
		
		@Nullable
		private TreePart topMostLog;
		
		@Nullable
		private TreePart bottomMostLog;
		
		@Nullable
		private TreePart logStart;
		
		private void updateTopMostLog(@NonNull TreePart candidate){
			if(this.topMostLog == null){
				this.topMostLog = candidate;
				return;
			}
			
			if(candidate.treePartType().isBreakable()
					&& candidate.treePartType().isLog()
					&& candidate.blockPos().getY() > this.topMostLog.blockPos().getY()){
				this.topMostLog = candidate;
			}
		}
		
		private void updateBottomMostLog(@NonNull TreePart candidate){
			if(this.bottomMostLog == null){
				this.bottomMostLog = candidate;
				return;
			}
			
			if(candidate.treePartType().isBreakable()
					&& candidate.treePartType().isLog()
					&& candidate.blockPos().getY() < this.bottomMostLog.blockPos().getY()){
				this.bottomMostLog = candidate;
			}
		}
		
		private void updateLogStart(@NonNull TreePart candidate) {
			if(this.logStart == null && candidate.treePartType() == TreePartType.LOG_START){
				this.logStart = candidate;
			}
		}
		
		protected void update(@NonNull TreePart candidate){
			updateLogStart(candidate);
			updateTopMostLog(candidate);
			updateBottomMostLog(candidate);
		}
		
		public Optional<TreePart> getTopMostLog(){
			return Optional.ofNullable(topMostLog);
		}
		
		public Optional<TreePart> getBottomMostLog(){
			return Optional.ofNullable(bottomMostLog);
		}
		
		public Optional<TreePart> getLogStart(){
			return Optional.ofNullable(logStart);
		}
	}
	
	@NonNull
	public Optional<IBlockPos> getTopMostLog(){
		return extremumBlocks.getTopMostLog()
				.map(TreePart::blockPos);
	}
	
	@NonNull
	public Optional<IBlockPos> getBottomMostLog(){
		return extremumBlocks.getBottomMostLog()
				.map(TreePart::blockPos);
	}
	
	@NonNull
	private Optional<IBlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::blockPos)
				.max(comparingInt(IBlockPos::getY));
	}
	
	@NonNull
	public Collection<TreePart> getNetherWarts(){
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.NETHER_WART)
				.collect(toSet());
	}
	
	@NonNull
	public Collection<TreePart> getMangroveRoots(){
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.MANGROVE_ROOTS)
				.collect(toSet());
	}
	
	@NonNull
	public Optional<TreePart> getStart(){
		return extremumBlocks.getLogStart();
	}
}
