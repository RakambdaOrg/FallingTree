package fr.raksrinana.fallingtree.common.leaf;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.wrapper.DirectionCompat;
import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.IBlockState;
import fr.raksrinana.fallingtree.common.wrapper.IServerLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class LeafBreakingHandler{
	private final Set<LeafBreakingSchedule> scheduledLeavesBreaking = ConcurrentHashMap.newKeySet();
	
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	public void onServerTick(){
		var leavesBreak = scheduledLeavesBreaking.iterator();
		while(leavesBreak.hasNext()){
			var leafBreakingSchedule = leavesBreak.next();
			
			if(leafBreakingSchedule.getRemainingTicks() > 0){
				leafBreakingSchedule.tick();
				continue;
			}
			
			var level = leafBreakingSchedule.getLevel();
			var chunk = level.getChunk(leafBreakingSchedule.getBlockPos());
			var chunkPos = chunk.getPos();
			
			if(level.hasChunk(chunkPos.getX(), chunkPos.getZ())){
				var state = level.getBlockState(leafBreakingSchedule.getBlockPos());
				state.tick(level, leafBreakingSchedule.getBlockPos(), level.getRandom());
				if(state.isRandomlyTicking()){
					state.randomTick(level, leafBreakingSchedule.getBlockPos(), level.getRandom());
				}
			}
			
			leavesBreak.remove();
		}
	}
	
	public void onBlockUpdate(@NotNull IServerLevel level, @NotNull IBlockPos eventPos, @NotNull IBlockState eventState, Set<DirectionCompat> directions){
		if(!mod.getConfiguration().getTrees().isLeavesBreaking()){
			return;
		}
		if(!level.isServer()){
			return;
		}
		
		var eventBlock = eventState.getBlock();
		if(!eventBlock.isAir()){
			return;
		}
		
		for(var direction : directions){
			var neighborPos = eventPos.relative(direction);
			var chunk = level.getChunk(neighborPos);
			var chunkPos = chunk.getPos();
			if(level.hasChunk(chunkPos.getX(), chunkPos.getZ())){
				var neighborState = level.getBlockState(neighborPos);
				if(mod.isLeafBlock(neighborState.getBlock())){
					addSchedule(new LeafBreakingSchedule(level, neighborPos, 4));
				}
			}
		}
	}
	
	public void addSchedule(@NotNull LeafBreakingSchedule schedule){
		scheduledLeavesBreaking.add(schedule);
	}
}
