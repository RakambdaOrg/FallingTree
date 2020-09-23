package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.BreakMode;
import fr.raksrinana.fallingtree.config.CommonConfig;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import fr.raksrinana.fallingtree.config.TreeConfiguration;
import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.TreeHandler;
import fr.raksrinana.fallingtree.utils.CachedSpeed;
import fr.raksrinana.fallingtree.utils.LeafBreakingSchedule;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import javax.annotation.Nonnull;
import java.util.*;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.canPlayerBreakTree;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isLeafBlock;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
public final class ForgeEventSubscriber{
	private static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	private static final Map<UUID, CachedSpeed> speedCache = new HashMap<>();
	
	@SubscribeEvent
	public static void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed event){
		if(!event.isCanceled()){
			if(TreeConfiguration.getBreakMode() == BreakMode.INSTANTANEOUS){
				if(isPlayerInRightState(event.getEntityPlayer())){
					CachedSpeed cachedSpeed = speedCache.compute(event.getEntityPlayer().getUniqueID(), (pos, speed) -> {
						if(Objects.isNull(speed) || !speed.isValid(event.getPos())){
							speed = getSpeed(event);
						}
						return speed;
					});
					if(Objects.nonNull(cachedSpeed)){
						event.setNewSpeed(cachedSpeed.getSpeed());
					}
				}
			}
		}
	}
	
	private static CachedSpeed getSpeed(PlayerEvent.BreakSpeed event){
		double speedMultiplicand = ToolConfiguration.getSpeedMultiplicand();
		return speedMultiplicand <= 0 ? null :
				TreeHandler.getTree(event.getEntity().getEntityWorld(), event.getPos())
						.map(tree -> new CachedSpeed(event.getPos(), event.getOriginalSpeed() / ((float) speedMultiplicand * tree.getLogCount())))
						.orElse(null);
	}
	
	@SubscribeEvent
	public static void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(!event.isCanceled() && !event.getWorld().isRemote){
			if(isPlayerInRightState(event.getPlayer())){
				TreeHandler.getTree(event.getWorld(), event.getPos()).ifPresent(tree -> {
					BreakMode breakMode = TreeConfiguration.getBreakMode();
					if(breakMode == BreakMode.INSTANTANEOUS){
						breakInstant(event, tree);
					}
					else if(breakMode == BreakMode.SHIFT_DOWN){
						breakShiftDown(event, tree);
					}
				});
			}
		}
	}
	
	private static void breakInstant(BlockEvent.BreakEvent event, Tree tree){
		if(TreeConfiguration.getMaxSize() >= tree.getLogCount()){
			if(!TreeHandler.destroyInstant(tree, event.getPlayer(), event.getPlayer().getHeldItem(EnumHand.MAIN_HAND))){
				event.setCanceled(true);
			}
		}
		else{
			event.getPlayer().sendMessage(new TextComponentTranslation("chat.falling_tree.tree_too_big", tree.getLogCount(), TreeConfiguration.getMaxSize()));
		}
	}
	
	private static void breakShiftDown(BlockEvent.BreakEvent event, Tree tree){
		TreeHandler.destroyShift(tree, event.getPlayer(), event.getPlayer().getHeldItem(EnumHand.MAIN_HAND));
		event.setCanceled(true);
	}
	
	private static boolean isPlayerInRightState(EntityPlayer player){
		if(player.capabilities.isCreativeMode && !CommonConfig.isBreakInCreative()){
			return false;
		}
		if(CommonConfig.isReverseSneaking() != player.isSneaking()){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	@SubscribeEvent
	public static void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event){
		if(TreeConfiguration.isLeavesBreaking() && !event.getWorld().isRemote && event.getWorld() instanceof WorldServer){
			WorldServer world = (WorldServer) event.getWorld();
			IBlockState eventState = event.getState();
			Block eventBlock = eventState.getBlock();
			BlockPos eventPos = event.getPos();
			if(eventBlock.isAir(eventState, world, eventPos)){
				for(EnumFacing facing : event.getNotifiedSides()){
					BlockPos neighborPos = eventPos.offset(facing);
					if(world.isAreaLoaded(neighborPos, 1)){
						IBlockState neighborState = event.getWorld().getBlockState(neighborPos);
						if(isLeafBlock(neighborState.getBlock())){
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
						if(isLeafBlock(block)){
							block.randomTick(world, leafBreakingSchedule.getBlockPos(), state, world.rand);
						}
					}
					leavesBreak.remove();
				}
				else{
					leafBreakingSchedule.tick();
				}
			}
		}
	}
}
