package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IBlockEntity;
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
