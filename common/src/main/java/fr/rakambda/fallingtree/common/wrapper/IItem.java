package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;

public interface IItem extends IWrapper{
	boolean isAxe();
	
	boolean isAir();
	
	float getDestroySpeed(@NonNull IItemStack itemStack, @NonNull IBlockState blockState);
}
