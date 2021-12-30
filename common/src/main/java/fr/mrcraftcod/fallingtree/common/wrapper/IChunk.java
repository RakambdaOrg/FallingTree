package fr.mrcraftcod.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;

public interface IChunk extends IWrapper{
	@NotNull
	IChunkPos getPos();
}
