package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.tree.breaking.BreakTreeTooBigException;
import fr.rakambda.fallingtree.common.tree.breaking.BreakTreeTooSmallException;
import fr.rakambda.fallingtree.common.tree.breaking.FallingAnimationTreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.breaking.FallingAnimationTreeBreakingHandler.FallingAnimationTreeBreakingConfig;
import fr.rakambda.fallingtree.common.tree.breaking.ITreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.rakambda.fallingtree.common.tree.builder.TreeTooBigException;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import static java.util.Objects.isNull;

@Log4j2
@RequiredArgsConstructor
public class TreeHandler{
	@NonNull
	private final FallingTreeCommon<?> mod;
	@NonNull
	private final Map<UUID, CacheSpeed> speedCache;
	
	@NonNull
	private final IPlayer player;
	@NonNull
	private final ILevel level;
	@NonNull
	private final IBlockPos originPos;
	@NonNull
	private final IBlockState originState;
	@Nullable
	private final IBlockEntity originEntity;
	
	@Nullable
	private Tree cachedTree = null;
	
	@Nullable
	public Tree getTree() throws TreeTooBigException{
		if(Objects.isNull(cachedTree)){
			cachedTree = mod.getTreeBuilder().getTree(player, level, originPos, originState, originEntity).orElse(null);
		}
		return cachedTree;
	}
	
	public boolean shouldCancelEvent(){
		if(!mod.isPlayerInRightState(player)){
			return false;
		}
		if(shouldPreserveTool(player)){
			return true;
		}
		try{
			getTree();
		}
		catch(TreeTooBigException e){
			return false;
		}
		return false;
	}
	
	@NonNull
	public IBreakAttemptResult breakTree(boolean isCancellable){
		if(!level.isServer()){
			return AbortedResult.NOT_SERVER;
		}
		if(!mod.getConfiguration().getTrees().isTreeBreaking()){
			return AbortedResult.NOT_ENABLED;
		}
		
		if(!mod.checkForceToolUsage(player, level, originPos)){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.force_tool_usage", mod.getConfiguration().getTrees().getMaxScanSize()));
			return AbortedResult.REQUIRED_TOOL_ABSENT;
		}
		
		if(!mod.isPlayerInRightState(player)){
			return AbortedResult.INVALID_PLAYER_STATE;
		}
		
		try{
			var tree = getTree();
			if(tree == null){
				return AbortedResult.NO_SUCH_TREE;
			}
			
			var breakMode = getBreakMode(player.getMainHandItem());
			return getBreakingHandler(breakMode).breakTree(isCancellable, player, tree);
		}
		catch(TreeTooBigException e){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.tree_too_big", mod.getConfiguration().getTrees().getMaxScanSize()));
			return AbortedResult.TREE_TOO_BIG_SCAN;
		}
		catch(BreakTreeTooSmallException e){
			// mod.notifyPlayer(player, mod.translate("chat.fallingtree.break_tree_too_small", mod.getConfiguration().getTrees().getMinSize()));
			return AbortedResult.TREE_TOO_SMALL_BREAK;
		}
		catch(BreakTreeTooBigException e){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.break_tree_too_big", mod.getConfiguration().getTrees().getMaxSize()));
			return AbortedResult.TREE_TOO_BIG_BREAK;
		}
	}
	
	@NonNull
	public Optional<Float> getBreakSpeed(float originalSpeed){
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
			if(isNull(speed) || !speed.isValid(originPos)){
				speed = getSpeed(originalSpeed);
			}
			return speed;
		});
		return Optional.ofNullable(cacheSpeed).map(CacheSpeed::getSpeed);
	}
	
	@Nullable
	private CacheSpeed getSpeed(float originalSpeed){
		var speedMultiplicand = mod.getConfiguration().getTools().getSpeedMultiplicand();
		try{
			return speedMultiplicand <= 0
					? null
					: Optional.ofNullable(getTree())
					.map(tree -> new CacheSpeed(originPos, originalSpeed / ((float) speedMultiplicand * tree.getLogCount())))
					.orElse(null);
		}
		catch(TreeTooBigException e){
			return null;
		}
	}
	
	private boolean shouldPreserveTool(@NonNull IPlayer player){
		var handItem = player.getMainHandItem();
		return mod.getConfiguration().getTools().getDurabilityMode().shouldPreserve(handItem.getDurability());
	}
	
	@NonNull
	private BreakMode getBreakMode(@NonNull IItemStack itemStack){
		return itemStack.getBreakModeFromEnchant().orElseGet(() -> mod.getConfiguration().getTrees().getBreakMode());
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
