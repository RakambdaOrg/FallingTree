package fr.raksrinana.fallingtree.forge;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class FallingTreeBlockBreakEvent extends BlockEvent.BreakEvent{
	public FallingTreeBlockBreakEvent(World world, BlockPos pos, BlockState state, PlayerEntity player){
		super(world, pos, state, player);
	}
	
	@Override
	public int getExpToDrop(){
		return 0;
	}
}
