package fr.rakambda.fallingtree.common.tree.builder.position;

import java.util.Collection;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AboveYFetcher implements IPositionFetcher{
	private static AboveYFetcher INSTANCE;
	
	@NonNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	@NonNull
	public Collection<ToAnalyzePos> getPositions(@NonNull ILevel level, @NonNull IBlockPos originPos, @NonNull ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return parentPos.betweenClosedStream(parentPos.above().north().east(), parentPos.below().south().west())
				.filter(pos -> pos.getY() > originPos.getY())
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
	
	public static AboveYFetcher getInstance(@NonNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new AboveYFetcher(common);
		}
		return INSTANCE;
	}
}
