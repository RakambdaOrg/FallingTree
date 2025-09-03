package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;
import java.util.stream.Stream;

public interface IBlockPos extends IWrapper{
	@NonNull
	IBlockPos immutable();
	
	@NonNull
	IBlockPos offset(int dx, int dy, int dz);
	
	@NonNull
	IBlockPos relative(@NonNull DirectionCompat direction);
	
	@NonNull
	default IBlockPos above(){
		return relative(DirectionCompat.UP);
	}
	
	@NonNull
	default IBlockPos below(){
		return relative(DirectionCompat.DOWN);
	}
	
	@NonNull
	default IBlockPos north(){
		return relative(DirectionCompat.NORTH);
	}
	
	@NonNull
	default IBlockPos east(){
		return relative(DirectionCompat.EAST);
	}
	
	@NonNull
	default IBlockPos south(){
		return relative(DirectionCompat.SOUTH);
	}
	
	@NonNull
	default IBlockPos west(){
		return relative(DirectionCompat.WEST);
	}
	
	int getX();
	
	int getY();
	
	int getZ();
	
	@NonNull
	Stream<IBlockPos> betweenClosedStream(@NonNull IBlockPos start, @NonNull IBlockPos end);
}
