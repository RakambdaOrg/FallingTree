package fr.raksrinana.fallingtree.fabric.event;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ServerPlayerWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public class PlayerJoinListener implements ServerPlayConnectionEvents.Join{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server){
		if(server.isDedicatedServer()){
			mod.getServerPacketHandler().onPlayerConnected(new ServerPlayerWrapper(handler.getPlayer()));
		}
		else{
			log.info("Player connected to a local world, not setting up proxy config");
		}
	}
}
