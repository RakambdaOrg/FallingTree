package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockBreakEvent;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
@ToString
public class BlockBreakEventWrapper implements IBlockBreakEvent{
	@Getter
	private final BlockEvent.@NonNull BreakEvent raw;
	
	@Override
	@NonNull
	public IBlockPos getBlockPos(){
		return new BlockPosWrapper(raw.getPos());
	}
}
