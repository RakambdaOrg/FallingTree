package fr.raksrinana.fallingtree.tree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class Tree{
	private final IWorld world;
	private final Set<BlockPos> logs;
	private final BlockPos hitPos;
	
	public Tree(@Nonnull IWorld world, @Nonnull BlockPos blockPos){
		this.world = world;
		this.hitPos = blockPos;
		this.logs = new HashSet<>();
	}
	
	public void addLog(@Nullable BlockPos blockPos){
		if(Objects.nonNull(blockPos)){
			this.logs.add(blockPos);
		}
	}
	
	public int getLogCount(){
		return this.logs.size();
	}
	
	@Nonnull
	public BlockPos getHitPos(){
		return this.hitPos;
	}
	
	@Nonnull
	public IWorld getWorld(){
		return this.world;
	}
	
	@Nonnull
	public Stream<BlockPos> getLogs(){
		return this.logs.stream();
	}
}
