package fr.raksrinana.fallingtree.forge.event;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.tree.exception.NoTreeFoundException;
import fr.raksrinana.fallingtree.common.tree.exception.NotServerException;
import fr.raksrinana.fallingtree.common.tree.exception.PlayerNotInRightState;
import fr.raksrinana.fallingtree.common.tree.exception.ToolUseForcedException;
import fr.raksrinana.fallingtree.common.tree.exception.TreeBreakingException;
import fr.raksrinana.fallingtree.common.tree.exception.TreeBreakingNotEnabledException;
import fr.raksrinana.fallingtree.forge.common.wrapper.BlockPosWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.LevelWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.PlayerWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
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
		var wrappedLevel = new LevelWrapper(event.getLevel());
		var wrappedPos = new BlockPosWrapper(event.getPos());
		
		try{
			var result = mod.getTreeHandler().breakTree(wrappedLevel, wrappedPlayer, wrappedPos);
			if(event.isCancelable()){
				switch(result.breakMode()){
					case INSTANTANEOUS -> event.setCanceled(result.shouldCancel());
					case SHIFT_DOWN -> event.setCanceled(true);
				}
			}
		}
		catch(TreeBreakingNotEnabledException | PlayerNotInRightState | TreeBreakingException | NoTreeFoundException | NotServerException ignored){
		}
		catch(ToolUseForcedException e){
			if(event.isCancelable()){
				event.setCanceled(true);
			}
		}
	}
}
