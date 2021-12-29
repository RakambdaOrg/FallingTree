package fr.raksrinana.fallingtree.forge.leaves;

import fr.raksrinana.fallingtree.forge.FallingTree;
import fr.raksrinana.fallingtree.forge.config.Configuration;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraftforge.event.TickEvent.Phase.END;
import static net.minecraftforge.fml.LogicalSide.SERVER;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LeafBreakingHandler{
	private static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = ConcurrentHashMap.newKeySet();
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event){
		if(event.side == SERVER && event.phase == END){
			var leavesBreak = scheduledLeavesBreaking.iterator();
			while(leavesBreak.hasNext()){
				var leafBreakingSchedule = leavesBreak.next();
				var level = leafBreakingSchedule.getLevel();
				if(leafBreakingSchedule.getRemainingTicks() <= 0){
					var chunk = level.getChunk(leafBreakingSchedule.getBlockPos());
					var chunkPos = chunk.getPos();
					if(level.hasChunk(chunkPos.x, chunkPos.z)){
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
		if(Configuration.getInstance().getTrees().isLeavesBreaking()
		   && !event.getWorld().isClientSide()
		   && event.getWorld() instanceof ServerLevel level){
			var eventState = event.getState();
			var eventBlock = eventState.getBlock();
			var eventPos = event.getPos();
			if(eventBlock.equals(AIR)){
				for(var facing : event.getNotifiedSides()){
					var neighborPos = eventPos.relative(facing);
					var chunk = level.getChunk(neighborPos);
					var chunkPos = chunk.getPos();
					if(level.hasChunk(chunkPos.x, chunkPos.z)){
						var neighborState = event.getWorld().getBlockState(neighborPos);
						if(FallingTreeUtils.isLeafBlock(neighborState.getBlock())){
							scheduledLeavesBreaking.add(new LeafBreakingSchedule(level, neighborPos, 4));
						}
					}
				}
			}
		}
	}
}
