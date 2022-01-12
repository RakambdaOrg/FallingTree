package fr.mrcraftcod.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Random;

public interface ILevel extends IWrapper{
	@NotNull
	IChunk getChunk(@NotNull IBlockPos blockPos);
	
	boolean hasChunk(int x, int z);
	
	@NotNull
	Random getRandom();
	
	boolean isServer();
	
	@NotNull
	IBlockState getBlockState(@NotNull IBlockPos blockPos);
	
	@Nullable
	IBlockEntity getBlockEntity(@NotNull IBlockPos blockPos);
	
	boolean removeBlock(@NotNull IBlockPos blockPos, boolean b);
}
