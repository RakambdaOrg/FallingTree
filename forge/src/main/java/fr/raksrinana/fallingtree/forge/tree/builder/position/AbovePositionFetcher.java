package fr.raksrinana.fallingtree.forge.tree.builder.position;

import fr.raksrinana.fallingtree.forge.tree.builder.ToAnalyzePos;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AbovePositionFetcher implements IPositionFetcher{
	private static AbovePositionFetcher INSTANCE;
	private static AbovePositionFetcher SECOND_STEP_INSTANCE;
	
	private final Function<BlockPos, BlockPos> lowerPosProvider;
	private final Supplier<IPositionFetcher> positionFetcherSupplier;
	
	@Override
	public Collection<ToAnalyzePos> getPositions(World level, BlockPos originPos, ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return BlockPos.betweenClosedStream(parentPos.above().north().east(), lowerPosProvider.apply(parentPos).south().west())
				.map(checkPos -> {
					var checkBlock = level.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(positionFetcherSupplier.get(), parentPos, parentBlock, checkPos.immutable(), checkBlock, FallingTreeUtils.getTreePart(checkBlock), parent.sequence() + 1);
				})
				.collect(toList());
	}
	
	public static AbovePositionFetcher getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new AbovePositionFetcher(BlockPos::above, AbovePositionFetcher::getSecondStepInstance);
		}
		return INSTANCE;
	}
	
	private static AbovePositionFetcher getSecondStepInstance(){
		if(isNull(SECOND_STEP_INSTANCE)){
			SECOND_STEP_INSTANCE = new AbovePositionFetcher(Function.identity(), BasicPositionFetcher::getInstance);
		}
		return SECOND_STEP_INSTANCE;
	}
}
