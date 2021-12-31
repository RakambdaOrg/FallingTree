package fr.mrcraftcod.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;

public interface IItem extends IWrapper{
	boolean isAir();
	
	float getDestroySpeed(@NotNull IItemStack itemStack, @NotNull IBlockState blockState);
}
