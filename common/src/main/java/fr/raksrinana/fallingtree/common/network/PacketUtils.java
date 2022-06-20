package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PacketUtils{
	private final FallingTreeCommon<?> mod;
	
	@NotNull
	public ConfigurationPacket createConfigurationPacket(){
		return new ConfigurationPacket(mod.getConfiguration().getTools().getSpeedMultiplicand());
	}
	
	public void onClientConfigurationPacket(ConfigurationPacket packet){
		mod.getConfiguration().getTools().setSpeedMultiplicand(packet.getSpeedMultiplicand());
	}
}
