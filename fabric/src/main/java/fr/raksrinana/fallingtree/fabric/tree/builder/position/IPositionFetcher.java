package fr.raksrinana.fallingtree.fabric.tree.builder.position;

import fr.raksrinana.fallingtree.fabric.tree.builder.ToAnalyzePos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.Collection;

public interface IPositionFetcher{
	Collection<ToAnalyzePos> getPositions(Level level, BlockPos originPos, ToAnalyzePos parent);
}
