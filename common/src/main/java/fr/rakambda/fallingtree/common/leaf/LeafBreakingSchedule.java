package fr.rakambda.fallingtree.common.leaf;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
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
