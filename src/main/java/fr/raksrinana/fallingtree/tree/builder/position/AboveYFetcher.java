package fr.raksrinana.fallingtree.tree.builder.position;

import fr.raksrinana.fallingtree.tree.builder.ToAnalyzePos;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;
import java.util.Objects;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getTreePart;
import static java.util.stream.Collectors.toList;

public class AboveYFetcher implements IPositionFetcher{
	private static AboveYFetcher INSTANCE;
	
	private AboveYFetcher(){
	}
	
	@Override
	public Collection<ToAnalyzePos> getPositions(World world, BlockPos originPos, ToAnalyzePos parent){
		BlockPos parentPos = parent.getCheckPos();
		Block parentBlock = world.getBlockState(parentPos).getBlock();
		return BlockPos.stream(parentPos.up().north().east(), parentPos.down().south().west())
				.filter(pos -> pos.getY() >= originPos.getY())
				.map(checkPos -> {
					Block checkBlock = world.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos.toImmutable(), checkBlock, getTreePart(checkBlock), parent.getSequence() + 1);
				})
				.collect(toList());
	}
	
	public static AboveYFetcher getInstance(){
		if(Objects.isNull(INSTANCE)){
			INSTANCE = new AboveYFetcher();
		}
		return INSTANCE;
	}
}
