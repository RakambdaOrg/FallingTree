package fr.rakambda.fallingtree.common.tree.breaking;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public class LeafForceBreaker {
    private final FallingTreeCommon<?> mod;
	
    public void forceBreakDecayLeaves(@NotNull IPlayer player, @NotNull Tree tree, @NotNull ILevel level) {
        var radius = mod.getConfiguration().getTrees().getLeavesBreakingForceRadius();
        if (radius > 0) {
            tree.getTopMostLog().ifPresent(topLog -> {
                var start = topLog.offset(-radius, -radius, -radius);
                var end = topLog.offset(radius, radius, radius);
                topLog.betweenClosedStream(start, end).forEach(checkPos -> {
                    var checkState = level.getBlockState(checkPos);
                    var checkBlock = checkState.getBlock();
                    if (mod.isLeafBlock(checkBlock)) {
                        if (!player.isCreative() || mod.getConfiguration().isLootInCreative()) {
                            checkState.dropResources(level, checkPos);
                        }
                        level.removeBlock(checkPos, false);
                    }
                });
            });
        }
    }
}
