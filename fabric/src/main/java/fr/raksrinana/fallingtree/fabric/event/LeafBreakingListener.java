package fr.raksrinana.fallingtree.fabric.event;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class LeafBreakingListener implements ServerTickEvents.EndTick{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void onEndTick(MinecraftServer minecraftServer){
		mod.getLeafBreakingHandler().onServerTick();
	}
}
