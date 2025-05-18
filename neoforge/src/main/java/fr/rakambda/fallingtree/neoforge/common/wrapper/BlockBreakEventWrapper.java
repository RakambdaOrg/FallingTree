package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockBreakEvent;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ToString
public class BlockBreakEventWrapper implements IBlockBreakEvent{
	@NotNull
	@Getter
	private final BlockEvent.BreakEvent raw;
	
	@Override
	@NotNull
	public IBlockPos getBlockPos(){
		return new BlockPosWrapper(raw.getPos());
	}
}
