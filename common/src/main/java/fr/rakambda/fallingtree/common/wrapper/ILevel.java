package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface ILevel extends IWrapper{
	@NonNull
	IChunk getChunk(@NonNull IBlockPos blockPos);
	
	boolean hasChunk(int x, int z);
	
	@NonNull
	IRandomSource getRandom();
	
	boolean isServer();
	
	@NonNull
	IBlockState getBlockState(@NonNull IBlockPos blockPos);
	
	@Nullable
	IBlockEntity getBlockEntity(@NonNull IBlockPos blockPos);
	
	boolean removeBlock(@NonNull IBlockPos blockPos, boolean b);
	
	void setBlock(@NonNull IBlockPos blockPos, @NonNull IBlockState blockState);
	
	@NonNull
	IBiome getBiome(@NonNull IBlockPos blockPos);
}
