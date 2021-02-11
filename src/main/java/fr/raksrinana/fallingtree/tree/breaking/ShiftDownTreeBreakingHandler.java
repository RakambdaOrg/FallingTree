package fr.raksrinana.fallingtree.tree.breaking;

import fr.raksrinana.fallingtree.config.Config;
import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.TreePart;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import javax.annotation.Nonnull;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Util.DUMMY_UUID;

public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	@Override
	public void breakTree(BlockEvent.BreakEvent event, Tree tree){
		destroy(tree, event.getPlayer(), event.getPlayer().getHeldItem(MAIN_HAND));
		event.setCanceled(true);
	}
	
	private void destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		World world = tree.getWorld();
		int damageMultiplicand = Config.COMMON.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslationTextComponent("chat.fallingtree.prevented_break_tool"), DUMMY_UUID);
				return;
			}
		}
		
		tree.getLastSequencePart()
				.map(TreePart::getBlockPos)
				.ifPresent(logBlock -> {
					final BlockState logState = world.getBlockState(logBlock);
					player.addStat(ITEM_USED.get(logState.getBlock().asItem()));
					logState.getBlock().harvestBlock(world, player, tree.getHitPos(), logState, world.getTileEntity(logBlock), tool);
					world.removeBlock(logBlock, false);
				});
		
		int toolDamage = damageMultiplicand;
		if(toolDamage > 0){
			tool.damageItem(toolDamage, player, (entity) -> {});
		}
	}
	
	public static ShiftDownTreeBreakingHandler getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ShiftDownTreeBreakingHandler();
		}
		return INSTANCE;
	}
}
