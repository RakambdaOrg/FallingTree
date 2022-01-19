package fr.raksrinana.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.util.Random;

public interface IBlockState extends IWrapper{
	void tick(@NotNull IServerLevel level, @NotNull IBlockPos blockPos, @NotNull Random random);
	
	void randomTick(@NotNull IServerLevel level, @NotNull IBlockPos blockPos, @NotNull Random random);
	
	@NotNull
	IBlock getBlock();
	
	boolean isRandomlyTicking();
	
	@NotNull
	Optional<Boolean> hasLeafPersistentFlag();
	
	void dropResources(@NotNull ILevel level, @NotNull IBlockPos blockPos);
}
