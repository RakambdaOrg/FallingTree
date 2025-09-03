package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface IBlock extends IWrapper{
	boolean isAir();
	
	void playerDestroy(@NonNull ILevel level, @NonNull IPlayer player, @NonNull IBlockPos blockPos, @NonNull IBlockState blockState, @Nullable IBlockEntity blockEntity, @NonNull IItemStack itemStack, boolean dropResources);

	@NonNull
	IComponent getAsComponent();
}
