package fr.raksrinana.fallingtree.fabric.leaves;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;

public class LeafBreakingSchedule{
	private final ServerLevel world;
	private final BlockPos blockPos;
	private int remainingTicks;
	
	public LeafBreakingSchedule(ServerLevel world, BlockPos blockPos, int remainingTicks){
		this.world = world;
		this.blockPos = blockPos;
		this.remainingTicks = remainingTicks;
	}
	
	public void tick(){
		remainingTicks--;
	}
	
	public int getRemainingTicks(){
		return remainingTicks;
	}
	
	public BlockPos getBlockPos(){
		return blockPos;
	}
	
	public ServerLevel getWorld(){
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
