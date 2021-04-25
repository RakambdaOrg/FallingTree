package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.tree.Tree;
import fr.raksrinana.fallingtree.fabric.tree.TreePart;
import fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Comparator;
import static net.minecraft.Util.NIL_UUID;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(Player player, Tree tree){
		return destroyInstant(tree, player, player.getMainHandItem());
	}
	
	private boolean destroyInstant(Tree tree, Player player, ItemStack tool){
		Level world = tree.getWorld();
		int breakableCount = tree.getBreakableCount();
		int damageMultiplicand = FallingTree.config.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageableItem() ? (tool.getMaxDamage() - tool.getDamageValue()) : Integer.MAX_VALUE;
		
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(FallingTree.config.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslatableComponent("chat.fallingtree.prevented_break_tool"), NIL_UUID);
				return false;
			}
			if(breakableCount >= rawWeightedUsesLeft){
				rawWeightedUsesLeft = Math.ceil(rawWeightedUsesLeft) - 1;
			}
		}
		
		int brokenCount = tree.getBreakableParts().stream()
				.sorted(Comparator.comparingInt(TreePart::getSequence).reversed())
				.limit((int) rawWeightedUsesLeft)
				.map(TreePart::getBlockPos)
				.mapToInt(logBlockPos -> {
					BlockState logState = world.getBlockState(logBlockPos);
					logState.getBlock().playerDestroy(world, player, logBlockPos, logState, world.getBlockEntity(logBlockPos), tool);
					world.removeBlock(logBlockPos, false);
					return 1;
				})
				.sum();
		
		int toolDamage = damageMultiplicand * brokenCount - 1;
		if(toolDamage > 0){
			tool.hurtAndBreak(toolDamage, player, (entity) -> {});
		}
		
		if(brokenCount >= breakableCount){
			forceBreakDecayLeaves(tree, world);
		}
		return true;
	}
	
	private void forceBreakDecayLeaves(Tree tree, Level world){
		int radius = FallingTree.config.getTreesConfiguration().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				MutableBlockPos checkPos = new MutableBlockPos();
				for(int dx = -radius; dx < radius; dx++){
					for(int dy = -radius; dy < radius; dy++){
						for(int dz = -radius; dz < radius; dz++){
							checkPos.set(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
							BlockState checkState = world.getBlockState(checkPos);
							Block checkBlock = checkState.getBlock();
							if(FallingTreeUtils.isLeafBlock(checkBlock)){
								Block.dropResources(checkState, world, checkPos);
								world.removeBlock(checkPos, false);
							}
						}
					}
				}
			});
		}
	}
	
	public static InstantaneousTreeBreakingHandler getInstance(){
		if(INSTANCE == null){
			INSTANCE = new InstantaneousTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
