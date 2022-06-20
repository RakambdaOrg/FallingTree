package fr.raksrinana.fallingtree.fabric.client;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.client.network.FabricClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;

public class FallingTreeClient implements ClientModInitializer{
	@Override
	public void onInitializeClient(){
		var mod = FallingTree.getMod();
		new FabricClientPacketHandler(mod).registerClient();
	}
}
