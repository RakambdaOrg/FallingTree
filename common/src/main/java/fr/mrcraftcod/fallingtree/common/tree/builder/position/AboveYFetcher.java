package fr.mrcraftcod.fallingtree.common.tree.builder.position;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.mrcraftcod.fallingtree.common.tree.builder.ToAnalyzePos;
import fr.mrcraftcod.fallingtree.common.wrapper.IBlockPos;
import fr.mrcraftcod.fallingtree.common.wrapper.ILevel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AboveYFetcher implements IPositionFetcher{
	private static AboveYFetcher INSTANCE;
	
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	@NotNull
	public Collection<ToAnalyzePos> getPositions(@NotNull ILevel level, @NotNull IBlockPos originPos, @NotNull ToAnalyzePos parent){
		var parentPos = parent.checkPos();
		var parentBlock = level.getBlockState(parentPos).getBlock();
		return parentPos.betweenClosedStream(parentPos.above().north().east(), parentPos.below().south().west())
				.filter(pos -> pos.getY() > originPos.getY())
				.map(checkPos -> {
					var checkBlock = level.getBlockState(checkPos).getBlock();
					return new ToAnalyzePos(this, parentPos, parentBlock, checkPos.immutable(), checkBlock, mod.getTreePart(checkBlock), parent.sequence() + 1);
				})
				.collect(toList());
	}
	
	public static AboveYFetcher getInstance(@NotNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new AboveYFetcher(common);
		}
		return INSTANCE;
	}
}
