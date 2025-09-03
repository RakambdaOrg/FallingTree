package fr.rakambda.fallingtree.fabric.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IChunk;
import fr.rakambda.fallingtree.common.wrapper.IChunkPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
@ToString
public class ChunkAccessWrapper implements IChunk{
	@NonNull
	@Getter
	private final ChunkAccess raw;
	
	@Override
	@NonNull
	public IChunkPos getPos(){
		return new ChunkPosWrapper(raw.getPos());
	}
}
