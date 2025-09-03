package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IRandomSource;
import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

@RequiredArgsConstructor
@ToString
public class BlockStateWrapper implements IBlockState{
	@NonNull
	@Getter
	private final BlockState raw;
	
	@Override
	public void tick(@NonNull IServerLevel level, @NonNull IBlockPos blockPos, @NonNull IRandomSource random){
		var l = (ServerLevel) level.getRaw();
		var bp = (BlockPos) blockPos.getRaw();
		raw.tick(l, bp, (RandomSource) random.getRaw()); //tick
	}
	
	@Override
	public void randomTick(@NonNull IServerLevel level, @NonNull IBlockPos blockPos, @NonNull IRandomSource random){
		var l = (ServerLevel) level.getRaw();
		var bp = (BlockPos) blockPos.getRaw();
		raw.randomTick(l, bp, (RandomSource) random.getRaw()); //randomTick
	}
	
	@Override
	@NonNull
	public IBlock getBlock(){
		return new BlockWrapper(raw.getBlock());
	}
	
	@Override
	public boolean isRandomlyTicking(){
		return raw.isRandomlyTicking();
	}
	
	@Override
	@NonNull
	public Optional<Boolean> hasLeafPersistentFlag(){
		return raw.getOptionalValue(LeavesBlock.PERSISTENT);
	}
	
	@Override
	public void dropResources(@NonNull ILevel level, @NonNull IBlockPos blockPos){
		Block.dropResources(raw, (Level) level.getRaw(), (BlockPos) blockPos.getRaw());
	}
}
