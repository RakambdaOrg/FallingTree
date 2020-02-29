package fr.raksrinana.fallingtree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import java.util.Objects;

public class ScheduledLeafBreak{
	private final ServerWorld world;
	private final BlockPos blockPos;
	private int remainingTicks;
	
	public ScheduledLeafBreak(ServerWorld world, BlockPos blockPos, int remainingTicks){
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
		if(!(o instanceof ScheduledLeafBreak)){
			return false;
		}
		ScheduledLeafBreak that = (ScheduledLeafBreak) o;
		return Objects.equals(getWorld(), that.getWorld()) && Objects.equals(getBlockPos(), that.getBlockPos());
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getWorld(), getBlockPos());
	}
}
