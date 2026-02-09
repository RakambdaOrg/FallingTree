package fr.rakambda.fallingtree.fabric.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.fabric.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class LeafBreakingListener implements ServerTickEvents.EndTick, ServerLevelEvents.Unload{
	@NonNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void onEndTick(@NonNull MinecraftServer minecraftServer){
		mod.getLeafBreakingHandler().onServerTick();
	}
	
	@Override
	public void onLevelUnload(@NonNull MinecraftServer server, @NonNull ServerLevel world){
		mod.getLeafBreakingHandler().onWorldUnload(new ServerLevelWrapper(world));
	}
}
