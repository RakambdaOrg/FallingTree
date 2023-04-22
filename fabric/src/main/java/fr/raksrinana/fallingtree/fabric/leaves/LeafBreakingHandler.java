package fr.raksrinana.fallingtree.fabric.leaves;

import io.netty.util.internal.ConcurrentSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class LeafBreakingHandler implements ServerTickEvents.EndTick, ServerWorldEvents.Unload{
	public static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	
	@Override
	public void onEndTick(MinecraftServer minecraftServer){
		Iterator<LeafBreakingSchedule> leavesBreak = scheduledLeavesBreaking.iterator();
		while(leavesBreak.hasNext()){
			LeafBreakingSchedule leafBreakingSchedule = leavesBreak.next();
			ServerLevel world = leafBreakingSchedule.getWorld();
			if(leafBreakingSchedule.getRemainingTicks() <= 0){
				ChunkAccess chunk = world.getChunk(leafBreakingSchedule.getBlockPos());
				ChunkPos chunkPos = chunk.getPos();
				if(world.hasChunk(chunkPos.x, chunkPos.z)){
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
	
	@Override
	public void onWorldUnload(MinecraftServer server, ServerLevel world){
		scheduledLeavesBreaking.removeIf(leafBreakingSchedule -> Objects.equals(world, leafBreakingSchedule.getWorld()));
	}
}
