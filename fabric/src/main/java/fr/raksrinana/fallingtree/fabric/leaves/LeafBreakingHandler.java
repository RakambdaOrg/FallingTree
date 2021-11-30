package fr.raksrinana.fallingtree.fabric.leaves;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LeafBreakingHandler implements ServerTickEvents.EndTick{
	public static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = ConcurrentHashMap.newKeySet();
	
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
}
