package fr.rakambda.fallingtree.fabric.client.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ClientPacketHandler;
import fr.rakambda.fallingtree.fabric.network.FabricServerPacketHandler;
import fr.rakambda.fallingtree.fabric.common.wrapper.FriendlyByteBufWrapper;
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
