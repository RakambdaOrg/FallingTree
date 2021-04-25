package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.tree.Tree;
import fr.raksrinana.fallingtree.fabric.tree.TreePart;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import static net.minecraft.Util.NIL_UUID;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(Player player, Tree tree){
		return destroyShift(tree, player, player.getMainHandItem());
	}
	
	private boolean destroyShift(Tree tree, Player player, ItemStack tool){
		Level world = tree.getWorld();
		int damageMultiplicand = FallingTree.config.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageableItem() ? (tool.getMaxDamage() - tool.getDamageValue()) : Integer.MAX_VALUE;
		
		if(FallingTree.config.getToolsConfiguration().isPreserve()){
			if(toolUsesLeft <= damageMultiplicand){
				player.sendMessage(new TranslatableComponent("chat.fallingtree.prevented_break_tool"), NIL_UUID);
				return false;
			}
		}
		
		tree.getLastSequencePart()
				.map(TreePart::getBlockPos)
				.ifPresent(logBlock -> {
					BlockState logState = world.getBlockState(logBlock);
					logState.getBlock().playerDestroy(world, player, tree.getHitPos(), logState, world.getBlockEntity(logBlock), tool);
					world.removeBlock(logBlock, false);
				});
		
		if(damageMultiplicand > 0){
			tool.hurtAndBreak(damageMultiplicand, player, (entity) -> {});
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
