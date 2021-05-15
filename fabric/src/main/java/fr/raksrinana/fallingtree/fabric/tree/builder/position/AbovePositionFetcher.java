package fr.raksrinana.fallingtree.fabric.tree.builder.position;

import fr.raksrinana.fallingtree.fabric.tree.builder.ToAnalyzePos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getTreePart;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class AbovePositionFetcher implements IPositionFetcher{
	private static AbovePositionFetcher INSTANCE;
	private final Function<BlockPos, BlockPos> lowerPosProvider;
	private final Supplier<IPositionFetcher> positionFetcherSupplier;
	private static AbovePositionFetcher SECOND_STEP_INSTANCE;
	
	private AbovePositionFetcher(Function<BlockPos, BlockPos> lowerPosProvider, Supplier<IPositionFetcher> positionFetcherSupplier){
		this.lowerPosProvider = lowerPosProvider;
		this.positionFetcherSupplier = positionFetcherSupplier;
	}
	
	@Override
	public Collection<ToAnalyzePos> getPositions(Level level, BlockPos originPos, ToAnalyzePos parent){
		var parentPos = parent.getCheckPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return BlockPos.betweenClosedStream(parentPos.above().north().east(), lowerPosProvider.apply(parentPos).south().west())
				.map(checkPos -> {
					var checkBlock = level.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(positionFetcherSupplier.get(), parentPos, parentBlock, checkPos.immutable(), checkBlock, getTreePart(checkBlock), parent.getSequence() + 1);
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
