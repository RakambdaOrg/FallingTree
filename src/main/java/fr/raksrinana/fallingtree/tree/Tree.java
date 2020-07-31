package fr.raksrinana.fallingtree.tree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Tree{
	private final World world;
	private final Set<BlockPos> logs;
	private final BlockPos hitPos;
	
	public Tree(@Nonnull World world, @Nonnull BlockPos blockPos){
		this.world = world;
		this.hitPos = blockPos;
		this.logs = new LinkedHashSet<>();
	}
	
	public void addLog(@Nullable BlockPos blockPos){
		if(Objects.nonNull(blockPos)){
			this.logs.add(blockPos);
		}
	}
	
	public int getLogCount(){
		return this.logs.size();
	}
	
	public Optional<BlockPos> getTopMostFurthestLog(){
		return getTopMostLog().flatMap(topMost -> logs.stream()
				.filter(log -> Objects.equals(log.getY(), topMost.getY()))
				.max(Comparator.comparingInt(this::getDistanceFromHit)));
	}
	
	public Optional<BlockPos> getTopMostLog(){
		return logs.stream().max(Comparator.comparingInt(BlockPos::getY));
	}
	
	public int getDistanceFromHit(BlockPos pos){
		return Math.abs(hitPos.getX() - pos.getX()) + Math.abs(hitPos.getY() - pos.getY()) + Math.abs(hitPos.getZ() - pos.getZ());
	}
	
	@Nonnull
	public BlockPos getHitPos(){
		return this.hitPos;
	}
	
	@Nonnull
	public World getWorld(){
		return this.world;
	}
	
	@Nonnull
	public Collection<BlockPos> getLogs(){
		return this.logs;
	}
}
