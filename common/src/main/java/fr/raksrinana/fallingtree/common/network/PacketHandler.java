package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.wrapper.IServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface PacketHandler{
	void register();
	
	void onPlayerConnected(@NotNull IServerPlayer serverPlayer);
}
