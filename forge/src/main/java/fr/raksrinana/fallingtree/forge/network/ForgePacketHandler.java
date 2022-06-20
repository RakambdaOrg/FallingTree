package fr.raksrinana.fallingtree.forge.network;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.network.ConfigurationPacket;
import fr.raksrinana.fallingtree.common.network.PacketHandler;
import fr.raksrinana.fallingtree.common.wrapper.IServerPlayer;
import fr.raksrinana.fallingtree.forge.FallingTree;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ForgePacketHandler implements PacketHandler{
	private static final String PROTOCOL_VERSION = "1";
	private static final Collection<String> ALLOWED_VERSIONS = Set.of(
			PROTOCOL_VERSION,
			NetworkRegistry.ABSENT,
			NetworkRegistry.ACCEPTVANILLA
	);
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(FallingTree.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			v -> ALLOWED_VERSIONS.stream().anyMatch(v::equals)
	);
	
	private static final int CONFIGURATION_MESSAGE_ID = 1;
	
	private final FallingTreeCommon<?> mod;
	
	public void register(){
		INSTANCE.registerMessage(CONFIGURATION_MESSAGE_ID, ConfigurationPacket.class, ConfigurationPacketCoding::encode, ConfigurationPacketCoding::decode, this::handleConfigurationPacket);
	}
	
	public void handleConfigurationPacket(ConfigurationPacket configurationPacket, Supplier<NetworkEvent.Context> contextSupplier){
		var context = contextSupplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> mod.getPacketUtils().onClientConfigurationPacket(configurationPacket)));
		context.setPacketHandled(true);
	}
	
	@Override
	public void onPlayerConnected(@NotNull IServerPlayer serverPlayer){
		var target = PacketDistributor.PLAYER.with(() -> (ServerPlayer) serverPlayer.getRaw());
		INSTANCE.send(target, mod.getPacketUtils().createConfigurationPacket());
	}
}
