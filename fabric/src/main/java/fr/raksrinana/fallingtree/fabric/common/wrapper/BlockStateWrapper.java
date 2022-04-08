package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IBlock;
import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.IBlockState;
import fr.raksrinana.fallingtree.common.wrapper.ILevel;
import fr.raksrinana.fallingtree.common.wrapper.IRandomSource;
import fr.raksrinana.fallingtree.common.wrapper.IServerLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

@RequiredArgsConstructor
public class BlockStateWrapper implements IBlockState{
	@NotNull
	@Getter
	private final BlockState raw;
	
	@Override
	public void tick(@NotNull IServerLevel level, @NotNull IBlockPos blockPos, @NotNull IRandomSource random){
		var l = (ServerLevel) level.getRaw();
		var bp = (BlockPos) blockPos.getRaw();
		raw.tick(l, bp, (RandomSource) random.getRaw());
	}
	
	@Override
	public void randomTick(@NotNull IServerLevel level, @NotNull IBlockPos blockPos, @NotNull IRandomSource random){
		var l = (ServerLevel) level.getRaw();
		var bp = (BlockPos) blockPos.getRaw();
		raw.randomTick(l, bp, (RandomSource) random.getRaw());
	}
	
	@Override
	@NotNull
	public IBlock getBlock(){
		return new BlockWrapper(raw.getBlock());
	}
	
	@Override
	public boolean isRandomlyTicking(){
		return raw.isRandomlyTicking();
	}
	
	@Override
	@NotNull
	public Optional<Boolean> hasLeafPersistentFlag(){
		return raw.getOptionalValue(LeavesBlock.PERSISTENT);
	}
	
	@Override
	public void dropResources(@NotNull ILevel level, @NotNull IBlockPos blockPos){
		Block.dropResources(raw, (Level) level.getRaw(), (BlockPos) blockPos.getRaw());
	}
}
