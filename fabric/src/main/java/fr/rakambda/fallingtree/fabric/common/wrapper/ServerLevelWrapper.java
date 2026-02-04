package fr.rakambda.fallingtree.fabric.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class ServerLevelWrapper extends LevelWrapper implements IServerLevel {
    public ServerLevelWrapper(@NonNull ServerLevel serverLevel) {
        super(serverLevel);
    }

    @Override
    @NonNull
    public ServerLevel getRaw() {
        return (ServerLevel) super.getRaw();
    }

    @Override
    public void spawnParticle(@NonNull IBlockPos blockPos, @NonNull IBlockState blockState, int count, float xDist, float yDist, float zDist, float maxSpeed) {
        getRaw().sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, (BlockState) blockState.getRaw()),
                blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f,
                count,
                xDist, yDist, zDist,
                maxSpeed);
    }
	
	@Override
	public void fallBlock(@NonNull IBlockPos logBlockPos, boolean drop, double vx, double vy, double vz){
		var entity = FallingBlockEntity.fall(getRaw(), (BlockPos) logBlockPos.getRaw(), (BlockState) getBlockState(logBlockPos).getRaw());
		if(!drop){
			entity.disableDrop();
		}
		entity.setDeltaMovement(vx, vy, vz);
	}
}
