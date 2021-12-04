package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.tree.Tree;
import fr.raksrinana.fallingtree.fabric.tree.TreePart;
import fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.Collection;
import java.util.List;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static fr.raksrinana.fallingtree.fabric.FallingTree.logger;
import static fr.raksrinana.fallingtree.fabric.utils.TreePartType.NETHER_WART;
import static java.util.Objects.isNull;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public boolean breakTree(Player player, Tree tree) throws BreakTreeTooBigException{
		return destroyShift(tree, player, player.getMainHandItem());
	}
	
	private boolean destroyShift(Tree tree, Player player, ItemStack tool) throws BreakTreeTooBigException{
		var treePartOptional = tree.getLastSequencePart();
		if(treePartOptional.isPresent()){
			var treePart = treePartOptional.get();
			var level = tree.getLevel();
			if(treePart.treePartType() == NETHER_WART && config.getTrees().isInstantlyBreakWarts()){
				return breakElements(tree, level, player, tool, tree.getWarts());
			}
			else{
				return breakElements(tree, level, player, tool, List.of(treePart));
			}
		}
		
		return false;
	}
	
	private boolean breakElements(Tree tree, Level level, Player player, ItemStack tool, Collection<TreePart> parts) throws BreakTreeTooBigException{
		var count = parts.size();
		var toolHandler = new ToolDamageHandler(tool, config.getTools().getDamageMultiplicand(), config.getTools().isPreserve(), count);
		
		if(toolHandler.getMaxBreakCount() <= 0){
			logger.debug("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			FallingTreeUtils.notifyPlayer(player, new TranslatableComponent("chat.fallingtree.prevented_break_tool"));
			return false;
		}
		
		var breakCount = parts.stream()
				.limit(toolHandler.getMaxBreakCount())
				.mapToInt(wart -> breakPart(tree, wart, level, player, tool))
				.sum();
		
		var damage = toolHandler.getActualDamage(breakCount);
		if(damage > 0){
			tool.hurtAndBreak(damage, player, (entity) -> {});
		}
		return false;
	}
	
	private int breakPart(Tree tree, TreePart treePart, Level level, Player player, ItemStack tool){
		var blockPos = treePart.blockPos();
		var logState = level.getBlockState(blockPos);
		logState.getBlock().playerDestroy(level, player, tree.getHitPos(), logState, level.getBlockEntity(blockPos), tool);
		level.removeBlock(blockPos, false);
		return 1;
	}
	
	public static ShiftDownTreeBreakingHandler getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new ShiftDownTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
