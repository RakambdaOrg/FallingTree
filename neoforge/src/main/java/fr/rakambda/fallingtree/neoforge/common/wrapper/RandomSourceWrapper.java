package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IRandomSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RandomSourceWrapper implements IRandomSource{
	@NotNull
	@Getter
	private final RandomSource raw;

	@Override
	public double nextDouble() {
		return raw.nextDouble();
	}
}
