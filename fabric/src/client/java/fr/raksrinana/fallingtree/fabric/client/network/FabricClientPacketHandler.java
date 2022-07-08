package fr.raksrinana.fallingtree.fabric.client.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.network.ClientPacketHandler;
import fr.raksrinana.fallingtree.fabric.common.wrapper.FriendlyByteBufWrapper;
import fr.raksrinana.fallingtree.fabric.network.FabricServerPacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricClientPacketHandler implements ClientPacketHandler{
	private final FallingTreeCommon<?> mod;
	
	public FabricClientPacketHandler(FallingTreeCommon<?> mod){
		this.mod = mod;
	}
	
	@Override
	public void registerClient(){
		ClientPlayNetworking.registerGlobalReceiver(FabricServerPacketHandler.CONFIGURATION_MESSAGE_ID, (client, handler, buf, responseSender) -> {
			var packet = mod.getPacketUtils().decodeConfigurationPacket(new FriendlyByteBufWrapper(buf));
			client.execute(() -> mod.getPacketUtils().onClientConfigurationPacket(packet));
		});
	}
}
