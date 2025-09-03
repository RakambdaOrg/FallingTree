package fr.rakambda.fallingtree.common.tree.builder.position;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import java.util.Collection;
import java.util.function.Function;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AbovePositionFetcher implements IPositionFetcher{
	private static AbovePositionFetcher INSTANCE;
	
	@NonNull
	private final FallingTreeCommon<?> mod;
	@NonNull
	private final Function<IBlockPos, IBlockPos> lowerPosProvider;
	
	@Override
	@NonNull
	public Collection<ToAnalyzePos> getPositions(@NonNull ILevel level, @NonNull IBlockPos originPos, @NonNull ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return parentPos.betweenClosedStream(parentPos.above().north().east(), lowerPosProvider.apply(parentPos).south().west())
				.map(checkPos -> {
					var checkedState = level.getBlockState(checkPos);
					var checkedEntity = level.getBlockEntity(checkPos);
					var checkBlock = checkedState.getBlock();
					var treePart = mod.getTreePart(checkBlock);
					var logSequence = treePart.isLog() ? 0 : (parent.sequenceSinceLastLog() + 1);
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos.immutable(), checkBlock, checkedState, checkedEntity, treePart, parent.sequence() + 1, logSequence);
				})
				.collect(toList());
	}
	
	public static AbovePositionFetcher getInstance(@NonNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new AbovePositionFetcher(common, IBlockPos::above);
		}
		return INSTANCE;
	}
}
