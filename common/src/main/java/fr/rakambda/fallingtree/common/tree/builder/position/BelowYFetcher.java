package fr.rakambda.fallingtree.common.tree.builder.position;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BelowYFetcher implements IPositionFetcher{
	private static BelowYFetcher INSTANCE;
	
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	@NotNull
	public Collection<ToAnalyzePos> getPositions(@NotNull ILevel level, @NotNull IBlockPos originPos, @NotNull ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return parentPos.betweenClosedStream(parentPos.below().south().west(), parentPos.above().north().east())
				.filter(pos -> pos.getY() < originPos.getY())
				.map(checkPos -> {
					var checkState = level.getBlockState(checkPos);
					var checkedEntity = level.getBlockEntity(checkPos);
					var checkBlock = checkState.getBlock();
					var treePart = mod.getTreePart(checkBlock);
					var logSequence = treePart.isLog() ? 0 : (parent.sequenceSinceLastLog() + 1);
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos.immutable(), checkBlock, checkState, checkedEntity, treePart, parent.sequence() + 1, logSequence);
				})
				.collect(toList());
	}
	
	public static BelowYFetcher getInstance(@NotNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new BelowYFetcher(common);
		}
		return INSTANCE;
	}
}
