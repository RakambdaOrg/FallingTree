package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.utils.TreePart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.*;
import java.util.stream.Stream;

public class Tree{
	private final World world;
	private final Set<BlockPos> logs;
	private final Set<BlockPos> warts;
	private final BlockPos hitPos;
	
	public Tree(World world, BlockPos blockPos){
		this.world = world;
		this.hitPos = blockPos;
		this.logs = new LinkedHashSet<>();
		this.warts = new LinkedHashSet<>();
	}
	
	public void addLog(BlockPos blockPos){
		if(Objects.nonNull(blockPos)){
			this.logs.add(blockPos);
		}
	}
	
	public void addWart(BlockPos blockPos){
		if(Objects.nonNull(blockPos)){
			this.warts.add(blockPos);
		}
	}
	
	public void addPart(TreePart treePart, BlockPos blockPos){
		switch(treePart){
			case LOG:
				addLog(blockPos);
				break;
			case WART:
				addWart(blockPos);
				break;
		}
	}
	
	public Collection<BlockPos> getWarts(){
		return this.warts;
	}
	
	public Optional<BlockPos> getTopMostFurthestPart(){
		return getTopMostPart().flatMap(topMost -> logs.stream()
				.filter(log -> Objects.equals(log.getY(), topMost.getY()))
				.max(Comparator.comparingInt(this::getDistanceFromHit)));
	}
	
	private Optional<BlockPos> getTopMostPart(){
		return getAllPartsStream().max(Comparator.comparingInt(BlockPos::getY));
	}
	
	public Optional<BlockPos> getTopMostLog(){
		return getLogs().stream().max(Comparator.comparingInt(BlockPos::getY));
	}
	
	private Stream<BlockPos> getAllPartsStream(){
		return Stream.concat(getLogs().stream(), getWarts().stream());
	}
	
	public int getDistanceFromHit(BlockPos pos){
		return Math.abs(hitPos.getX() - pos.getX()) + Math.abs(hitPos.getY() - pos.getY()) + Math.abs(hitPos.getZ() - pos.getZ());
	}
	
	public int getLogCount(){
		return this.logs.size();
	}
	
	public BlockPos getHitPos(){
		return this.hitPos;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public Collection<BlockPos> getLogs(){
		return this.logs;
	}
}
