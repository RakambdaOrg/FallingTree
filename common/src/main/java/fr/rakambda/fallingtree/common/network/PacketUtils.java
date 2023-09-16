package fr.rakambda.fallingtree.common.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public class PacketUtils{
	private final FallingTreeCommon<?> mod;
	
	public void onClientConfigurationPacket(@NotNull ConfigurationPacket packet){
		log.info("Received FT configuration packet from server, setting up proxy config values");
		mod.getProxyConfiguration().getTools().setSpeedMultiplicand(packet.getSpeedMultiplicand());
		mod.getProxyConfiguration().getTools().setForceToolUsage(packet.isForceToolUsage());
		mod.getProxyConfiguration().getTrees().setBreakMode(packet.getBreakMode());
	}
	
	public void onClientDisconnect(){
		log.info("Disconnected from server, resetting proxy config values");
		mod.getProxyConfiguration().reset();
	}
}
