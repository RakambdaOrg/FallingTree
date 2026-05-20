package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.tree.breaking.FallingAnimationTreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.breaking.FallingAnimationTreeBreakingHandler.FallingAnimationTreeBreakingConfig;
import fr.rakambda.fallingtree.common.tree.breaking.ITreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.rakambda.fallingtree.common.utils.CacheSpeed;
import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
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
	
	@NonNull
	private BreakMode getBreakMode(@NonNull IItemStack itemStack){
		return itemStack.getBreakModeFromEnchant()
				.orElseGet(() -> mod.getConfiguration().getTrees().getBreakMode());
	}
	
	@NonNull
	private ITreeBreakingHandler getBreakingHandler(@NonNull BreakMode breakMode){
		return switch(breakMode){
			case INSTANTANEOUS -> InstantaneousTreeBreakingHandler.getInstance(mod);
			case FALL_ITEM -> FallingAnimationTreeBreakingHandler.getInstance(mod, FallingAnimationTreeBreakingConfig.withRandomSpread(true, true));
			case FALL_ITEM_STRAIGHT -> FallingAnimationTreeBreakingHandler.getInstance(mod, FallingAnimationTreeBreakingConfig.straightDown(true, true));
			case FALL_BLOCK -> FallingAnimationTreeBreakingHandler.getInstance(mod, FallingAnimationTreeBreakingConfig.withRandomSpread(false, true));
			case FALL_ALL_BLOCK -> FallingAnimationTreeBreakingHandler.getInstance(mod, FallingAnimationTreeBreakingConfig.withRandomSpread(false, false));
			case SHIFT_DOWN -> ShiftDownTreeBreakingHandler.getInstance(mod);
		};
	}
}
