package fr.rakambda.fallingtree.neoforge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.neoforge.common.wrapper.BlockPosWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.LevelWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.PlayerWrapper;
import fr.rakambda.fallingtree.neoforge.common.wrapper.ServerLevelWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class BlockBreakListener{
	@NotNull
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
		
		var result = mod.getTreeHandler().getBreakSpeed(wrappedPlayer, wrappedPos, event.getNewSpeed());
		if(result.isEmpty()){
			return;
		}
		
		event.setNewSpeed(result.get());
	}
	
	@SubscribeEvent
	public void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(event.isCanceled()){
			return;
		}
		if(event instanceof FallingTreeBlockBreakEvent){
			return;
		}
		
		var wrappedPlayer = new PlayerWrapper(event.getPlayer());
		var wrappedLevel = event.getLevel() instanceof ServerLevel serverLevel ? new ServerLevelWrapper(serverLevel) : new LevelWrapper(event.getLevel());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		
		var result = mod.getTreeHandler().breakTree(wrappedLevel, wrappedPlayer, wrappedPos);
		if(result.shouldCancel()){
			event.setCanceled(true);
		}
	}
}
