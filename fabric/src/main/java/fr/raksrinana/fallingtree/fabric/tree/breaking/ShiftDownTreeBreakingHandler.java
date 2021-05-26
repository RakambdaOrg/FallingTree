package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.tree.Tree;
import fr.raksrinana.fallingtree.fabric.tree.TreePart;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static java.util.Objects.isNull;
import static net.minecraft.Util.NIL_UUID;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(Player player, Tree tree){
		return destroyShift(tree, player, player.getMainHandItem());
	}
	
	private boolean destroyShift(Tree tree, Player player, ItemStack tool){
		var level = tree.getLevel();
		var damageMultiplicand = config.getTools().getDamageMultiplicand();
		var toolUsesLeft = tool.isDamageableItem() ? (tool.getMaxDamage() - tool.getDamageValue()) : Integer.MAX_VALUE;
		
		if(config.getTools().isPreserve()){
			if(toolUsesLeft <= damageMultiplicand){
				player.sendMessage(new TranslatableComponent("chat.fallingtree.prevented_break_tool"), NIL_UUID);
				return false;
			}
		}
		
		tree.getLastSequencePart()
				.map(TreePart::blockPos)
				.ifPresent(logBlock -> {
					var logState = level.getBlockState(logBlock);
					logState.getBlock().playerDestroy(level, player, tree.getHitPos(), logState, level.getBlockEntity(logBlock), tool);
					level.removeBlock(logBlock, false);
				});
		
		if(damageMultiplicand > 0){
			tool.hurtAndBreak(damageMultiplicand, player, (entity) -> {});
		}
		return false;
	}
	
	public static ShiftDownTreeBreakingHandler getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new ShiftDownTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
