package fr.rakambda.fallingtree.common.tree.builder.position;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.function.Function;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BelowPositionFetcher implements IPositionFetcher{
	private static BelowPositionFetcher INSTANCE;
	
	@NotNull
	private final FallingTreeCommon<?> mod;
	@NotNull
	private final Function<IBlockPos, IBlockPos> higherPosProvider;
	
	@Override
	@NotNull
	public Collection<ToAnalyzePos> getPositions(@NotNull ILevel level, @NotNull IBlockPos originPos, @NotNull ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return parentPos.betweenClosedStream(parentPos.below().south().west(), higherPosProvider.apply(parentPos).north().east())
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
	
	public static BelowPositionFetcher getInstance(@NotNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new BelowPositionFetcher(common, IBlockPos::below);
		}
		return INSTANCE;
	}
}
