package fr.rakambda.fallingtree.fabric.client;

import fr.rakambda.fallingtree.fabric.FallingTree;
import fr.rakambda.fallingtree.fabric.client.event.PlayerLeaveListener;
import fr.rakambda.fallingtree.fabric.client.network.FabricClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class FallingTreeClient implements ClientModInitializer{
	@Override
	public void onInitializeClient(){
		var mod = FallingTree.getMod();
		new FabricClientPacketHandler(mod).registerClient();
		
		ClientPlayConnectionEvents.DISCONNECT.register(new PlayerLeaveListener(mod));
	}
}
