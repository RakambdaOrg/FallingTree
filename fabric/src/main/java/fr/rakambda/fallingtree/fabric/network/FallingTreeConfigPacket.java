package fr.rakambda.fallingtree.fabric.network;

import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import fr.rakambda.fallingtree.fabric.FallingTree;
import fr.rakambda.fallingtree.fabric.common.wrapper.FriendlyByteBufWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@RequiredArgsConstructor
public class FallingTreeConfigPacket implements FabricPacket{
	public static final ResourceLocation MESSAGE_ID = new ResourceLocation(FallingTree.MOD_ID, "configuration-packet");
	public static final PacketType<FallingTreeConfigPacket> TYPE = PacketType.create(MESSAGE_ID, buf -> new FallingTreeConfigPacket(ConfigurationPacket.read(new FriendlyByteBufWrapper(buf))));
	
	@Getter
	private final ConfigurationPacket packet;
	
	@Override
	public void write(FriendlyByteBuf buf){
		packet.write(new FriendlyByteBufWrapper(buf));
	}
	
	@Override
	public PacketType<?> getType(){
		return TYPE;
	}
}
