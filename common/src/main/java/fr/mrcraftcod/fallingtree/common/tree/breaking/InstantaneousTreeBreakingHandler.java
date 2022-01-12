package fr.mrcraftcod.fallingtree.common.tree.breaking;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.mrcraftcod.fallingtree.common.tree.Tree;
import fr.mrcraftcod.fallingtree.common.tree.TreePart;
import fr.mrcraftcod.fallingtree.common.wrapper.ILevel;
import fr.mrcraftcod.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	private final FallingTreeCommon<?> mod;
	
	@Override
	public boolean breakTree(@NotNull IPlayer player, @NotNull Tree tree) throws BreakTreeTooBigException{
		var tool = player.getMainHandItem();
		var level = tree.getLevel();
		var toolHandler = new ToolDamageHandler(tool,
				mod.getConfiguration().getTools().getDamageMultiplicand(),
				mod.getConfiguration().getTools().isPreserve(),
				tree.getBreakableCount(),
				mod.getConfiguration().getTrees().getMaxSize(),
				mod.getConfiguration().getTrees().getMaxSizeAction(),
				mod.getConfiguration().getTools().getDamageRounding());
		
		if(toolHandler.getMaxBreakCount() <= 0){
			log.debug("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.prevented_break_tool"));
			return false;
		}
		
		var brokenCount = tree.getBreakableParts().stream()
				.sorted(mod.getConfiguration().getTrees().getBreakOrder().getComparator())
				.limit(toolHandler.getMaxBreakCount())
				.map(TreePart::blockPos)
				.mapToInt(logBlockPos -> {
					var logState = level.getBlockState(logBlockPos);
					
					if(!tree.getHitPos().equals(logBlockPos) && !mod.checkCanBreakBlock(level, logBlockPos, logState, player)){
						return 0;
					}
					
					player.awardItemUsed(tool.getItem());
					logState.getBlock().playerDestroy(level, player, logBlockPos, logState, level.getBlockEntity(logBlockPos), tool);
					var isRemoved = level.removeBlock(logBlockPos, false);
					return isRemoved ? 1 : 0;
				})
				.sum();
		
		var toolDamage = toolHandler.getActualDamage(brokenCount) - 1;
		if(toolDamage > 0){
			tool.damage(toolDamage, player);
		}
		
		if(brokenCount >= toolHandler.getMaxBreakCount()){
			forceBreakDecayLeaves(tree, level);
		}
		return true;
	}
	
	private void forceBreakDecayLeaves(@NotNull Tree tree, @NotNull ILevel level){
		var radius = mod.getConfiguration().getTrees().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				var start = topLog.offset(-radius, -radius, -radius);
				var end = topLog.offset(radius, radius, radius);
				topLog.betweenClosedStream(start, end).forEach(checkPos -> {
					var checkState = level.getBlockState(checkPos);
					var checkBlock = checkState.getBlock();
					if(mod.isLeafBlock(checkBlock)){
						checkState.dropResources(level, checkPos);
						level.removeBlock(checkPos, false);
					}
				});
			});
		}
	}
	
	@NotNull
	public static InstantaneousTreeBreakingHandler getInstance(@NotNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new InstantaneousTreeBreakingHandler(common);
		}
		return INSTANCE;
	}
}
