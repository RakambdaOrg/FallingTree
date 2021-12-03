package fr.raksrinana.fallingtree.forge.tree.breaking;

import fr.raksrinana.fallingtree.forge.FallingTreeBlockBreakEvent;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.tree.Tree;
import fr.raksrinana.fallingtree.forge.tree.TreePart;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import javax.annotation.Nonnull;
import static fr.raksrinana.fallingtree.forge.FallingTree.logger;
import static java.util.Objects.isNull;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public void breakTree(BlockEvent.BreakEvent event, Tree tree) throws BreakTreeTooBigException{
		if(!destroyInstant(tree, event.getPlayer(), event.getPlayer().getItemInHand(MAIN_HAND))){
			if(event.isCancelable()){
				event.setCanceled(true);
			}
		}
	}
	
	private boolean destroyInstant(@Nonnull Tree tree, @Nonnull Player player, @Nonnull ItemStack tool) throws BreakTreeTooBigException{
		var level = tree.getLevel();
		var toolHandler = new ToolDamageHandler(tool, Config.COMMON.getTools().getDamageMultiplicand(), Config.COMMON.getTools().isPreserve(), tree.getBreakableCount());
		
		if(toolHandler.getMaxBreakCount() <= 0){
			logger.debug("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			FallingTreeUtils.notifyPlayer(player, new TranslatableComponent("chat.fallingtree.prevented_break_tool"));
			return false;
		}
		
		var brokenCount = tree.getBreakableParts().stream()
				.sorted(Config.COMMON.getTrees().getBreakOrder().getComparator())
				.limit(toolHandler.getMaxBreakCount())
				.map(TreePart::blockPos)
				.mapToInt(logBlockPos -> {
					var logState = level.getBlockState(logBlockPos);
					if(!tree.getHitPos().equals(logBlockPos)){
						var cancelled = MinecraftForge.EVENT_BUS.post(new FallingTreeBlockBreakEvent(level, logBlockPos, logState, player));
						if(cancelled){
							return 0;
						}
					}
					
					player.awardStat(ITEM_USED.get(logState.getBlock().asItem()));
					logState.getBlock().playerDestroy(level, player, logBlockPos, logState, level.getBlockEntity(logBlockPos), tool);
					level.removeBlock(logBlockPos, false);
					return 1;
				})
				.sum();
		
		var toolDamage = toolHandler.getActualDamage(brokenCount) - 1;
		if(toolDamage > 0){
			tool.hurtAndBreak(toolDamage, player, (entity) -> {});
		}
		
		if(brokenCount >= toolHandler.getMaxBreakCount()){
			forceBreakDecayLeaves(tree, level);
		}
		return true;
	}
	
	private static void forceBreakDecayLeaves(@Nonnull Tree tree, Level level){
		var radius = Config.COMMON.getTrees().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				var checkPos = new BlockPos.MutableBlockPos();
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
