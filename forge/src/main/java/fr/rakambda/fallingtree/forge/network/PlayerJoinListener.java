package fr.rakambda.fallingtree.forge.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import fr.rakambda.fallingtree.common.network.ConfigurationPacket;
import fr.rakambda.fallingtree.forge.common.wrapper.PlayerWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PacketDistributor;
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
		if(event.isCanceled()){
			return;
		}
		
		if(event.getEntity() instanceof ServerPlayer serverPlayer){
			var server = serverPlayer.getServer();
			if(Objects.nonNull(server) && server.isDedicatedServer()){
				var packet = ConfigurationPacket.get(mod.getConfiguration());
				ForgePacketHandler.INSTANCE.send(packet, PacketDistributor.PLAYER.with(serverPlayer));
			}
			else{
				log.info("Player connected to a local world, not setting up proxy config");
			}
			
			if(ModList.get().isLoaded("veinminer")){
				new PlayerWrapper(event.getEntity()).sendMessage(mod.translate("chat.fallingtree.veinminer_incompatibility"), NotificationMode.CHAT);
			}
		}
	}
}
