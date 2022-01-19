package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IChunk;
import fr.raksrinana.fallingtree.common.wrapper.IChunkPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
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
