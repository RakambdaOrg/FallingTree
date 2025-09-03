package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;
import java.util.Optional;

public interface IBlockState extends IWrapper{
	void tick(@NonNull IServerLevel level, @NonNull IBlockPos blockPos, @NonNull IRandomSource random);
	
	void randomTick(@NonNull IServerLevel level, @NonNull IBlockPos blockPos, @NonNull IRandomSource random);
	
	@NonNull
	IBlock getBlock();
	
	boolean isRandomlyTicking();
	
	@NonNull
	Optional<Boolean> hasLeafPersistentFlag();
	
	void dropResources(@NonNull ILevel level, @NonNull IBlockPos blockPos);
}
