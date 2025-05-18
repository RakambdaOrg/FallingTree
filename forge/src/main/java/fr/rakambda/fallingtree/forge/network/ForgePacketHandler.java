package fr.rakambda.fallingtree.forge.network;

import fr.rakambda.fallingtree.common.network.ClientPacketHandler;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.forge.FallingTree;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import static fr.rakambda.fallingtree.forge.FallingTreeUtils.id;

@RequiredArgsConstructor
public class ForgePacketHandler implements ClientPacketHandler, ServerPacketHandler{
	public static final SimpleChannel INSTANCE = ChannelBuilder
			.named(id("main"))
			.optional()
			.networkProtocolVersion(0)
			.simpleChannel()
			.play()
			.clientbound()
			.add(FallingTreeConfigPacket.class, FallingTreeConfigPacket.CODEC, ForgePacketHandler::handleConfigurationPacket)
			.build();
	
	@Override
	public void registerServer(){
	}
	
	@Override
	public void registerClient(){
	}
	
	public static void handleConfigurationPacket(FallingTreeConfigPacket configurationPacket, CustomPayloadEvent.Context context){
		context.enqueueWork(() -> {
			if(FMLEnvironment.dist.isClient()){
				FallingTree.getMod().getPacketUtils().onClientConfigurationPacket(configurationPacket.getPacket());
			}
		});
		context.setPacketHandled(true);
	}
}
