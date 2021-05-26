package fr.raksrinana.fallingtree.fabric.tree.builder.position;

import fr.raksrinana.fallingtree.fabric.tree.builder.ToAnalyzePos;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.Collection;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getTreePart;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AboveYFetcher implements IPositionFetcher{
	private static AboveYFetcher INSTANCE;
	
	@Override
	public Collection<ToAnalyzePos> getPositions(Level level, BlockPos originPos, ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return BlockPos.betweenClosedStream(parentPos.above().north().east(), parentPos.below().south().west())
				.filter(pos -> pos.getY() > originPos.getY())
				.map(checkPos -> {
					var checkBlock = level.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos.immutable(), checkBlock, getTreePart(checkBlock), parent.sequence() + 1);
				})
				.collect(toList());
	}
	
	public static AboveYFetcher getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new AboveYFetcher();
		}
		return INSTANCE;
	}
}
