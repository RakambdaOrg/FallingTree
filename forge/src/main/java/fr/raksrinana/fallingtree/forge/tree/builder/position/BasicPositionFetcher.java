package fr.raksrinana.fallingtree.forge.tree.builder.position;

import fr.raksrinana.fallingtree.forge.tree.builder.ToAnalyzePos;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicPositionFetcher implements IPositionFetcher{
	private static BasicPositionFetcher INSTANCE;
	
	@Override
	public Collection<ToAnalyzePos> getPositions(World level, BlockPos originPos, ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return BlockPos.betweenClosedStream(parentPos.above().north().east(), parentPos.below().south().west())
				.map(checkPos -> {
					Block checkBlock = level.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos.immutable(), checkBlock, FallingTreeUtils.getTreePart(checkBlock), parent.sequence() + 1);
				})
				.collect(toList());
	}
	
	public static BasicPositionFetcher getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new BasicPositionFetcher();
		}
		return INSTANCE;
	}
}
