package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IRandomSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class RandomSourceWrapper implements IRandomSource{
	@NonNull
	@Getter
	private final RandomSource raw;

	@Override
	public double nextDouble() {
		return raw.nextDouble();
	}
}
