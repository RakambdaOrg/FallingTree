package fr.raksrinana.fallingtree.fabric.leaves;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;

public class LeafBreakingSchedule{
	private final ServerLevel level;
	private final BlockPos blockPos;
	private int remainingTicks;
	
	public LeafBreakingSchedule(ServerLevel level, BlockPos blockPos, int remainingTicks){
		this.level = level;
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
	
	public ServerLevel getLevel(){
		return level;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof LeafBreakingSchedule that)){
			return false;
		}
		return Objects.equals(getLevel(), that.getLevel()) && Objects.equals(getBlockPos(), that.getBlockPos());
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getLevel(), getBlockPos());
	}
}
