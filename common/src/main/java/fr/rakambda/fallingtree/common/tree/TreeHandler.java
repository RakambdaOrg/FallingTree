package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.tree.breaking.*;
import fr.rakambda.fallingtree.common.tree.builder.TreeTooBigException;
import fr.rakambda.fallingtree.common.tree.exception.NoTreeFoundException;
import fr.rakambda.fallingtree.common.tree.exception.NotServerException;
import fr.rakambda.fallingtree.common.tree.exception.PlayerNotInRightState;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.tree.exception.ToolUseForcedException;
import fr.rakambda.fallingtree.common.tree.exception.TreeBreakingException;
import fr.rakambda.fallingtree.common.tree.exception.TreeBreakingNotEnabledException;
import fr.rakambda.fallingtree.common.utils.CacheSpeed;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IEnchantment;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.Objects.isNull;

@Log4j2
@RequiredArgsConstructor
public class TreeHandler{
	@NotNull
	private final FallingTreeCommon<?> mod;
	private final Map<UUID, CacheSpeed> speedCache = new ConcurrentHashMap<>();
	
	@NotNull
	public IBreakAttemptResult attemptTreeBreaking(@NotNull ILevel level, @NotNull IPlayer player, @NotNull IBlockPos blockPos) {
		if(!level.isServer()){
			return BreakAbortionCause.NOT_SERVER;
		}
		if(!mod.getConfiguration().getTrees().isTreeBreaking()){
			return BreakAbortionCause.NOT_ENABLED;
		}
		
		if(!mod.checkForceToolUsage(player, level, blockPos)){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.force_tool_usage", mod.getConfiguration().getTrees().getMaxScanSize()));
			return BreakAbortionCause.REQUIRED_TOOL_ABSENT;
		}
		
		if(!mod.isPlayerInRightState(player)){
			return BreakAbortionCause.INVALID_PLAYER_STATE;
		}
		
		try{
			var treeOptional = mod.getTreeBuilder().getTree(player, level, blockPos);
			if(treeOptional.isEmpty()){
				return BreakAbortionCause.NO_SUCH_TREE;
			}
			
			var tree = treeOptional.get();
			var breakMode = getBreakMode(player.getMainHandItem());
			var result = getBreakingHandler(breakMode).breakTree(player, tree);
			return new BreakTreeResult(!result, breakMode);
		}
		catch(TreeTooBigException e){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.tree_too_big", mod.getConfiguration().getTrees().getMaxScanSize()));
			return BreakAbortionCause.TREE_TOO_BIG_SCAN;
		}
		catch(BreakTreeTooBigException e){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.break_tree_too_big", mod.getConfiguration().getTrees().getMaxSize()));
			return BreakAbortionCause.TREE_TOO_BIG_BREAK;
		}
	}

	@Deprecated
	@NotNull
	public BreakTreeResult breakTree(@NotNull ILevel level, @NotNull IPlayer player, @NotNull IBlockPos blockPos) throws TreeBreakingNotEnabledException, PlayerNotInRightState, ToolUseForcedException, TreeBreakingException, NoTreeFoundException, NotServerException{
		IBreakAttemptResult result = this.attemptTreeBreaking(level, player, blockPos);
		if (result instanceof BreakTreeResult ret) {
			return ret;
		}
		if (result instanceof BreakAbortionCause cause) {
			switch (cause) {
				case NOT_SERVER -> throw new NotServerException();
				case TREE_TOO_BIG_SCAN -> throw new TreeBreakingException("Tree exceeded scanning limits");
				case NO_SUCH_TREE -> throw new NoTreeFoundException();
				case INVALID_PLAYER_STATE -> throw new PlayerNotInRightState();
				case NOT_ENABLED -> throw new TreeBreakingNotEnabledException();
				case TREE_TOO_BIG_BREAK -> throw new TreeBreakingException("Tree exceeded breaking limits");
				case REQUIRED_TOOL_ABSENT -> throw new ToolUseForcedException();
			}
		}

		throw new TreeBreakingException("Unknown/Unsupported break result: " + result);
	}
	
	@NotNull
	private BreakMode getBreakMode(@NotNull IItemStack itemStack){
		return itemStack.getAnyEnchant(mod.getChopperEnchantments())
				.flatMap(IEnchantment::getBreakMode)
				.orElseGet(() -> mod.getConfiguration().getTrees().getBreakMode());
	}
	
	@NotNull
	private ITreeBreakingHandler getBreakingHandler(@NotNull BreakMode breakMode){
		return switch(breakMode){
			case INSTANTANEOUS -> InstantaneousTreeBreakingHandler.getInstance(mod);
			case FALL_ITEM -> FallingAnimationTreeBreakingHandler.getInstance(mod, true, true);
			case FALL_BLOCK -> FallingAnimationTreeBreakingHandler.getInstance(mod, false, true);
			case FALL_ALL_BLOCK -> FallingAnimationTreeBreakingHandler.getInstance(mod, false, false);
			case SHIFT_DOWN -> ShiftDownTreeBreakingHandler.getInstance(mod);
		};
	}
	
	@NotNull
	public Optional<Float> getBreakSpeed(@NotNull IPlayer player, @NotNull IBlockPos blockPos, float originalSpeed){
		if(!mod.getConfiguration().getTrees().isTreeBreaking()){
			return Optional.empty();
		}
		if(!getBreakMode(player.getMainHandItem()).isApplySpeedMultiplier()){
			return Optional.empty();
		}
		if(!mod.isPlayerInRightState(player)){
			return Optional.empty();
		}
		
		var cacheSpeed = speedCache.compute(player.getUUID(), (uuid, speed) -> {
			if(isNull(speed) || !speed.isValid(blockPos)){
				speed = getSpeed(player, blockPos, originalSpeed);
			}
			return speed;
		});
		return Optional.ofNullable(cacheSpeed).map(CacheSpeed::getSpeed);
	}
	
	@Nullable
	private CacheSpeed getSpeed(@NotNull IPlayer player, @NotNull IBlockPos pos, float originalSpeed){
		var speedMultiplicand = mod.getConfiguration().getTools().getSpeedMultiplicand();
		try{
			return speedMultiplicand <= 0 ? null :
					mod.getTreeBuilder().getTree(player, player.getLevel(), pos)
							.map(tree -> new CacheSpeed(pos, originalSpeed / ((float) speedMultiplicand * tree.getLogCount())))
							.orElse(null);
		}
		catch(TreeTooBigException e){
			return null;
		}
	}
}
