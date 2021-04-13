package fr.raksrinana.fallingtree.fabric.tree;

import fr.raksrinana.fallingtree.fabric.utils.TreePartType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.*;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toSet;

public class Tree{
	private final World world;
	private final Set<TreePart> parts;
	private final Map<TreePartType, Integer> partCounts;
	private final BlockPos hitPos;
	
	public Tree(World world, BlockPos blockPos){
		this.world = world;
		this.hitPos = blockPos;
		this.parts = new LinkedHashSet<>();
		this.partCounts = new HashMap<>();
	}
	
	public void addPart(TreePart treePart){
		this.parts.add(treePart);
		this.partCounts.compute(treePart.getTreePartType(), (key, value) -> {
			if(Objects.isNull(value)){
				return 1;
			}
			return value + 1;
		});
	}
	
	public int getBreakableCount(){
		return Arrays.stream(TreePartType.values())
				.filter(TreePartType::isBreakable)
				.mapToInt(this::getPartCount)
				.sum();
	}
	
	private int getPartCount(TreePartType treePartType){
		return this.partCounts.computeIfAbsent(treePartType, key -> 0);
	}
	
	public Optional<TreePart> getLastSequencePart(){
		return getParts().stream()
				.max(comparingInt(TreePart::getSequence));
	}
	
	public Collection<TreePart> getLogs(){
		return getParts().stream()
				.filter(part -> part.getTreePartType() == TreePartType.LOG)
				.collect(toSet());
	}
	
	public Collection<TreePart> getBreakableParts(){
		return getParts().stream()
				.filter(part -> part.getTreePartType().isBreakable())
				.collect(toSet());
	}
	
	public int getLogCount(){
		return getPartCount(TreePartType.LOG);
	}
	
	public Optional<BlockPos> getTopMostLog(){
		return getLogs().stream()
				.map(TreePart::getBlockPos)
				.max(comparingInt(BlockPos::getY));
	}
	
	private Optional<BlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::getBlockPos)
				.max(comparingInt(BlockPos::getY));
	}
	
	public Collection<TreePart> getWarts(){
		return getParts().stream()
				.filter(part -> part.getTreePartType() == TreePartType.NETHER_WART)
				.collect(toSet());
	}
	
	public BlockPos getHitPos(){
		return this.hitPos;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public Collection<TreePart> getParts(){
		return this.parts;
	}
}
