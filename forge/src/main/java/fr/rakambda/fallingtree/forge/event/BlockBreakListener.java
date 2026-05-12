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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jspecify.annotations.NonNull;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class BlockBreakListener{
	@NonNull
	private final FallingTreeCommon<?> mod;
	
	public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed event){
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
	
	public boolean onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(mod.isOwnEvent(new BlockBreakEventWrapper(event))){
			return false;
		}
		
		var wrappedPlayer = new PlayerWrapper(event.getPlayer());
		var wrappedLevel = event.getLevel() instanceof ServerLevel serverLevel ? new ServerLevelWrapper(serverLevel) : new LevelWrapper(event.getLevel());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		var wrappedState = new BlockStateWrapper(event.getState());
		var wrappedEntity = wrappedLevel.getBlockEntity(wrappedPos);
		
		final var treeHandler = mod.getTreeHandler().create(wrappedLevel, wrappedPlayer, wrappedPos, wrappedState, wrappedEntity);
		
		if(treeHandler.shouldCancelEvent()){
			return true;
		}
		
		var result = treeHandler.breakTree(true);
		return result.shouldCancel();
	}
}
