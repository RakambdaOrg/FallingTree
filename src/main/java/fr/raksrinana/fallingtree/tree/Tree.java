package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.utils.TreePartType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.*;
import static fr.raksrinana.fallingtree.utils.TreePartType.LOG;
import static fr.raksrinana.fallingtree.utils.TreePartType.WART;
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
	
	public Optional<TreePart> getLastSequencePart(){
		return getLogs().stream()
				.max(comparingInt(TreePart::getSequence));
	}
	
	public Optional<BlockPos> getTopMostLog(){
		return getLogs().stream()
				.map(TreePart::getBlockPos)
				.max(comparingInt(BlockPos::getY));
	}
	
	public Collection<TreePart> getLogs(){
		return getParts().stream()
				.filter(part -> part.getTreePartType() == LOG)
				.collect(toSet());
	}
	
	private Optional<BlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::getBlockPos)
				.max(comparingInt(BlockPos::getY));
	}
	
	public Collection<TreePart> getWarts(){
		return getParts().stream()
				.filter(part -> part.getTreePartType() == WART)
				.collect(toSet());
	}
	
	public int getLogCount(){
		return this.partCounts.computeIfAbsent(LOG, key -> 0);
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
