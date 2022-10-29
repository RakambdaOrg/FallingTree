package fr.rakambda.fallingtree.fabric.client.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class PlayerLeaveListener implements ClientPlayConnectionEvents.Disconnect{
	private final FallingTreeCommon<?> mod;
	
	public PlayerLeaveListener(FallingTreeCommon<?> mod){
		this.mod = mod;
	}
	
	@Override
	public void onPlayDisconnect(ClientPacketListener handler, Minecraft client){
		mod.getPacketUtils().onClientDisconnect();
	}
}
