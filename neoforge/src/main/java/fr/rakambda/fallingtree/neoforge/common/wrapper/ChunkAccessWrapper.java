package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IChunk;
import fr.rakambda.fallingtree.common.wrapper.IChunkPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ToString
public class ChunkAccessWrapper implements IChunk{
	@NotNull
	@Getter
	private final ChunkAccess raw;
	
	@Override
	@NotNull
	public IChunkPos getPos(){
		return new ChunkPosWrapper(raw.getPos());
	}
}
