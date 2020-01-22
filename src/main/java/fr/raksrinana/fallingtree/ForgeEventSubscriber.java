package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.Config;
import fr.raksrinana.fallingtree.tree.TreeHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventSubscriber{
	@SubscribeEvent
	public static void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(!event.isCanceled() && !event.getWorld().isRemote()){
			if(isPlayerInRightState(event.getPlayer())){
				TreeHandler.getTree(event.getWorld(), event.getPos()).ifPresent(tree -> {
					if(Config.COMMON.maxTreeSize.get() >= tree.getLogCount()){
						if(TreeHandler.destroy(tree, event.getPlayer(), event.getPlayer().getHeldItem(Hand.MAIN_HAND))){
							event.setCanceled(true);
						}
					}
					else{
						event.getPlayer().sendMessage(new TranslationTextComponent("chat.falling_tree.tree_too_big", tree.getLogCount(), Config.COMMON.maxTreeSize.get()));
					}
				});
			}
		}
	}
	
	private static boolean isPlayerInRightState(PlayerEntity player){
		if(player.abilities.isCreativeMode){
			return false;
		}
		if(Config.COMMON.reverseSneaking.get() != player.isCrouching()){
			return false;
		}
		return TreeHandler.canPlayerBreakTree(player);
	}
	
	@SubscribeEvent
	public static void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event){
		if(Config.COMMON.breakLeaves.get() && !event.getWorld().isRemote()){
			IWorld world = event.getWorld();
			BlockState eventState = event.getState();
			Block eventBlock = eventState.getBlock();
			BlockPos eventPos = event.getPos();
			if(eventBlock.isAir(eventState, world, eventPos)){
				for(Direction facing : event.getNotifiedSides()){
					BlockPos neighborPos = eventPos.offset(facing);
					if(world.isBlockLoaded(neighborPos)){
						BlockState neighborState = event.getWorld().getBlockState(neighborPos);
						if(BlockTags.LEAVES.contains(neighborState.getBlock())){
							neighborState.getBlock().randomTick(neighborState, (ServerWorld) world, neighborPos, world.getRandom());
						}
					}
				}
			}
		}
	}
}
