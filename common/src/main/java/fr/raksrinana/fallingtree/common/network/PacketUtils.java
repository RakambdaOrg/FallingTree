package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.wrapper.IFriendlyByteBuf;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PacketUtils{
	private final FallingTreeCommon<?> mod;
	
	@NotNull
	public ConfigurationPacket createConfigurationPacket(){
		return new ConfigurationPacket(mod.getConfiguration().getTools().getSpeedMultiplicand());
	}
	
	public void onClientConfigurationPacket(@NotNull ConfigurationPacket packet){
		mod.getConfiguration().getTools().setSpeedMultiplicand(packet.getSpeedMultiplicand());
	}
	
	@NotNull
	public void encodeConfigurationPacket(@NotNull ConfigurationPacket packet, @NotNull IFriendlyByteBuf buf){
		buf.writeDouble(packet.getSpeedMultiplicand());
	}
	
	@NotNull
	public ConfigurationPacket decodeConfigurationPacket(@NotNull IFriendlyByteBuf buf){
		return new ConfigurationPacket(buf.readDouble());
	}
}
