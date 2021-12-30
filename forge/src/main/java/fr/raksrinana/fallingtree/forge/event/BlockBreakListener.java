package fr.raksrinana.fallingtree.forge.event;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.forge.common.wrapper.BlockPosWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.BlockStateWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.LevelWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.PlayerWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
		
		var wrappedPlayer = new PlayerWrapper(event.getPlayer());
		var wrappedState = new BlockStateWrapper(event.getState());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		
		var result = mod.getTreeHandler().getBreakSpeed(wrappedPlayer, wrappedState, wrappedPos, event.getNewSpeed());
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
		var wrappedLevel = new LevelWrapper(event.getWorld());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		
		var result = mod.getTreeHandler().breakTree(wrappedLevel, wrappedPlayer, wrappedPos);
		if(result.isEmpty()){
			return;
		}
		
		if(event.isCancelable()){
			switch(result.get().breakMode()){
				case INSTANTANEOUS -> event.setCanceled(result.get().shouldCancel());
				case SHIFT_DOWN -> event.setCanceled(true);
			}
		}
	}
}
