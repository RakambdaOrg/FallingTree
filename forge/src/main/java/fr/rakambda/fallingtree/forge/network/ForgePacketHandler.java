package fr.rakambda.fallingtree.forge.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ClientPacketHandler;
import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.forge.common.wrapper.FriendlyByteBufWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.SimpleChannel;
import static fr.rakambda.fallingtree.forge.FallingTreeUtils.id;

@RequiredArgsConstructor
public class ForgePacketHandler implements ClientPacketHandler, ServerPacketHandler{
	public static final SimpleChannel INSTANCE = net.minecraftforge.network.ChannelBuilder.named(id("main"))
			.optional()
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
	
	public void handleConfigurationPacket(ConfigurationPacket configurationPacket, CustomPayloadEvent.Context context){
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> mod.getPacketUtils().onClientConfigurationPacket(configurationPacket)));
		context.setPacketHandled(true);
	}
}
