package fr.rakambda.fallingtree.neoforge.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ClientPacketHandler;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.neoforge.FallingTree;
import lombok.RequiredArgsConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class NeoForgePacketHandler implements ClientPacketHandler, ServerPacketHandler{
	@SubscribeEvent
	public void register(@NotNull RegisterPayloadHandlersEvent event){
		var registrar = event.registrar(FallingTree.MOD_ID).optional();
		
		registrar.configurationToClient(FallingTreeConfigPacket.TYPE, FallingTreeConfigPacket.CODEC,
				(packet, sender) -> mod.getPacketUtils().onClientConfigurationPacket(packet.getPacket()));
	}
	
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void registerServer(){
	}
	
	@Override
	public void registerClient(){
	}
}
