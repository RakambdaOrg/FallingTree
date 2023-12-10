package fr.rakambda.fallingtree.neoforge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.neoforge.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.jetbrains.annotations.NotNull;
import static net.neoforged.fml.LogicalSide.SERVER;
import static net.neoforged.neoforge.event.TickEvent.Phase.END;

@RequiredArgsConstructor
public class LeafBreakingListener{
	@NotNull
	private final FallingTreeCommon<Direction> mod;
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		if(event.side == SERVER && event.phase == END){
			mod.getLeafBreakingHandler().onServerTick();
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(LevelEvent.Unload event){
		if(event.getLevel() instanceof ServerLevel serverLevel){
			mod.getLeafBreakingHandler().onWorldUnload(new ServerLevelWrapper(serverLevel));
		}
	}
}
