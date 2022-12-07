package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IChunk;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IRandomSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@ToString
public class LevelWrapper implements ILevel{
	@NotNull
	@Getter
	private final LevelAccessor raw;
	
	@Override
	public boolean isServer(){
		return !raw.isClientSide();
	}
	
	@Override
	@NotNull
	public IChunk getChunk(@NotNull IBlockPos blockPos){
		var pos = (BlockPos) blockPos.getRaw();
		return new ChunkAccessWrapper(raw.getChunk(pos));
	}
	
	@Override
	public boolean hasChunk(int x, int z){
		return raw.hasChunk(x, z);
	}
	
	@Override
	@NotNull
	public IBlockState getBlockState(@NotNull IBlockPos blockPos){
		var pos = (BlockPos) blockPos.getRaw();
		return new BlockStateWrapper(raw.getBlockState(pos));
	}
	
	@Override
	@Nullable
	public IBlockEntity getBlockEntity(@NotNull IBlockPos blockPos){
		var entity = raw.getBlockEntity((BlockPos) blockPos.getRaw());
		return entity == null ? null : new BlockEntityWrapper(entity);
	}
	
	@Override
	@NotNull
	public IRandomSource getRandom(){
		return new RandomSourceWrapper(raw.getRandom());
	}
	
	@Override
	public boolean removeBlock(@NotNull IBlockPos blockPos, boolean b){
		return raw.removeBlock((BlockPos) blockPos.getRaw(), b);
	}
}
