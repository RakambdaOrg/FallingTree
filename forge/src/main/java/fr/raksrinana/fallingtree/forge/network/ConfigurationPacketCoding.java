package fr.raksrinana.fallingtree.forge.network;

import fr.raksrinana.fallingtree.common.network.ConfigurationPacket;
import net.minecraft.network.FriendlyByteBuf;

public class ConfigurationPacketCoding{
	public static void encode(ConfigurationPacket packet, FriendlyByteBuf buf){
		buf.writeDouble(packet.getSpeedMultiplicand());
	}
	
	public static ConfigurationPacket decode(FriendlyByteBuf buf){
		return new ConfigurationPacket(buf.readDouble());
	}
}
