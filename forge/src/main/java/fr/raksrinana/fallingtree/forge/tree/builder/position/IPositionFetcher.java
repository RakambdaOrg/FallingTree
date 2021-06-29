package fr.raksrinana.fallingtree.forge.tree.builder.position;

import fr.raksrinana.fallingtree.forge.tree.builder.ToAnalyzePos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;

public interface IPositionFetcher{
	Collection<ToAnalyzePos> getPositions(World level, BlockPos originPos, ToAnalyzePos parent);
}
