package fr.rakambda.fallingtree.common.utils;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class CacheSpeed{
	@NonNull
	private final IBlockPos pos;
	private final float speed;
	private final long startMillis = System.currentTimeMillis();
	
	public boolean isValid(@NonNull IBlockPos blockPos){
		return System.currentTimeMillis() <= startMillis + 1000 && Objects.equals(pos, blockPos);
	}
}
