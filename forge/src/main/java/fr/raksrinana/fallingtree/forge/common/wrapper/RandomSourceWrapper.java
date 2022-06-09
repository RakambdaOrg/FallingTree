package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IRandomSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RandomSourceWrapper implements IRandomSource{
	@NotNull
	@Getter
	private final RandomSource raw;
}
