package fr.raksrinana.fallingtree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import java.util.Objects;

public class LeafBreakingSchedule{
	private final WorldServer world;
	private final BlockPos blockPos;
	private int remainingTicks;
	
	public LeafBreakingSchedule(WorldServer world, BlockPos blockPos, int remainingTicks){
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
	
	public WorldServer getWorld(){
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
