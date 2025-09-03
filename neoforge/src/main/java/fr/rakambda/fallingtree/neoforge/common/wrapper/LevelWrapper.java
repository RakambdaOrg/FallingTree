package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBiome;
import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IChunk;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IRandomSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
@ToString
@Log4j2
public class LevelWrapper implements ILevel{
	@NonNull
	@Getter
	private final LevelAccessor raw;
	
	@Override
	public boolean isServer(){
		return !raw.isClientSide();
	}
	
	@Override
	@NonNull
	public IChunk getChunk(@NonNull IBlockPos blockPos){
		var pos = (BlockPos) blockPos.getRaw();
		return new ChunkAccessWrapper(raw.getChunk(pos));
	}
	
	@Override
	public boolean hasChunk(int x, int z){
		return raw.hasChunk(x, z);
	}
	
	@Override
	@NonNull
	public IBlockState getBlockState(@NonNull IBlockPos blockPos){
		var pos = (BlockPos) blockPos.getRaw();
		return new BlockStateWrapper(raw.getBlockState(pos));
	}
	
	@Override
	@Nullable
	public IBlockEntity getBlockEntity(@NonNull IBlockPos blockPos){
		var entity = raw.getBlockEntity((BlockPos) blockPos.getRaw());
		return entity == null ? null : new BlockEntityWrapper(entity);
	}
	
	@Override
	@NonNull
	public IRandomSource getRandom(){
		return new RandomSourceWrapper(raw.getRandom());
	}
	
	@Override
	public boolean removeBlock(@NonNull IBlockPos blockPos, boolean b){
		return raw.removeBlock((BlockPos) blockPos.getRaw(), b);
	}
	
	@Override
	public void setBlock(@NonNull IBlockPos blockPos, @NonNull IBlockState blockState){
		raw.setBlock((BlockPos) blockPos.getRaw(), (BlockState) blockState.getRaw(), 1);
	}
	
	@Override
	public void fallBlock(@NonNull IBlockPos logBlockPos, boolean drop, double dx, double dy, double dz, double vx, double vy, double vz){
		var entity = createFallingEntity(logBlockPos, dx, dy, dz);
		if(!drop){
			entity.disableDrop();
		}
		entity.setDeltaMovement(vx, vy, vz);
		raw.addFreshEntity(entity);
	}
	
	@NonNull
	private FallingBlockEntity createFallingEntity(@NonNull IBlockPos logBlockPos, double dx, double dy, double dz){
		var x = (double) logBlockPos.getX() + dx;
		var y = (double) logBlockPos.getY() + dy;
		var z = (double) logBlockPos.getZ() + dz;
		var blockState = (BlockState) getBlockState(logBlockPos).getRaw();
		var newBlockState = blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState;
		
		var entity = new FallingBlockEntity(EntityType.FALLING_BLOCK, (Level) raw);
		entity.blocksBuilding = true;
		entity.setPos(x, y, z);
		entity.xo = x;
		entity.yo = y;
		entity.zo = z;
		entity.blockState = newBlockState;
		return entity;
	}
	
	@Override
	@NonNull
	public IBiome getBiome(@NonNull IBlockPos blockPos){
		return new BiomeWrapper(raw.getBiome((BlockPos) blockPos.getRaw()));
	}
}
