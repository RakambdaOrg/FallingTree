package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.tree.TreePart;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
class MutableTree implements Tree{
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
	
	void addPart(@NonNull TreePart treePart){
		parts.add(treePart);
		partCounts.compute(treePart.treePartType(), (key, value) -> {
			if(isNull(value)){
				return 1;
			}
			return value + 1;
		});
	}
	
	void removePartsHigherThan(int y, @NonNull TreePartType partType){
		parts.removeIf(part -> {
			if(part.treePartType() == partType && part.blockPos().getY() > y){
				decrementPartCount(partType);
				this.extremumBlocks.onRemoved(part);
				return true;
			}
			return false;
		});
	}
	
	@Override
	public int getBreakableCount(){
		return Arrays.stream(TreePartType.getValues())
				.filter(TreePartType::isBreakable)
				.mapToInt(this::getPartCount)
				.sum();
	}
	
	private int getPartCount(@NonNull TreePartType treePartType){
		return partCounts.computeIfAbsent(treePartType, key -> 0);
	}
	
	int getSize(){
		return partCounts.values().stream().mapToInt(i -> i).sum();
	}
	
	private void decrementPartCount(@NonNull TreePartType partType){
		partCounts.computeIfPresent(partType, (type, count) -> Math.max(0, count - 1));
	}
	
	@Override
	public @NonNull Optional<TreePart> getLastSequencePart(){
		return getParts().stream()
				.max(comparingInt(TreePart::sequence));
	}
	
	@Override
	public @NonNull Optional<TreePart> getLastSequenceLogPart(){
		return getParts().stream()
				.filter(part -> part.treePartType().isLog())
				.max(comparingInt(TreePart::sequence));
	}
	
	@Override
	public @NonNull Collection<TreePart> getBreakableLogs(){
		return getParts().stream()
				.filter(part -> part.treePartType().isLog())
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	@Override
	public @NonNull Collection<TreePart> getBreakableParts(){
		return getParts().stream()
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	@Override
	public int getLogCount(){
		return getPartCount(TreePartType.LOG);
	}
	
	private class ExtremumBlocks{
		
		@Nullable
		private TreePart topMostLog = null;
		
		@Nullable
		private TreePart bottomMostLog = null;
		
		@Nullable
		private TreePart logStart = null;
		
		public Optional<TreePart> getTopMostLog(){
			if(topMostLog != null){
				return Optional.of(topMostLog);
			}
			
			this.topMostLog = parts.stream()
					.filter(treePart -> treePart.treePartType() == TreePartType.LOG)
					.max(Comparator.comparing(treePart -> treePart.blockPos().getY()))
					.orElse(null);
			
			return Optional.ofNullable(topMostLog);
		}
		
		public Optional<TreePart> getBottomMostLog(){
			if(bottomMostLog != null){
				return Optional.of(bottomMostLog);
			}
			
			this.bottomMostLog = parts.stream()
					.filter(treePart -> treePart.treePartType() == TreePartType.LOG)
					.min(Comparator.comparing(treePart -> treePart.blockPos().getY()))
					.orElse(null);
			
			return Optional.ofNullable(bottomMostLog);
		}
		
		public Optional<TreePart> getLogStart(){
			if(logStart != null){
				return Optional.of(logStart);
			}
			
			this.logStart = parts.stream()
					.filter(treePart -> treePart.treePartType() == TreePartType.LOG_START)
					.findFirst()
					.orElse(null);
			
			return Optional.ofNullable(logStart);
		}
		
		public void onRemoved(@NonNull final TreePart removedTreePart){
			if(topMostLog != null && topMostLog.equals(removedTreePart)){
				this.topMostLog = null;
			}
			
			if(bottomMostLog != null && bottomMostLog.equals(removedTreePart)){
				this.bottomMostLog = null;
			}
			
			if(logStart != null && logStart.equals(removedTreePart)){
				this.logStart = null;
			}
		}
	}
	
	@Override
	public @NonNull Optional<IBlockPos> getTopMostLog(){
		return extremumBlocks.getTopMostLog()
				.map(TreePart::blockPos);
	}
	
	@Override
	public @NonNull Optional<IBlockPos> getBottomMostLog(){
		return extremumBlocks.getBottomMostLog()
				.map(TreePart::blockPos);
	}
	
	@Override
	public @NonNull Optional<IBlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::blockPos)
				.max(comparingInt(IBlockPos::getY));
	}
	
	@Override
	public @NonNull Collection<TreePart> getNetherWarts(){
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.NETHER_WART)
				.collect(toSet());
	}
	
	@Override
	public @NonNull Collection<TreePart> getMangroveRoots(){
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.MANGROVE_ROOTS)
				.collect(toSet());
	}
	
	@Override
	public @NonNull Optional<TreePart> getStart(){
		return extremumBlocks.getLogStart();
	}
	
	@Override
	public @NonNull Stream<TreePart> getPartsStream(){
		return getParts().stream();
	}
}
