package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.utils.TreePartType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.*;
import java.util.stream.Collectors;

public class Tree{
	private final World world;
	private final Set<TreePart> parts;
	private final BlockPos hitPos;
	
	public Tree(World world, BlockPos blockPos){
		this.world = world;
		this.hitPos = blockPos;
		this.parts = new LinkedHashSet<>();
	}
	
	public void addPart(TreePart treePart){
		this.parts.add(treePart);
	}
	
	public Optional<TreePart> getLastSequencePart(){
		return getParts().stream()
				.max(Comparator.comparingInt(TreePart::getSequence));
	}
	
	public Collection<TreePart> getWarts(){
		return getParts().stream()
				.filter(part -> part.getTreePartType() == TreePartType.WART)
				.collect(Collectors.toSet());
	}
	
	public Collection<TreePart> getLogs(){
		return getParts().stream()
				.filter(part -> part.getTreePartType() == TreePartType.LOG)
				.collect(Collectors.toSet());
	}
	
	private Optional<BlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::getBlockPos)
				.max(Comparator.comparingInt(BlockPos::getY));
	}
	
	public Optional<BlockPos> getTopMostLog(){
		return getLogs().stream()
				.map(TreePart::getBlockPos)
				.max(Comparator.comparingInt(BlockPos::getY));
	}
	
	public int getLogCount(){
		return this.getLogs().size();
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
