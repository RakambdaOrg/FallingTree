package fr.raksrinana.fallingtree.fabric.tree.builder.position;

import fr.raksrinana.fallingtree.fabric.tree.builder.ToAnalyzePos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;

public interface IPositionFetcher{
	Collection<ToAnalyzePos> getPositions(World world, BlockPos originPos, ToAnalyzePos parent);
}
