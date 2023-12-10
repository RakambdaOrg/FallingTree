package fr.rakambda.fallingtree.neoforge.client.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import lombok.RequiredArgsConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class PlayerLeaveListener{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onPlayerLoggedOutEvent(@Nonnull ClientPlayerNetworkEvent.LoggingOut event){
		mod.getPacketUtils().onClientDisconnect();
	}
}
