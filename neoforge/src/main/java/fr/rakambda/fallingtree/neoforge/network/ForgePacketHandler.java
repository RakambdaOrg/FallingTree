package fr.rakambda.fallingtree.neoforge.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ClientPacketHandler;
import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.neoforge.FallingTree;
import fr.rakambda.fallingtree.neoforge.common.wrapper.FriendlyByteBufWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

@RequiredArgsConstructor
public class ForgePacketHandler implements ClientPacketHandler, ServerPacketHandler{
	public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(FallingTree.MOD_ID, "main"))
			.clientAcceptedVersions(v -> NetworkRegistry.acceptsVanillaClientConnections())
			.serverAcceptedVersions(v -> NetworkRegistry.canConnectToVanillaServer())
			.simpleChannel();
	
	private static final int CONFIGURATION_MESSAGE_ID = 1;
	
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void registerServer(){
		INSTANCE.messageBuilder(ConfigurationPacket.class, CONFIGURATION_MESSAGE_ID)
				.decoder(buf -> ConfigurationPacket.read(new FriendlyByteBufWrapper(buf)))
				.encoder((packet, buf) -> packet.write(new FriendlyByteBufWrapper(buf)))
				.consumerMainThread(this::handleConfigurationPacket)
				.add();
	}
	
	@Override
	public void registerClient(){
	}
	
	public void handleConfigurationPacket(ConfigurationPacket configurationPacket, NetworkEvent.Context context){
		context.enqueueWork(() -> {
			if(FMLEnvironment.dist == Dist.CLIENT){
				mod.getPacketUtils().onClientConfigurationPacket(configurationPacket);
			}
		});
		context.setPacketHandled(true);
	}
}
