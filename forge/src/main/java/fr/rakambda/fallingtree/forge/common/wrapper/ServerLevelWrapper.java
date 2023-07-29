package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ServerLevelWrapper extends LevelWrapper implements IServerLevel{
	public ServerLevelWrapper(@NotNull ServerLevel serverLevel){
		super(serverLevel);
	}

	@Override
	@NotNull
	public ServerLevel getRaw() {
		return (ServerLevel) super.getRaw();
	}

	@Override
	public void spawnParticle(@NotNull IBlockPos blockPos, @NotNull IBlockState blockState, int count, float xDist, float yDist, float zDist, float maxSpeed) {
		getRaw().sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, (BlockState) blockState.getRaw()),
				blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f,
				count,
				xDist, yDist, zDist,
				maxSpeed);
	}
}
