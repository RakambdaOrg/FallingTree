package fr.rakambda.fallingtree.neoforge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.neoforge.common.wrapper.BlockBreakEventWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.BlockPosWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.BlockStateWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.LevelWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.PlayerWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import org.jspecify.annotations.NonNull;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class BlockBreakListener{
	@NonNull
	private final FallingTreeCommon<?> mod;
	
	@SubscribeEvent
	public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed event){
		if(event.isCanceled()){
			return;
		}
		
		var optionalPos = event.getPosition();
		if(optionalPos.isEmpty()){
			return;
		}
		
		var wrappedPlayer = new PlayerWrapper(event.getEntity());
		var wrappedPos = new BlockPosWrapper(optionalPos.get());
		var wrappedState = new BlockStateWrapper(event.getState());
		
		var result = mod.getTreeHandler().create(wrappedPlayer.getLevel(), wrappedPlayer, wrappedPos, wrappedState).getBreakSpeed(event.getNewSpeed());
		if(result.isEmpty()){
			return;
		}
		
		event.setNewSpeed(result.get());
	}
	
	@SubscribeEvent
	public void onBlockBreakEvent(@Nonnull BreakBlockEvent event){
		if(event.isCanceled()){
			return;
		}
		if(mod.isOwnEvent(new BlockBreakEventWrapper(event))){
			return;
		}
		
		var wrappedPlayer = new PlayerWrapper(event.getPlayer());
		var wrappedLevel = event.getLevel() instanceof ServerLevel serverLevel ? new ServerLevelWrapper(serverLevel) : new LevelWrapper(event.getLevel());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		var wrappedState = new BlockStateWrapper(event.getState());
		var wrappedEntity = wrappedLevel.getBlockEntity(wrappedPos);
		
		final var treeHandler = mod.getTreeHandler().create(wrappedLevel, wrappedPlayer, wrappedPos, wrappedState, wrappedEntity);
		
		if(treeHandler.shouldCancelEvent()){
			event.setCanceled(true);
			return;
		}
		
		var result = treeHandler.breakTree(true);
		if(result.shouldCancel()){
			event.setCanceled(true);
		}
	}
}
