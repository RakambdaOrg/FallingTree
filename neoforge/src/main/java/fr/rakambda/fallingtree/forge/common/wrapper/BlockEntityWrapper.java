package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ToString
public class BlockEntityWrapper implements IBlockEntity{
	@NotNull
	@Getter
	private final BlockEntity raw;
}
