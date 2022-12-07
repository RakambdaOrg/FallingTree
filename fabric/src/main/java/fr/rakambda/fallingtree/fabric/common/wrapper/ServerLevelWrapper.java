package fr.rakambda.fallingtree.fabric.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class ServerLevelWrapper extends LevelWrapper implements IServerLevel{
	public ServerLevelWrapper(@NotNull ServerLevel serverLevel){
		super(serverLevel);
	}
}
