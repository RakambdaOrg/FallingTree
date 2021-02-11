package fr.raksrinana.fallingtree.tree.breaking;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.TreePart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isLeafBlock;
import static net.minecraft.util.Util.NIL_UUID;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(PlayerEntity player, Tree tree){
		return destroyInstant(tree, player, player.getMainHandStack());
	}
	
	private boolean destroyInstant(Tree tree, PlayerEntity player, ItemStack tool){
		World world = tree.getWorld();
		int damageMultiplicand = FallingTree.config.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(FallingTree.config.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendSystemMessage(new TranslatableText("chat.falling_tree.prevented_break_tool"), NIL_UUID);
				return false;
			}
			if(tree.getLogCount() >= rawWeightedUsesLeft){
				rawWeightedUsesLeft = Math.ceil(rawWeightedUsesLeft) - 1;
			}
		}
		
		tree.getLogs().stream()
				.limit((int) rawWeightedUsesLeft)
				.map(TreePart::getBlockPos)
				.forEachOrdered(logBlockPos -> {
					BlockState logState = world.getBlockState(logBlockPos);
					logState.getBlock().afterBreak(world, player, logBlockPos, logState, world.getBlockEntity(logBlockPos), tool);
					world.removeBlock(logBlockPos, false);
				});
		
		int toolDamage = (damageMultiplicand * (int) Math.min(tree.getLogCount(), rawWeightedUsesLeft)) - 1;
		if(toolDamage > 0){
			tool.damage(toolDamage, player, (entity) -> {});
		}
		
		boolean isTreeFullyBroken = damageMultiplicand == 0 || rawWeightedUsesLeft >= tree.getLogCount();
		if(isTreeFullyBroken){
			breakWarts(tree, player, tool, world);
			breakLeaves(tree, world);
		}
		return true;
	}
	
	private void breakWarts(Tree tree, PlayerEntity player, ItemStack tool, World world){
		tree.getWarts().stream()
				.map(TreePart::getBlockPos)
				.forEach(wartPos -> {
					BlockState wartState = world.getBlockState(wartPos);
					wartState.getBlock().afterBreak(world, player, wartPos, wartState, world.getBlockEntity(wartPos), tool);
					world.removeBlock(wartPos, false);
				});
	}
	
	private void breakLeaves(Tree tree, World world){
		int radius = FallingTree.config.getTreesConfiguration().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				BlockPos.Mutable checkPos = new BlockPos.Mutable();
				for(int dx = -radius; dx < radius; dx++){
					for(int dy = -radius; dy < radius; dy++){
						for(int dz = -radius; dz < radius; dz++){
							checkPos.set(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
							BlockState checkState = world.getBlockState(checkPos);
							Block checkBlock = checkState.getBlock();
							if(isLeafBlock(checkBlock)){
								Block.dropStacks(checkState, world, checkPos);
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
