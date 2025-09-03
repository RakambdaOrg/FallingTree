package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;

public interface IServerLevel extends ILevel{
    void spawnParticle(@NonNull IBlockPos blockPos, @NonNull IBlockState blockState, int count, float xDist, float yDist, float zDist, float maxSpeed);
}
