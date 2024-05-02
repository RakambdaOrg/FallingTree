package fr.rakambda.fallingtree.fabric.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;

@RequiredArgsConstructor
public class FabricServerPacketHandler implements ServerPacketHandler{
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void registerServer(){
		PayloadTypeRegistry.configurationS2C().register(FallingTreeConfigPacket.TYPE, FallingTreeConfigPacket.CODEC);
		ServerConfigurationConnectionEvents.CONFIGURE.register(((handler, server) -> {
			var packet = ConfigurationPacket.get(mod.getConfiguration());
			ServerConfigurationNetworking.send(handler, new FallingTreeConfigPacket(packet));
		}));
	}
}
