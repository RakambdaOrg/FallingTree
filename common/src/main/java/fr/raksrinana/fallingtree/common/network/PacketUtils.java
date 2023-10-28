package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import fr.raksrinana.fallingtree.common.wrapper.IFriendlyByteBuf;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public class PacketUtils{
	private final FallingTreeCommon<?> mod;
	
	@NotNull
	public ConfigurationPacket createConfigurationPacket(){
		return ConfigurationPacket.builder()
				.speedMultiplicand(mod.getConfiguration().getTools().getSpeedMultiplicand())
				.forceToolUsage(mod.getConfiguration().getTools().isForceToolUsage())
				.breakMode(mod.getConfiguration().getTrees().getBreakMode())
				.build();
	}
	
	public void onClientConfigurationPacket(@NotNull ConfigurationPacket packet){
		log.info("Received FT configuration packet from server, setting up proxy config values");
		mod.getProxyConfiguration().getTools().setSpeedMultiplicand(packet.getSpeedMultiplicand());
		mod.getProxyConfiguration().getTools().setForceToolUsage(packet.isForceToolUsage());
		mod.getProxyConfiguration().getTrees().setBreakMode(packet.getBreakMode());
	}
	
	public void encodeConfigurationPacket(@NotNull ConfigurationPacket packet, @NotNull IFriendlyByteBuf buf){
		buf.writeDouble(packet.getSpeedMultiplicand());
		buf.writeBoolean(packet.isForceToolUsage());
		buf.writeInteger(packet.getBreakMode().ordinal());
	}
	
	public void onClientDisconnect(){
		log.info("Disconnected from server, resetting proxy config values");
		mod.getProxyConfiguration().reset();
	}
	
	@NotNull
	public ConfigurationPacket decodeConfigurationPacket(@NotNull IFriendlyByteBuf buf){
		return ConfigurationPacket.builder()
				.speedMultiplicand(buf.readDouble())
				.forceToolUsage(buf.readBoolean())
				.breakMode(BreakMode.getValues()[buf.readInteger()])
				.build();
	}
}
