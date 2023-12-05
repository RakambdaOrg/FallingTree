package fr.rakambda.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBlock extends IWrapper{
	boolean isAir();
	
	void playerDestroy(@NotNull ILevel level, @NotNull IPlayer player, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @Nullable IBlockEntity blockEntity, @NotNull IItemStack itemStack);

	@NotNull
	IComponent getAsComponent();
}
