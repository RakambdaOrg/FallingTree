package fr.raksrinana.fallingtree.forge.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import java.util.Objects;

public class LeafBreakingSchedule{
	private final ServerWorld world;
	private final BlockPos blockPos;
	private int remainingTicks;
	
	public LeafBreakingSchedule(ServerWorld world, BlockPos blockPos, int remainingTicks){
		this.world = world;
		this.blockPos = blockPos;
		this.remainingTicks = remainingTicks;
	}
	
	public void tick(){
		this.remainingTicks--;
	}
	
	public int getRemainingTicks(){
		return remainingTicks;
	}
	
	public BlockPos getBlockPos(){
		return blockPos;
	}
	
	public ServerWorld getWorld(){
		return world;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof LeafBreakingSchedule)){
			return false;
		}
		LeafBreakingSchedule that = (LeafBreakingSchedule) o;
		return Objects.equals(getWorld(), that.getWorld()) && Objects.equals(getBlockPos(), that.getBlockPos());
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getWorld(), getBlockPos());
	}
}
