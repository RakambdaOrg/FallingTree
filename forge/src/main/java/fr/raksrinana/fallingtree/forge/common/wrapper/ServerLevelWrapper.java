package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.IServerLevel;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class ServerLevelWrapper extends LevelWrapper implements IServerLevel{
	public ServerLevelWrapper(@NotNull ServerLevel serverLevel){
		super(serverLevel);
	}
}
