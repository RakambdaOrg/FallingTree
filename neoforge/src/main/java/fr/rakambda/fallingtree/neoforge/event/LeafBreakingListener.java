package fr.rakambda.fallingtree.neoforge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.neoforge.common.wrapper.BlockPosWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.BlockStateWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LeafBreakingListener{
	@NotNull
	private final FallingTreeCommon<Direction> mod;
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event){
		mod.getLeafBreakingHandler().onServerTick();
	}
	
	@SubscribeEvent
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
	
	@SubscribeEvent
	public void onWorldUnload(LevelEvent.Unload event){
		if(event.getLevel() instanceof ServerLevel serverLevel){
			mod.getLeafBreakingHandler().onWorldUnload(new ServerLevelWrapper(serverLevel));
		}
	}
}
