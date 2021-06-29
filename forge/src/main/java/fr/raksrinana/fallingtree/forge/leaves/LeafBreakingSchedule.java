package fr.raksrinana.fallingtree.forge.leaves;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LeafBreakingSchedule{
	private final ServerWorld level;
	private final BlockPos blockPos;
	@EqualsAndHashCode.Exclude
	private int remainingTicks;
	
	public void tick(){
		remainingTicks--;
	}
}
