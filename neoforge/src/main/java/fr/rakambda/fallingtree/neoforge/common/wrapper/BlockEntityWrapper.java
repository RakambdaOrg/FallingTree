package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
@ToString
public class BlockEntityWrapper implements IBlockEntity{
	@NonNull
	@Getter
	private final BlockEntity raw;
}
