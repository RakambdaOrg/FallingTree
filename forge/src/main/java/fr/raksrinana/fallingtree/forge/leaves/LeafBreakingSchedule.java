package fr.raksrinana.fallingtree.forge.leaves;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LeafBreakingSchedule{
	private final ServerLevel level;
	private final BlockPos blockPos;
	@EqualsAndHashCode.Exclude
	private int remainingTicks;
	
	public void tick(){
		remainingTicks--;
	}
}
