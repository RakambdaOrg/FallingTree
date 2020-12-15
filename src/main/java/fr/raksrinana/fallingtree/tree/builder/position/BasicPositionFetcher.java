package fr.raksrinana.fallingtree.tree.builder.position;

import fr.raksrinana.fallingtree.tree.builder.ToAnalyzePos;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getTreePart;
import static java.util.stream.Collectors.toList;

public class BasicPositionFetcher implements IPositionFetcher{
	private static BasicPositionFetcher INSTANCE;
	
	private BasicPositionFetcher(){
	}
	
	@Override
	public Collection<ToAnalyzePos> getPositions(World world, ToAnalyzePos parent){
		BlockPos parentPos = parent.getCheckPos();
		Block parentBlock = world.getBlockState(parentPos).getBlock();
		return EnumSet.allOf(Direction.class).stream()
				.map(parentPos::offset)
				.map(checkPos -> {
					Block checkBlock = world.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos, checkBlock, getTreePart(checkBlock), parent.getSequence() + 1);
				})
				.collect(toList());
	}
	
	public static BasicPositionFetcher getInstance(){
		if(Objects.isNull(INSTANCE)){
			INSTANCE = new BasicPositionFetcher();
		}
		return INSTANCE;
	}
}
