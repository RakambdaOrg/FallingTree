package fr.mrcraftcod.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;
import java.util.stream.Stream;

public interface IBlockPos extends IWrapper{
	@NotNull
	IBlockPos immutable();
	
	@NotNull
	IBlockPos offset(int dx, int dy, int dz);
	
	@NotNull
	IBlockPos relative(@NotNull DirectionCompat direction);
	
	@NotNull
	default IBlockPos above(){
		return relative(DirectionCompat.UP);
	}
	
	@NotNull
	default IBlockPos below(){
		return relative(DirectionCompat.DOWN);
	}
	
	@NotNull
	default IBlockPos north(){
		return relative(DirectionCompat.NORTH);
	}
	
	@NotNull
	default IBlockPos east(){
		return relative(DirectionCompat.EAST);
	}
	
	@NotNull
	default IBlockPos south(){
		return relative(DirectionCompat.SOUTH);
	}
	
	@NotNull
	default IBlockPos west(){
		return relative(DirectionCompat.WEST);
	}
	
	int getX();
	
	int getY();
	
	int getZ();
	
	@NotNull
	Stream<IBlockPos> betweenClosedStream(@NotNull IBlockPos start, @NotNull IBlockPos end);
}
