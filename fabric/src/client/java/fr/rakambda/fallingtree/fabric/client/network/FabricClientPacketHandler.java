package fr.rakambda.fallingtree.fabric.client.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ClientPacketHandler;
import fr.rakambda.fallingtree.fabric.network.FallingTreeConfigPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;

public class FabricClientPacketHandler implements ClientPacketHandler{
	private final FallingTreeCommon<?> mod;
	
	public FabricClientPacketHandler(FallingTreeCommon<?> mod){
		this.mod = mod;
	}
	
	@Override
	public void registerClient(){
		ClientConfigurationNetworking.registerGlobalReceiver(FallingTreeConfigPacket.TYPE,
				(packet, sender) -> mod.getPacketUtils().onClientConfigurationPacket(packet.getPacket()));
	}
}
