package fr.raksrinana.fallingtree.common.leaf;

import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.IServerLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LeafBreakingSchedule{
	private final IServerLevel level;
	private final IBlockPos blockPos;
	@EqualsAndHashCode.Exclude
	private int remainingTicks;
	
	public void tick(){
		remainingTicks--;
	}
}
