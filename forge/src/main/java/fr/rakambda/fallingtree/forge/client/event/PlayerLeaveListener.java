package fr.rakambda.fallingtree.forge.client.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class PlayerLeaveListener{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@OnlyIn(Dist.CLIENT)
	public void onPlayerLoggedOutEvent(@Nonnull ClientPlayerNetworkEvent.LoggingOut event){
		mod.getPacketUtils().onClientDisconnect();
	}
}
