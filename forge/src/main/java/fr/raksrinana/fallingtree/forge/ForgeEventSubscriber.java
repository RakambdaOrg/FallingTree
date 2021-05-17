package fr.raksrinana.fallingtree.forge;

import fr.raksrinana.fallingtree.forge.config.BreakMode;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.config.ToolConfiguration;
import fr.raksrinana.fallingtree.forge.utils.CacheSpeed;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import fr.raksrinana.fallingtree.forge.utils.LeafBreakingSchedule;
import fr.raksrinana.fallingtree.forge.tree.breaking.ITreeBreakingHandler;
import fr.raksrinana.fallingtree.forge.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.raksrinana.fallingtree.forge.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.raksrinana.fallingtree.forge.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.forge.tree.builder.TreeTooBigException;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import javax.annotation.Nonnull;
import java.util.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Util.NIL_UUID;
import static net.minecraftforge.event.TickEvent.Phase.END;
import static net.minecraftforge.fml.LogicalSide.SERVER;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventSubscriber{
	private static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	private static final Map<UUID, CacheSpeed> speedCache = new HashMap<>();
	
	@SubscribeEvent
	public static void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed event){
		if(Config.COMMON.getTreesConfiguration().isTreeBreaking() && !event.isCanceled()){
			if(Config.COMMON.getTreesConfiguration().getBreakMode() == BreakMode.INSTANTANEOUS){
				if(isPlayerInRightState(event.getPlayer())){
					CacheSpeed cacheSpeed = speedCache.compute(event.getPlayer().getUUID(), (pos, speed) -> {
						if(isNull(speed) || !speed.isValid(event.getPos())){
							speed = getSpeed(event);
						}
						return speed;
					});
					if(nonNull(cacheSpeed)){
						event.setNewSpeed(cacheSpeed.getSpeed());
					}
				}
			}
		}
	}
	
	private static boolean isPlayerInRightState(PlayerEntity player){
		if(player.isCreative() && !Config.COMMON.isBreakInCreative()){
			return false;
		}
		if(Config.COMMON.isReverseSneaking() != player.isCrouching()){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	private static CacheSpeed getSpeed(PlayerEvent.BreakSpeed event){
		double speedMultiplicand = Config.COMMON.getToolsConfiguration().getSpeedMultiplicand();
		try{
			return speedMultiplicand <= 0 ? null :
					TreeBuilder.getTree(event.getEntity().getCommandSenderWorld(), event.getPos())
							.map(tree -> new CacheSpeed(event.getPos(), event.getNewSpeed() / ((float) speedMultiplicand * tree.getLogCount())))
							.orElse(null);
		}
		catch(TreeTooBigException e){
			return null;
		}
	}
	
	public static boolean canPlayerBreakTree(@Nonnull PlayerEntity player){
		ToolConfiguration toolConfiguration = Config.COMMON.getToolsConfiguration();
		Item heldItem = player.getItemInHand(MAIN_HAND).getItem();
		boolean isWhitelistedTool = toolConfiguration.isIgnoreTools()
				|| heldItem instanceof AxeItem
				|| toolConfiguration.getWhitelisted().stream().anyMatch(tool -> tool.equals(heldItem));
		if(isWhitelistedTool){
			boolean isBlacklistedTool = toolConfiguration.getBlacklisted().stream().anyMatch(tool -> tool.equals(heldItem));
			return !isBlacklistedTool;
		}
		return false;
	}
	
	@SubscribeEvent
	public static void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event){
		if(Config.COMMON.getTreesConfiguration().isLeavesBreaking()
				&& !event.getWorld().isClientSide()
				&& event.getWorld() instanceof ServerWorld){
			ServerWorld world = (ServerWorld) event.getWorld();
			BlockState eventState = event.getState();
			Block eventBlock = eventState.getBlock();
			BlockPos eventPos = event.getPos();
			if(eventBlock.isAir(eventState, world, eventPos)){
				for(Direction facing : event.getNotifiedSides()){
					BlockPos neighborPos = eventPos.relative(facing);
					if(world.isAreaLoaded(neighborPos, 1)){
						BlockState neighborState = event.getWorld().getBlockState(neighborPos);
						if(FallingTreeUtils.isLeafBlock(neighborState.getBlock())){
							scheduledLeavesBreaking.add(new LeafBreakingSchedule(world, neighborPos, 4));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(Config.COMMON.getTreesConfiguration().isTreeBreaking() && !event.isCanceled() && !event.getWorld().isClientSide()){
			if(event instanceof FallingTreeBlockBreakEvent){
				return;
			}
			if(isPlayerInRightState(event.getPlayer()) && event.getWorld() instanceof World){
				try{
					TreeBuilder.getTree((World) event.getWorld(), event.getPos()).ifPresent(tree -> {
						BreakMode breakMode = Config.COMMON.getTreesConfiguration().getBreakMode();
						getBreakingHandler(breakMode).breakTree(event, tree);
					});
				}
				catch(TreeTooBigException e){
					event.getPlayer().sendMessage(new TranslationTextComponent("chat.fallingtree.tree_too_big", Config.COMMON.getTreesConfiguration().getMaxSize()), NIL_UUID);
				}
			}
		}
	}
	
	public static ITreeBreakingHandler getBreakingHandler(BreakMode breakMode){
		if(breakMode == BreakMode.INSTANTANEOUS){
			return InstantaneousTreeBreakingHandler.getInstance();
		}
		return ShiftDownTreeBreakingHandler.getInstance();
	}
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event){
		if(event.side == SERVER && event.phase == END){
			Iterator<LeafBreakingSchedule> leavesBreak = scheduledLeavesBreaking.iterator();
			while(leavesBreak.hasNext()){
				LeafBreakingSchedule leafBreakingSchedule = leavesBreak.next();
				ServerWorld world = leafBreakingSchedule.getWorld();
				if(leafBreakingSchedule.getRemainingTicks() <= 0){
					if(world.isAreaLoaded(leafBreakingSchedule.getBlockPos(), 1)){
						BlockState state = world.getBlockState(leafBreakingSchedule.getBlockPos());
						state.tick(world, leafBreakingSchedule.getBlockPos(), world.getRandom());
						if(state.isRandomlyTicking()){
							state.randomTick(world, leafBreakingSchedule.getBlockPos(), world.getRandom());
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
