package fr.raksrinana.fallingtree.fabric.client.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.network.ClientPacketHandler;
import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.common.wrapper.FriendlyByteBufWrapper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class FabricClientPacketHandler implements ClientPacketHandler{
	private static final ResourceLocation CONFIGURATION_MESSAGE_ID = new ResourceLocation(FallingTree.MOD_ID, "configurationMessageId");
	
	private final FallingTreeCommon<?> mod;
	
	public FabricClientPacketHandler(FallingTreeCommon<?> mod){
		this.mod = mod;
	}
	
	@Override
	public void registerClient(){
		ClientPlayNetworking.registerGlobalReceiver(CONFIGURATION_MESSAGE_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				var packet = mod.getPacketUtils().decodeConfigurationPacket(new FriendlyByteBufWrapper(buf));
				mod.getPacketUtils().onClientConfigurationPacket(packet);
			});
		});
	}
}
