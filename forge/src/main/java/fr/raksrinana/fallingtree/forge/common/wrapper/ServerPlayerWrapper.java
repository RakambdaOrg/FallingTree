package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IServerPlayer;
import lombok.ToString;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@ToString
public class ServerPlayerWrapper extends PlayerWrapper implements IServerPlayer{
	public ServerPlayerWrapper(@NotNull ServerPlayer raw){
		super(raw);
	}
}
