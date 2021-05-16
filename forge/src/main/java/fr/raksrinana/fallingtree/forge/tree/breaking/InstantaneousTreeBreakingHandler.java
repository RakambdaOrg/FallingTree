package fr.raksrinana.fallingtree.forge.tree.breaking;

import fr.raksrinana.fallingtree.forge.FallingTreeBlockBreakEvent;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.tree.Tree;
import fr.raksrinana.fallingtree.forge.tree.TreePart;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import javax.annotation.Nonnull;
import java.util.Comparator;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Util.NIL_UUID;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public void breakTree(BlockEvent.BreakEvent event, Tree tree){
		if(!destroy(tree, event.getPlayer(), event.getPlayer().getItemInHand(MAIN_HAND))){
			if(event.isCancelable()){
				event.setCanceled(true);
			}
		}
	}
	
	private boolean destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		World world = tree.getWorld();
		int breakableCount = tree.getBreakableCount();
		int damageMultiplicand = Config.COMMON.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageableItem() ? (tool.getMaxDamage() - tool.getDamageValue()) : Integer.MAX_VALUE;
		
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslationTextComponent("chat.fallingtree.prevented_break_tool"), NIL_UUID);
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
					if(!tree.getHitPos().equals(logBlockPos)){
						boolean cancelled = MinecraftForge.EVENT_BUS.post(new FallingTreeBlockBreakEvent(world, logBlockPos, logState, player));
						if(cancelled){
							return 0;
						}
					}
					
					player.awardStat(ITEM_USED.get(logState.getBlock().asItem()));
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
	
	private static void forceBreakDecayLeaves(@Nonnull Tree tree, World world){
		int radius = Config.COMMON.getTreesConfiguration().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				BlockPos.Mutable checkPos = new BlockPos.Mutable();
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
