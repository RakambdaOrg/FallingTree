package fr.raksrinana.fallingtree.forge.tree.breaking;

import fr.raksrinana.fallingtree.forge.FallingTreeBlockBreakEvent;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.tree.Tree;
import fr.raksrinana.fallingtree.forge.tree.TreePart;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import static fr.raksrinana.fallingtree.forge.FallingTree.logger;
import static fr.raksrinana.fallingtree.forge.utils.TreePartType.NETHER_WART;
import static java.util.Objects.isNull;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public void breakTree(BlockEvent.BreakEvent event, Tree tree){
		destroyShift(tree, event.getPlayer(), event.getPlayer().getItemInHand(MAIN_HAND));
		if(event.isCancelable()){
			event.setCanceled(true);
		}
	}
	
	private boolean destroyShift(@Nonnull Tree tree, @Nonnull Player player, @Nonnull ItemStack tool){
		tree.getLastSequencePart()
				.map(treePart -> {
					var level = tree.getLevel();
					if(treePart.treePartType() == NETHER_WART && Config.COMMON.getTrees().isInstantlyBreakWarts()){
						return breakElements(tree, level, player, tool, tree.getWarts());
					}
					else{
						return breakElements(tree, level, player, tool, List.of(treePart));
					}
				});
		
		return false;
	}
	
	private boolean breakElements(Tree tree, Level level, Player player, ItemStack tool, Collection<TreePart> parts){
		var count = parts.size();
		var damageMultiplicand = Config.COMMON.getTools().getDamageMultiplicand();
		var toolHandler = new ToolDamageHandler(tool, damageMultiplicand, Config.COMMON.getTools().isPreserve(), count);
		
		if(toolHandler.getMaxBreakCount() <= 0){
			logger.debug("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			FallingTreeUtils.notifyPlayer(player, new TranslatableComponent("chat.fallingtree.prevented_break_tool"));
			return false;
		}
		
		var breakCount = parts.stream()
				.mapToInt(wart -> breakPart(wart, level, player))
				.sum();
		
		var damage = toolHandler.getActualDamage(breakCount);
		if(Double.compare(damageMultiplicand, 0) > 0 && damage > 0){
			tool.hurtAndBreak(damage, player, (entity) -> {});
		}
		return true;
	}
	
	private int breakPart(TreePart treePart, Level level, Player player){
		var blockPos = treePart.blockPos();
		var logState = level.getBlockState(blockPos);
		
		if(MinecraftForge.EVENT_BUS.post(new FallingTreeBlockBreakEvent(level, blockPos, logState, player))){
			return 0;
		}
		
		player.awardStat(ITEM_USED.get(logState.getBlock().asItem()));
		return 1;
	}
	
	public static ShiftDownTreeBreakingHandler getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new ShiftDownTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
