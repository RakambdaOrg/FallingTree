package fr.raksrinana.fallingtree.fabric.event;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ServerPlayerWrapper;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlayerJoinListener implements ServerPlayConnectionEvents.Join{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server){
		mod.getServerPacketHandler().onPlayerConnected(new ServerPlayerWrapper(handler.getPlayer()));
	}
}
