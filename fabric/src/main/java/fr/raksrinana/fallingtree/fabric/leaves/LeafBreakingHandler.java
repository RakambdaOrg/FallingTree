package fr.raksrinana.fallingtree.fabric.leaves;

import io.netty.util.internal.ConcurrentSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;
import java.util.Set;

public class LeafBreakingHandler implements ServerTickEvents.EndTick, ServerWorldEvents.Unload{
	public static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	
	@Override
	public void onEndTick(MinecraftServer minecraftServer){
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
	
	@Override
	public void onWorldUnload(MinecraftServer server, ServerLevel world){
		scheduledLeavesBreaking.removeIf(leafBreakingSchedule -> Objects.equals(world, leafBreakingSchedule.getLevel()));
	}
}
