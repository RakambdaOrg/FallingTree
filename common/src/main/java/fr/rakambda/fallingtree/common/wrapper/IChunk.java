package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;

public interface IChunk extends IWrapper{
	@NonNull
	IChunkPos getPos();
}
