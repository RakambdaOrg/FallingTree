package fr.raksrinana.fallingtree.fabric.tree.builder.position;

import fr.raksrinana.fallingtree.fabric.tree.builder.ToAnalyzePos;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getTreePart;
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
	public Collection<ToAnalyzePos> getPositions(World world, BlockPos originPos, ToAnalyzePos parent){
		BlockPos parentPos = parent.getCheckPos();
		Block parentBlock = world.getBlockState(parentPos).getBlock();
		return BlockPos.stream(parentPos.up().north().east(), lowerPosProvider.apply(parentPos).south().west())
				.map(checkPos -> {
					Block checkBlock = world.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(positionFetcherSupplier.get(), parentPos, parentBlock, checkPos.toImmutable(), checkBlock, getTreePart(checkBlock), parent.getSequence() + 1);
				})
				.collect(toList());
	}
	
	public static AbovePositionFetcher getInstance(){
		if(Objects.isNull(INSTANCE)){
			INSTANCE = new AbovePositionFetcher(BlockPos::up, AbovePositionFetcher::getSecondStepInstance);
		}
		return INSTANCE;
	}
	
	private static AbovePositionFetcher getSecondStepInstance(){
		if(Objects.isNull(SECOND_STEP_INSTANCE)){
			SECOND_STEP_INSTANCE = new AbovePositionFetcher(Function.identity(), BasicPositionFetcher::getInstance);
		}
		return SECOND_STEP_INSTANCE;
	}
}
