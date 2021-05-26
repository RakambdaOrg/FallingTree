package fr.raksrinana.fallingtree.fabric.leaves;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class LeafBreakingSchedule{
	private final ServerLevel level;
	private final BlockPos blockPos;
	private int remainingTicks;
	
	public void tick(){
		remainingTicks--;
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
