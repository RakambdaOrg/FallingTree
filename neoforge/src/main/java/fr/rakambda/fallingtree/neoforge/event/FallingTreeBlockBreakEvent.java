package fr.rakambda.fallingtree.neoforge.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

public class FallingTreeBlockBreakEvent extends BreakBlockEvent{
	public FallingTreeBlockBreakEvent(Level level, BlockPos pos, BlockState state, Player player){
		super(level, pos, state, player);
	}
}
