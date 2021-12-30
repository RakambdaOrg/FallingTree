package fr.raksrinana.fallingtree.fabric.event;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.fabric.common.wrapper.BlockPosWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.LevelWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.PlayerWrapper;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BlockBreakListener implements PlayerBlockBreakEvents.Before{
	@NotNull
	private final FallingTreeCommon<?> mod;
	
	@Override
	public boolean beforeBlockBreak(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity){
		var wrappedPlayer = new PlayerWrapper(player);
		var wrappedLevel = new LevelWrapper(level);
		var wrappedPos = new BlockPosWrapper(blockPos);
		
		var result = mod.getTreeHandler().breakTree(wrappedLevel, wrappedPlayer, wrappedPos);
		if(result.isEmpty()){
			return true;
		}
		
		return switch(result.get().breakMode()){
			case INSTANTANEOUS -> !result.get().shouldCancel();
			case SHIFT_DOWN -> false;
		};
	}
}
