package fr.raksrinana.fallingtree.forge.leaves;

import fr.raksrinana.fallingtree.forge.FallingTree;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Objects;
import java.util.Set;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraftforge.event.TickEvent.Phase.END;
import static net.minecraftforge.fml.LogicalSide.SERVER;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LeafBreakingHandler{
	private static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event){
		if(event.side == SERVER && event.phase == END){
			var leavesBreak = scheduledLeavesBreaking.iterator();
			while(leavesBreak.hasNext()){
				var leafBreakingSchedule = leavesBreak.next();
				var level = leafBreakingSchedule.getLevel();
				if(leafBreakingSchedule.getRemainingTicks() <= 0){
					if(level.isAreaLoaded(leafBreakingSchedule.getBlockPos(), 1)){
						var state = level.getBlockState(leafBreakingSchedule.getBlockPos());
						state.tick(level, leafBreakingSchedule.getBlockPos(), level.getRandom());
						if(state.isRandomlyTicking()){
							state.randomTick(level, leafBreakingSchedule.getBlockPos(), level.getRandom());
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
	
	@SubscribeEvent
	public static void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event){
		if(Config.COMMON.getTrees().isLeavesBreaking()
				&& !event.getWorld().isClientSide()
				&& event.getWorld() instanceof ServerLevel level){
			var eventState = event.getState();
			var eventBlock = eventState.getBlock();
			var eventPos = event.getPos();
			if(eventBlock.equals(AIR)){
				for(var facing : event.getNotifiedSides()){
					var neighborPos = eventPos.relative(facing);
					if(level.isAreaLoaded(neighborPos, 1)){
						var neighborState = event.getWorld().getBlockState(neighborPos);
						if(FallingTreeUtils.isLeafBlock(neighborState.getBlock())){
							scheduledLeavesBreaking.add(new LeafBreakingSchedule(level, neighborPos, 4));
						}
					}
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event){
		scheduledLeavesBreaking.removeIf(leafBreakingSchedule -> Objects.equals(event.getWorld(), leafBreakingSchedule.getLevel()));
	}
}
