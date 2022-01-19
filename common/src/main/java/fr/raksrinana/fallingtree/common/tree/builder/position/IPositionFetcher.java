package fr.raksrinana.fallingtree.common.tree.builder.position;

import fr.raksrinana.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.ILevel;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface IPositionFetcher{
	@NotNull
	Collection<ToAnalyzePos> getPositions(@NotNull ILevel level, @NotNull IBlockPos originPos, @NotNull ToAnalyzePos parent);
}
