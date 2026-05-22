package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.utils.CacheSpeed;
import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@RequiredArgsConstructor
public class TreeHandlerFactory{
	@NonNull
	private final FallingTreeCommon<?> mod;
	private final Map<UUID, CacheSpeed> speedCache = new ConcurrentHashMap<>();
	
	@NonNull
	public TreeHandler create(@NonNull ILevel level, @NonNull IPlayer player, @NonNull IBlockPos originPos, @NonNull IBlockState originState, @Nullable IBlockEntity originEntity){
		return new TreeHandler(mod, speedCache, player, level, originPos, originState, originEntity);
	}
}
