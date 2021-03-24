package fr.raksrinana.fallingtree.tree.breaking;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.TreePart;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import static net.minecraft.util.Util.NIL_UUID;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(PlayerEntity player, Tree tree){
		return destroyShift(tree, player, player.getMainHandStack());
	}
	
	private boolean destroyShift(Tree tree, PlayerEntity player, ItemStack tool){
		World world = tree.getWorld();
		int damageMultiplicand = FallingTree.config.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		
		if(FallingTree.config.getToolsConfiguration().isPreserve()){
			if(toolUsesLeft <= damageMultiplicand){
				player.sendSystemMessage(new TranslatableText("chat.fallingtree.prevented_break_tool"), NIL_UUID);
				return false;
			}
		}
		
		tree.getLastSequencePart()
				.map(TreePart::getBlockPos)
				.ifPresent(logBlock -> {
					BlockState logState = world.getBlockState(logBlock);
					logState.getBlock().afterBreak(world, player, tree.getHitPos(), logState, world.getBlockEntity(logBlock), tool);
					world.removeBlock(logBlock, false);
				});
		
		if(damageMultiplicand > 0){
			tool.damage(damageMultiplicand, player, (entity) -> {});
		}
		return false;
	}
	
	public static ShiftDownTreeBreakingHandler getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ShiftDownTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
