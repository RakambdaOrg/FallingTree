package fr.rakambda.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;

public interface IServerLevel extends ILevel{
    void spawnParticle(@NotNull IBlockPos blockPos, @NotNull IBlockState blockState, int count, float xDist, float yDist, float zDist, float maxSpeed);
}
