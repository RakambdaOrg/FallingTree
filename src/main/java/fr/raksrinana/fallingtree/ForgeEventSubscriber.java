package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.CommonConfig;
import fr.raksrinana.fallingtree.config.TreeConfiguration;
import fr.raksrinana.fallingtree.tree.TreeHandler;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
public final class ForgeEventSubscriber{
	private static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	
	@SubscribeEvent
	public static void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(!event.isCanceled() && !event.getWorld().isRemote){
			if(isPlayerInRightState(event.getPlayer()) && Objects.nonNull(event.getWorld())){
				TreeHandler.getTree(event.getWorld(), event.getPos()).ifPresent(tree -> {
					if(TreeConfiguration.getMaxSize() >= tree.getLogCount()){
						if(TreeHandler.destroy(tree, event.getPlayer(), event.getPlayer().getHeldItem(EnumHand.MAIN_HAND))){
							event.setCanceled(true);
						}
					}
					else{
						event.getPlayer().sendMessage(new TextComponentTranslation("chat.falling_tree.tree_too_big", tree.getLogCount(), TreeConfiguration.getMaxSize()));
					}
				});
			}
		}
	}
	
	private static boolean isPlayerInRightState(EntityPlayer player){
		if(player.isCreative()){
			return false;
		}
		if(CommonConfig.reverseSneaking != player.isSneaking()){
			return false;
		}
		return TreeHandler.canPlayerBreakTree(player);
	}
	
	@SubscribeEvent
	public static void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event){
		if(TreeConfiguration.isLavesBreaking() && !event.getWorld().isRemote){
			WorldServer world = (WorldServer) event.getWorld();
			IBlockState eventState = event.getState();
			Block eventBlock = eventState.getBlock();
			BlockPos eventPos = event.getPos();
			if(eventBlock.isAir(eventState, world, eventPos)){
				for(EnumFacing facing : event.getNotifiedSides()){
					BlockPos neighborPos = eventPos.offset(facing);
					if(world.isBlockLoaded(neighborPos)){
						IBlockState neighborState = event.getWorld().getBlockState(neighborPos);
						if(neighborState.getBlock() instanceof BlockLeaves){
							scheduledLeavesBreaking.add(new LeafBreakingSchedule(world, neighborPos, 4));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event){
		if(event.side == Side.SERVER && event.phase == TickEvent.Phase.END){
			Iterator<LeafBreakingSchedule> leavesBreak = scheduledLeavesBreaking.iterator();
			while(leavesBreak.hasNext()){
				LeafBreakingSchedule leafBreakingSchedule = leavesBreak.next();
				WorldServer world = leafBreakingSchedule.getWorld();
				if(leafBreakingSchedule.getRemainingTicks() <= 0){
					if(world.isBlockLoaded(leafBreakingSchedule.getBlockPos())){
						IBlockState state = world.getBlockState(leafBreakingSchedule.getBlockPos());
						Block block = state.getBlock();
						if(block instanceof BlockLeaves){
							block.randomTick(world, leafBreakingSchedule.getBlockPos(), state, world.rand);
						}
						else{
							leavesBreak.remove();
						}
					}
					else{
						leavesBreak.remove();
					}
				}
				else{
					leafBreakingSchedule.tick();
				}
			}
		}
	}
}
