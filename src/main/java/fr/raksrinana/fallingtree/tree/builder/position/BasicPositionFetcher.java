package fr.raksrinana.fallingtree.tree.builder.position;

import fr.raksrinana.fallingtree.tree.builder.ToAnalyzePos;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getTreePart;

public class BasicPositionFetcher implements IPositionFetcher{
	private static BasicPositionFetcher INSTANCE;
	
	private BasicPositionFetcher(){
	}
	
	@Override
	public Collection<ToAnalyzePos> getPositions(World world, ToAnalyzePos parent){
		BlockPos parentPos = parent.getCheckPos();
		Block parentBlock = world.getBlockState(parentPos).getBlock();
		return Arrays.stream(Direction.values())
				.map(parentPos::offset)
				.map(checkPos -> {
					Block checkBlock = world.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos, checkBlock, getTreePart(checkBlock), parent.getSequence() + 1);
				})
				.collect(Collectors.toList());
	}
	
	public static BasicPositionFetcher getInstance(){
		if(Objects.isNull(INSTANCE)){
			INSTANCE = new BasicPositionFetcher();
		}
		return INSTANCE;
	}
}
