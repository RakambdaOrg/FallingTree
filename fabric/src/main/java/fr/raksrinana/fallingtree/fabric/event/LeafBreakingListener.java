package fr.raksrinana.fallingtree.fabric.event;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class LeafBreakingListener implements ServerTickEvents.EndTick, ServerWorldEvents.Unload{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void onEndTick(MinecraftServer minecraftServer){
		mod.getLeafBreakingHandler().onServerTick();
	}
	
	@Override
	public void onWorldUnload(MinecraftServer server, ServerLevel world){
		mod.getLeafBreakingHandler().onWorldUnload(new ServerLevelWrapper(world));
	}
}
