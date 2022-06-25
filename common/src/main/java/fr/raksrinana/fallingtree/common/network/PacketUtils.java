package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import fr.raksrinana.fallingtree.common.wrapper.IFriendlyByteBuf;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PacketUtils{
	private final FallingTreeCommon<?> mod;
	
	@NotNull
	public ConfigurationPacket createConfigurationPacket(){
		return ConfigurationPacket.builder()
				.speedMultiplicand(mod.getConfiguration().getTools().getSpeedMultiplicand())
				.breakMode(mod.getConfiguration().getTrees().getBreakMode())
				.build();
	}
	
	public void onClientConfigurationPacket(@NotNull ConfigurationPacket packet){
		mod.getConfiguration().getTools().setSpeedMultiplicand(packet.getSpeedMultiplicand());
		mod.getConfiguration().getTrees().setBreakMode(packet.getBreakMode());
	}
	
	public void encodeConfigurationPacket(@NotNull ConfigurationPacket packet, @NotNull IFriendlyByteBuf buf){
		buf.writeDouble(packet.getSpeedMultiplicand());
		buf.writeInteger(packet.getBreakMode().ordinal());
	}
	
	@NotNull
	public ConfigurationPacket decodeConfigurationPacket(@NotNull IFriendlyByteBuf buf){
		return ConfigurationPacket.builder()
				.speedMultiplicand(buf.readDouble())
				.breakMode(BreakMode.values()[buf.readInteger()])
				.build();
	}
}
