package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.wrapper.IServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface ServerPacketHandler{
	void registerServer();
	
	void onPlayerConnected(@NotNull IServerPlayer serverPlayer);
}
