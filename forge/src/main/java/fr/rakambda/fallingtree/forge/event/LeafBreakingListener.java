package fr.rakambda.fallingtree.forge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockPosWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockStateWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LeafBreakingListener{
	@NotNull
	private final FallingTreeCommon<Direction> mod;
	
	public void onServerTick(TickEvent.ServerTickEvent.Post event){
		mod.getLeafBreakingHandler().onServerTick();
	}
	
	public void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event){
		if(event.getLevel() instanceof ServerLevel level){
			var eventState = event.getState();
			var eventPos = event.getPos();
			
			mod.getLeafBreakingHandler().onBlockUpdate(
					new ServerLevelWrapper(level),
					new BlockPosWrapper(eventPos),
					new BlockStateWrapper(eventState),
					event.getNotifiedSides().stream().map(mod::asDirectionCompat).collect(Collectors.toSet()));
		}
	}
	
	public void onWorldUnload(LevelEvent.Unload event){
		if(event.getLevel() instanceof ServerLevel serverLevel){
			mod.getLeafBreakingHandler().onWorldUnload(new ServerLevelWrapper(serverLevel));
		}
	}
}
