package fr.rakambda.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;

public interface IItem extends IWrapper{
	boolean isAxe();
	
	boolean isAir();
	
	float getDestroySpeed(@NotNull IItemStack itemStack, @NotNull IBlockState blockState);
}
