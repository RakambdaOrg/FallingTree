package fr.raksrinana.fallingtree.tree.breaking;

import fr.raksrinana.fallingtree.config.Config;
import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.TreePart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import javax.annotation.Nonnull;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isLeafBlock;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Util.DUMMY_UUID;

public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	@Override
	public void breakTree(BlockEvent.BreakEvent event, Tree tree){
		if(!destroy(tree, event.getPlayer(), event.getPlayer().getHeldItem(MAIN_HAND))){
			event.setCanceled(true);
		}
	}
	
	private int getMaxSize(){
		return Config.COMMON.getTreesConfiguration().getMaxSize();
	}
	
	private boolean destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		World world = tree.getWorld();
		int damageMultiplicand = Config.COMMON.getToolsConfiguration().getDamageMultiplicand();
		int toolUsesLeft = tool.isDamageable() ? (tool.getMaxDamage() - tool.getDamage()) : Integer.MAX_VALUE;
		
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(Config.COMMON.getToolsConfiguration().isPreserve()){
			if(rawWeightedUsesLeft <= 1){
				player.sendMessage(new TranslationTextComponent("chat.fallingtree.prevented_break_tool"), DUMMY_UUID);
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
					player.addStat(ITEM_USED.get(logState.getBlock().asItem()));
					logState.getBlock().harvestBlock(world, player, logBlockPos, logState, world.getTileEntity(logBlockPos), tool);
					world.removeBlock(logBlockPos, false);
				});
		
		int toolDamage = (damageMultiplicand * (int) Math.min(tree.getLogCount(), rawWeightedUsesLeft)) - 1;
		if(toolDamage > 0){
			tool.damageItem(toolDamage, player, (entity) -> {});
		}
		
		boolean isTreeFullyBroken = damageMultiplicand == 0 || rawWeightedUsesLeft >= tree.getLogCount();
		if(isTreeFullyBroken){
			breakWarts(tree, player, tool, world);
			breakLeaves(tree, world);
		}
		return true;
	}
	
	private void breakWarts(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool, World world){
		tree.getWarts().stream()
				.map(TreePart::getBlockPos)
				.forEach(wartPos -> {
					BlockState wartState = world.getBlockState(wartPos);
					wartState.getBlock().harvestBlock(world, player, wartPos, wartState, world.getTileEntity(wartPos), tool);
					world.removeBlock(wartPos, false);
				});
	}
	
	private static void breakLeaves(@Nonnull Tree tree, World world){
		int radius = Config.COMMON.getTreesConfiguration().getLeavesBreakingForceRadius();
		if(radius > 0){
			tree.getTopMostLog().ifPresent(topLog -> {
				BlockPos.Mutable checkPos = new BlockPos.Mutable();
				for(int dx = -radius; dx < radius; dx++){
					for(int dy = -radius; dy < radius; dy++){
						for(int dz = -radius; dz < radius; dz++){
							checkPos.setPos(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
							BlockState checkState = world.getBlockState(checkPos);
							Block checkBlock = checkState.getBlock();
							if(isLeafBlock(checkBlock)){
								Block.spawnDrops(checkState, world, checkPos);
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
