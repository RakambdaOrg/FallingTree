package fr.mrcraftcod.fallingtree.common.utils;

import fr.mrcraftcod.fallingtree.common.wrapper.IBlockPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class CacheSpeed{
	@NotNull
	private final IBlockPos pos;
	private final float speed;
	private final long startMillis = System.currentTimeMillis();
	
	public boolean isValid(@NotNull IBlockPos blockPos){
		return System.currentTimeMillis() <= startMillis + 1000 && Objects.equals(pos, blockPos);
	}
}
