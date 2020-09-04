package fr.raksrinana.fallingtree.leaves;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import java.util.Set;

public class LeafBreakingHandler implements net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick{
	public static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	
	@Override
	public void onEndTick(MinecraftServer minecraftServer){
		Iterator<LeafBreakingSchedule> leavesBreak = scheduledLeavesBreaking.iterator();
		while(leavesBreak.hasNext()){
			LeafBreakingSchedule leafBreakingSchedule = leavesBreak.next();
			ServerWorld world = leafBreakingSchedule.getWorld();
			if(leafBreakingSchedule.getRemainingTicks() <= 0){
				Chunk chunk = world.getChunk(leafBreakingSchedule.getBlockPos());
				ChunkPos chunkPos = chunk.getPos();
				if(world.isChunkLoaded(chunkPos.x, chunkPos.z)){
					BlockState state = world.getBlockState(leafBreakingSchedule.getBlockPos());
					state.scheduledTick(world, leafBreakingSchedule.getBlockPos(), world.getRandom());
					if(state.hasRandomTicks()){
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
