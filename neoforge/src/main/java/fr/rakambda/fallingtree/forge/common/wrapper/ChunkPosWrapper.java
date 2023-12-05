package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IChunkPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ToString
public class ChunkPosWrapper implements IChunkPos{
	@NotNull
	@Getter
	private final ChunkPos raw;
	
	@Override
	public int getX(){
		return raw.x;
	}
	
	@Override
	public int getZ(){
		return raw.z;
	}
}
