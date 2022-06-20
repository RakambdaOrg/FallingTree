package fr.raksrinana.fallingtree.fabric.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.network.ServerPacketHandler;
import fr.raksrinana.fallingtree.common.wrapper.IServerPlayer;
import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.common.wrapper.FriendlyByteBufWrapper;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class FabricServerPacketHandler implements ServerPacketHandler{
	private static final ResourceLocation CONFIGURATION_MESSAGE_ID = new ResourceLocation(FallingTree.MOD_ID, "configurationMessageId");
	
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void registerServer(){
	}
	
	@Override
	public void onPlayerConnected(@NotNull IServerPlayer serverPlayer){
		var buf = new FriendlyByteBufWrapper(PacketByteBufs.create());
		var packet = mod.getPacketUtils().createConfigurationPacket();
		mod.getPacketUtils().encodeConfigurationPacket(packet, buf);
		ServerPlayNetworking.send((ServerPlayer) serverPlayer.getRaw(), CONFIGURATION_MESSAGE_ID, buf.getRaw());
	}
}
