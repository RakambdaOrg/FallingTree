package fr.rakambda.fallingtree.neoforge.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
public class PlayerJoinListener{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@SubscribeEvent
	public void onPlayerLoggedInEvent(@Nonnull PlayerEvent.PlayerLoggedInEvent event){
		if(event.getEntity() instanceof ServerPlayer serverPlayer){
			var server = serverPlayer.getServer();
			if(Objects.nonNull(server) && server.isDedicatedServer()){
				var packet = ConfigurationPacket.get(mod.getConfiguration());
				ForgePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
			}
			else{
				log.info("Player connected to a local world, not setting up proxy config");
			}
		}
	}
}
