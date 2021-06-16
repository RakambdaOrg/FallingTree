package fr.raksrinana.fallingtree.fabric.tree;

import fr.raksrinana.fallingtree.fabric.utils.TreePartType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.*;
import static fr.raksrinana.fallingtree.fabric.utils.TreePartType.LOG;
import static fr.raksrinana.fallingtree.fabric.utils.TreePartType.NETHER_WART;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class Tree{
	@Getter
	private final Level level;
	@Getter
	private final BlockPos hitPos;
	@Getter
	private final Set<TreePart> parts = new LinkedHashSet<>();
	private final Map<TreePartType, Integer> partCounts = new HashMap<>();
	
	public void addPart(TreePart treePart){
		parts.add(treePart);
		partCounts.compute(treePart.treePartType(), (key, value) -> {
			if(isNull(value)){
				return 1;
			}
			return value + 1;
		});
	}
	
	public void removePartsHigherThan(int y, TreePartType partType){
		parts.removeIf(part -> {
			if(part.treePartType() == partType && part.blockPos().getY() > y){
				decrementPartCount(partType);
				return true;
			}
			return false;
		});
	}
	
	public int getBreakableCount(){
		return Arrays.stream(TreePartType.values())
				.filter(TreePartType::isBreakable)
				.mapToInt(this::getPartCount)
				.sum();
	}
	
	private int getPartCount(TreePartType treePartType){
		return partCounts.computeIfAbsent(treePartType, key -> 0);
	}
	
	private void decrementPartCount(TreePartType partType){
		partCounts.computeIfPresent(partType, (type, count) -> Math.max(0, count - 1));
	}
	
	public Optional<TreePart> getLastSequencePart(){
		return getParts().stream()
				.max(comparingInt(TreePart::sequence));
	}
	
	public Collection<TreePart> getLogs(){
		return getParts().stream()
				.filter(part -> part.treePartType() == LOG)
				.collect(toSet());
	}
	
	public Collection<TreePart> getBreakableParts(){
		return getParts().stream()
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	public int getLogCount(){
		return getPartCount(LOG);
	}
	
	public Optional<BlockPos> getTopMostLog(){
		return getLogs().stream()
				.map(TreePart::blockPos)
				.max(comparingInt(BlockPos::getY));
	}
	
	private Optional<BlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::blockPos)
				.max(comparingInt(BlockPos::getY));
	}
	
	public Collection<TreePart> getWarts(){
		return getParts().stream()
				.filter(part -> part.treePartType() == NETHER_WART)
				.collect(toSet());
	}
}
