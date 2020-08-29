package fr.raksrinana.fallingtree.leaves;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import java.util.Iterator;
import java.util.Set;
import static fr.raksrinana.fallingtree.FallingTreeUtils.isLeafBlock;

public class LeafBreakingHandler implements net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick{
	public static final Set<LeafBreakingSchedule> scheduledLeavesBreaking = new ConcurrentSet<>();
	
	@Override
	public void onEndTick(MinecraftServer minecraftServer){
		Iterator<LeafBreakingSchedule> leavesBreak = scheduledLeavesBreaking.iterator();
		while(leavesBreak.hasNext()){
			LeafBreakingSchedule leafBreakingSchedule = leavesBreak.next();
			ServerWorld world = leafBreakingSchedule.getWorld();
			if(leafBreakingSchedule.getRemainingTicks() <= 0){
				if(world.isChunkLoaded(leafBreakingSchedule.getBlockPos())){
					BlockState state = world.getBlockState(leafBreakingSchedule.getBlockPos());
					Block block = state.getBlock();
					if(isLeafBlock(block)){
						block.randomTick(state, world, leafBreakingSchedule.getBlockPos(), world.getRandom());
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
