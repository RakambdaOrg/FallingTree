package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Random;

@RequiredArgsConstructor
public class LevelWrapper implements ILevel{
	@NotNull
	@Getter
	private final Level raw;
	
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
	public boolean removeBlock(@NotNull IBlockPos blockPos, boolean b){
		return raw.removeBlock((BlockPos) blockPos.getRaw(), b);
	}
	
	@Override
	@NotNull
	public Random getRandom(){
		return raw.getRandom();
	}
}
