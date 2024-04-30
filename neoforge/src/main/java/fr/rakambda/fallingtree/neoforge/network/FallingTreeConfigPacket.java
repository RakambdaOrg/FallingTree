package fr.rakambda.fallingtree.neoforge.network;

import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import fr.rakambda.fallingtree.neoforge.FallingTree;
import fr.rakambda.fallingtree.neoforge.common.wrapper.FriendlyByteBufWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class FallingTreeConfigPacket implements CustomPacketPayload{
	public static final Type<FallingTreeConfigPacket> TYPE = new Type<>(new ResourceLocation(FallingTree.MOD_ID, "configuration-packet"));
	public static final StreamCodec<FriendlyByteBuf, FallingTreeConfigPacket> CODEC = CustomPacketPayload.codec(
			FallingTreeConfigPacket::write,
			packet -> new FallingTreeConfigPacket(ConfigurationPacket.read(new FriendlyByteBufWrapper(packet))));
	
	@Getter
	private final ConfigurationPacket packet;
	
	public void write(FriendlyByteBuf buf){
		packet.write(new FriendlyByteBufWrapper(buf));
	}
	
	@Override
	@NotNull
	public Type<? extends CustomPacketPayload> type(){
		return TYPE;
	}
}
