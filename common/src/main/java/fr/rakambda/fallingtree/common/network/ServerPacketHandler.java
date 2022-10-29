package fr.rakambda.fallingtree.common.network;

import fr.rakambda.fallingtree.common.wrapper.IServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface ServerPacketHandler{
	void registerServer();
	
	void onPlayerConnected(@NotNull IServerPlayer serverPlayer);
}
