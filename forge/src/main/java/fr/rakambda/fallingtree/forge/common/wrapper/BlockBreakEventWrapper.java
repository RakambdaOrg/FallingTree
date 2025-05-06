package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockBreakEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ToString
public class BlockBreakEventWrapper implements IBlockBreakEvent{
	@NotNull
	@Getter
	private final BlockEvent.BreakEvent raw;
}
