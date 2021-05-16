package fr.raksrinana.fallingtree.forge.tree.breaking;

import fr.raksrinana.fallingtree.forge.FallingTreeBlockBreakEvent;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.tree.Tree;
import fr.raksrinana.fallingtree.forge.tree.TreePart;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import javax.annotation.Nonnull;
import java.util.Optional;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Util.NIL_UUID;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public void breakTree(BlockEvent.BreakEvent event, Tree tree){
		destroy(tree, event.getPlayer(), event.getPlayer().getItemInHand(MAIN_HAND));
		if(event.isCancelable()){
			event.setCanceled(true);
		}
	}
	
	private void destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		World world = tree.getWorld();
		int damageMultiplicand = Config.COMMON.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageableItem() ? (tool.getMaxDamage() - tool.getDamageValue()) : Integer.MAX_VALUE;
		
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			if(toolUsesLeft <= damageMultiplicand){
				player.sendMessage(new TranslationTextComponent("chat.fallingtree.prevented_break_tool"), NIL_UUID);
				return;
			}
		}
		
		Optional<TreePart> lastSequencePartOptional = tree.getLastSequencePart();
		if(lastSequencePartOptional.isPresent()){
			TreePart treePart = lastSequencePartOptional.get();
			BlockPos blockPos = treePart.getBlockPos();
			
			BlockState logState = world.getBlockState(blockPos);
			boolean cancelled = MinecraftForge.EVENT_BUS.post(new FallingTreeBlockBreakEvent(world, blockPos, logState, player));
			
			if(!cancelled){
				player.awardStat(ITEM_USED.get(logState.getBlock().asItem()));
				logState.getBlock().playerDestroy(world, player, tree.getHitPos(), logState, world.getBlockEntity(blockPos), tool);
				world.removeBlock(blockPos, false);
				
				if(damageMultiplicand > 0){
					tool.hurtAndBreak(damageMultiplicand, player, (entity) -> {});
				}
			}
		}
	}
	
	public static ShiftDownTreeBreakingHandler getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ShiftDownTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
