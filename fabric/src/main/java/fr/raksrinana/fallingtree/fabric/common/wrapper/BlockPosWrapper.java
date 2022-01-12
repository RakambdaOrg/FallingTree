package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.DirectionCompat;
import fr.mrcraftcod.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.fabric.FallingTree;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class BlockPosWrapper implements IBlockPos{
	@NotNull
	@Getter
	private final BlockPos raw;
	
	@Override
	@NotNull
	public IBlockPos immutable(){
		return new BlockPosWrapper(raw.immutable());
	}
	
	@Override
	@NotNull
	public IBlockPos relative(@NotNull DirectionCompat direction){
		return new BlockPosWrapper(raw.relative(FallingTree.getMod().asDirection(direction)));
	}
	
	@Override
	@NotNull
	public IBlockPos offset(int dx, int dy, int dz){
		return new BlockPosWrapper(raw.offset(dx, dy, dz));
	}
	
	@Override
	public int getX(){
		return raw.getX();
	}
	
	@Override
	public int getY(){
		return raw.getY();
	}
	
	@Override
	public int getZ(){
		return raw.getZ();
	}
	
	@Override
	@NotNull
	public Stream<IBlockPos> betweenClosedStream(@NotNull IBlockPos start, @NotNull IBlockPos end){
		return BlockPos.betweenClosedStream((BlockPos) start.getRaw(), (BlockPos) end.getRaw()).map(BlockPosWrapper::new);
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof IBlockPos pos)){
			return false;
		}
		return raw.equals(pos.getRaw());
	}
	
	@Override
	public int hashCode(){
		return raw.hashCode();
	}
}
