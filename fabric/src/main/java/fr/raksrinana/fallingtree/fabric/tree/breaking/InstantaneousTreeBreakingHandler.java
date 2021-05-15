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
import java.util.Comparator;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static java.util.Objects.isNull;
import static net.minecraft.Util.NIL_UUID;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(Player player, Tree tree){
		return destroyInstant(tree, player, player.getMainHandItem());
	}
	
	private boolean destroyInstant(Tree tree, Player player, ItemStack tool){
		var level = tree.getLevel();
		var breakableCount = tree.getBreakableCount();
		var damageMultiplicand = config.getToolsConfiguration().getDamageMultiplicand();
		var toolUsesLeft = tool.isDamageableItem() ? (tool.getMaxDamage() - tool.getDamageValue()) : Integer.MAX_VALUE;
		
		var rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(config.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslatableComponent("chat.fallingtree.prevented_break_tool"), NIL_UUID);
				return false;
			}
			if(breakableCount >= rawWeightedUsesLeft){
				rawWeightedUsesLeft = Math.ceil(rawWeightedUsesLeft) - 1;
			}
		}
		
		var brokenCount = tree.getBreakableParts().stream()
				.sorted(Comparator.comparingInt(TreePart::getSequence).reversed())
				.limit((int) rawWeightedUsesLeft)
				.map(TreePart::getBlockPos)
				.mapToInt(logBlockPos -> {
					var logState = level.getBlockState(logBlockPos);
					logState.getBlock().playerDestroy(level, player, logBlockPos, logState, level.getBlockEntity(logBlockPos), tool);
					level.removeBlock(logBlockPos, false);
					return 1;
				})
				.sum();
		
		var toolDamage = damageMultiplicand * brokenCount - 1;
		if(toolDamage > 0){
			tool.hurtAndBreak(toolDamage, player, (entity) -> {});
		}
		
		if(brokenCount >= breakableCount){
			forceBreakDecayLeaves(tree, level);
		}
		return true;
	}
	
	private void forceBreakDecayLeaves(Tree tree, Level level){
		var radius = config.getTreesConfiguration().getLeavesBreakingForceRadius();
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
