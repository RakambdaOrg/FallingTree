package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.IChunk;
import fr.mrcraftcod.fallingtree.common.wrapper.IChunkPos;
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
