package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
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
	
	public void addPart(@NonNull TreePart treePart){
		parts.add(treePart);
		partCounts.compute(treePart.treePartType(), (key, value) -> {
			if(isNull(value)){
				return 1;
			}
			return value + 1;
		});
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
	
	@NonNull
	public Optional<IBlockPos> getTopMostLog(){
		return getBreakableLogs().stream()
				.map(TreePart::blockPos)
				.max(comparingInt(IBlockPos::getY));
	}
	
	@NonNull
	public Optional<IBlockPos> getBottomMostLog(){
		return getBreakableLogs().stream()
				.map(TreePart::blockPos)
				.min(comparingInt(IBlockPos::getY));
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
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.LOG_START)
				.findFirst();
	}
}
