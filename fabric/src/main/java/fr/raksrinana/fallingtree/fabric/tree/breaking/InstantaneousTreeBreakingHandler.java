package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.tree.Tree;
import fr.raksrinana.fallingtree.fabric.tree.TreePart;
import fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static fr.raksrinana.fallingtree.fabric.FallingTree.logger;
import static java.util.Objects.isNull;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(Player player, Tree tree){
		return destroyInstant(tree, player, player.getMainHandItem());
	}
	
	private boolean destroyInstant(Tree tree, Player player, ItemStack tool){
		var level = tree.getLevel();
		var breakableCount = Math.min(tree.getBreakableCount(), config.getTrees().getMaxSize());
		var toolHandler = new ToolDamageHandler(tool, config.getTools().getDamageMultiplicand(), config.getTools().isPreserve(), breakableCount);
		
		if(toolHandler.getMaxBreakCount() <= 0){
			logger.debug("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			FallingTreeUtils.notifyPlayer(player, new TranslatableComponent("chat.fallingtree.prevented_break_tool"));
			return false;
		}
		
		var brokenCount = tree.getBreakableParts().stream()
				.sorted(config.getTrees().getBreakOrder().getComparator())
				.limit(toolHandler.getMaxBreakCount())
				.map(TreePart::blockPos)
				.mapToInt(logBlockPos -> {
					var logState = level.getBlockState(logBlockPos);
					logState.getBlock().playerDestroy(level, player, logBlockPos, logState, level.getBlockEntity(logBlockPos), tool);
					level.removeBlock(logBlockPos, false);
					return 1;
				})
				.sum();
		
		var toolDamage = toolHandler.getActualDamage(brokenCount) - 1;
		if(toolDamage > 0){
			tool.hurtAndBreak(toolDamage, player, (entity) -> {});
		}
		
		if(brokenCount >= breakableCount){
			forceBreakDecayLeaves(tree, level);
		}
		return true;
	}
	
	private void forceBreakDecayLeaves(Tree tree, Level level){
		var radius = config.getTrees().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				var checkPos = new MutableBlockPos();
				for(var dx = -radius; dx < radius; dx++){
					for(var dy = -radius; dy < radius; dy++){
						for(var dz = -radius; dz < radius; dz++){
							checkPos.set(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
							var checkState = level.getBlockState(checkPos);
							var checkBlock = checkState.getBlock();
							if(FallingTreeUtils.isLeafBlock(checkBlock)){
								Block.dropResources(checkState, level, checkPos);
								level.removeBlock(checkPos, false);
							}
						}
					}
				}
			});
		}
	}
	
	public static InstantaneousTreeBreakingHandler getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new InstantaneousTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
