package fr.rakambda.fallingtree.fabric.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.exception.NoTreeFoundException;
import fr.rakambda.fallingtree.common.tree.exception.NotServerException;
import fr.rakambda.fallingtree.common.tree.exception.PlayerNotInRightState;
import fr.rakambda.fallingtree.common.tree.exception.ToolUseForcedException;
import fr.rakambda.fallingtree.common.tree.exception.TreeBreakingException;
import fr.rakambda.fallingtree.common.tree.exception.TreeBreakingNotEnabledException;
import fr.rakambda.fallingtree.fabric.common.wrapper.BlockPosWrapper;
import fr.rakambda.fallingtree.fabric.common.wrapper.LevelWrapper;
import fr.rakambda.fallingtree.fabric.common.wrapper.PlayerWrapper;
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
		
		try{
			var result = mod.getTreeHandler().breakTree(wrappedLevel, wrappedPlayer, wrappedPos);
			return switch(result.breakMode()){
				case INSTANTANEOUS, FALL_ITEM, FALL_BLOCK -> !result.shouldCancel();
				case SHIFT_DOWN -> false;
			};
		}
		catch(TreeBreakingNotEnabledException | PlayerNotInRightState | TreeBreakingException | NoTreeFoundException | NotServerException e){
			return true;
		}
		catch(ToolUseForcedException e){
			return false;
		}
	}
}
