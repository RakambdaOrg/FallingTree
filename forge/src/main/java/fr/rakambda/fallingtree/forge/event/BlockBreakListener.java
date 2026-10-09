package fr.rakambda.fallingtree.forge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockBreakEventWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockPosWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockStateWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.LevelWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.PlayerWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jspecify.annotations.NonNull;
import javax.annotation.Nonnull;
import java.util.Objects;

@RequiredArgsConstructor
public class BlockBreakListener{
	@NonNull
	private final FallingTreeCommon<?> mod;
	
	public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed event){
		if(Objects.isNull(event.getPosition())){
			return;
		}
		
		var wrappedPlayer = new PlayerWrapper(event.getEntity());
		var wrappedPos = new BlockPosWrapper(event.getPosition());
		var wrappedState = new BlockStateWrapper(event.getState());
		
		var result = mod.getTreeHandler(wrappedPlayer.getLevel(), wrappedPlayer, wrappedPos, wrappedState, null).getBreakSpeed(event.getNewSpeed());
		if(result.isEmpty()){
			return;
		}
		
		event.setNewSpeed(result.get());
	}
	
	/**
	 * @return true to stop the event from reaching other listeners. Preventing the block from being broken is done by denying the event.
	 */
	public boolean onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(mod.isOwnEvent(new BlockBreakEventWrapper(event))){
			return false;
		}
		// Already prevented, e.g. adventure mode or another mod
		if(event.getResult().isDenied()){
			return false;
		}
		
		var wrappedPlayer = new PlayerWrapper(event.getPlayer());
		var wrappedLevel = event.getLevel() instanceof ServerLevel serverLevel ? new ServerLevelWrapper(serverLevel) : new LevelWrapper(event.getLevel());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		var wrappedState = new BlockStateWrapper(event.getState());
		var wrappedEntity = wrappedLevel.getBlockEntity(wrappedPos);
		
		var treeHandler = mod.getTreeHandler(wrappedLevel, wrappedPlayer, wrappedPos, wrappedState, wrappedEntity);
		
		if(treeHandler.shouldCancelEvent()){
			return deny(event);
		}
		
		var result = treeHandler.breakTree(true);
		if(result.shouldCancel()){
			return deny(event);
		}
		return false;
	}
	
	private static boolean deny(@Nonnull BlockEvent.BreakEvent event){
		event.setResult(Result.DENY);
		return true;
	}
}
