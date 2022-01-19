package fr.raksrinana.fallingtree.common.tree;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import fr.raksrinana.fallingtree.common.tree.breaking.BreakTreeTooBigException;
import fr.raksrinana.fallingtree.common.tree.breaking.ITreeBreakingHandler;
import fr.raksrinana.fallingtree.common.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.raksrinana.fallingtree.common.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.raksrinana.fallingtree.common.tree.builder.TreeTooBigException;
import fr.raksrinana.fallingtree.common.utils.CacheSpeed;
import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.IBlockState;
import fr.raksrinana.fallingtree.common.wrapper.ILevel;
import fr.raksrinana.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class TreeHandler{
	@NotNull
	private final FallingTreeCommon<?> mod;
	private final Map<UUID, CacheSpeed> speedCache = new ConcurrentHashMap<>();
	
	@NotNull
	public Optional<BreakTreeResult> breakTree(@NotNull ILevel level, @NotNull IPlayer player, @NotNull IBlockPos blockPos){
		if(!level.isServer()){
			return Optional.empty();
		}
		if(!mod.getConfiguration().getTrees().isTreeBreaking()){
			return Optional.empty();
		}
		
		var blockState = level.getBlockState(blockPos);
		if(!mod.isPlayerInRightState(player, blockState)){
			return Optional.empty();
		}
		
		try{
			var treeOptional = mod.getTreeBuilder().getTree(player, level, blockPos);
			if(treeOptional.isEmpty()){
				return Optional.empty();
			}
			
			var tree = treeOptional.get();
			var breakMode = mod.getConfiguration().getTrees().getBreakMode();
			var result = getBreakingHandler(breakMode).breakTree(player, tree);
			return Optional.of(new BreakTreeResult(!result, breakMode));
		}
		catch(TreeTooBigException e){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.tree_too_big", mod.getConfiguration().getTrees().getMaxScanSize()));
			return Optional.empty();
		}
		catch(BreakTreeTooBigException e){
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.break_tree_too_big", mod.getConfiguration().getTrees().getMaxSize()));
			return Optional.empty();
		}
	}
	
	@NotNull
	private ITreeBreakingHandler getBreakingHandler(@NotNull BreakMode breakMode){
		return switch(breakMode){
			case INSTANTANEOUS -> InstantaneousTreeBreakingHandler.getInstance(mod);
			case SHIFT_DOWN -> ShiftDownTreeBreakingHandler.getInstance(mod);
		};
	}
	
	@NotNull
	public Optional<Float> getBreakSpeed(@NotNull IPlayer player, @NotNull IBlockState blockState, @NotNull IBlockPos blockPos, float originalSpeed){
		if(!mod.getConfiguration().getTrees().isTreeBreaking()){
			return Optional.empty();
		}
		if(mod.getConfiguration().getTrees().getBreakMode() != BreakMode.INSTANTANEOUS){
			return Optional.empty();
		}
		if(!mod.isPlayerInRightState(player, blockState)){
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
