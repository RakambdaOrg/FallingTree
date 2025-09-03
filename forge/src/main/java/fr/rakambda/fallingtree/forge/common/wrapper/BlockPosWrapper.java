package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.DirectionCompat;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.forge.FallingTree;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.NonNull;
import java.util.stream.Stream;

@RequiredArgsConstructor
@ToString
public class BlockPosWrapper implements IBlockPos{
	@NonNull
	@Getter
	private final BlockPos raw;
	
	@Override
	@NonNull
	public IBlockPos immutable(){
		return new BlockPosWrapper(raw.immutable());
	}
	
	@Override
	@NonNull
	public IBlockPos offset(int dx, int dy, int dz){
		return new BlockPosWrapper(raw.offset(dx, dy, dz));
	}
	
	@Override
	@NonNull
	public IBlockPos relative(@NonNull DirectionCompat direction){
		return new BlockPosWrapper(raw.relative(FallingTree.getMod().asDirection(direction)));
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
	@NonNull
	public Stream<IBlockPos> betweenClosedStream(@NonNull IBlockPos start, @NonNull IBlockPos end){
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
