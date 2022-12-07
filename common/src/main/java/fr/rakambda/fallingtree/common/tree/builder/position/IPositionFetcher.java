package fr.rakambda.fallingtree.common.tree.builder.position;

import fr.rakambda.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface IPositionFetcher{
	@NotNull
	Collection<ToAnalyzePos> getPositions(@NotNull ILevel level, @NotNull IBlockPos originPos, @NotNull ToAnalyzePos parent);
}
