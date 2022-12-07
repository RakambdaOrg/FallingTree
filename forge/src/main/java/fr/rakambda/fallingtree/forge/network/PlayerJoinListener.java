package fr.rakambda.fallingtree.forge.network;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.forge.common.wrapper.ServerPlayerWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
				mod.getServerPacketHandler().onPlayerConnected(new ServerPlayerWrapper(serverPlayer));
			}
			else{
				log.info("Player connected to a local world, not setting up proxy config");
			}
		}
	}
}
