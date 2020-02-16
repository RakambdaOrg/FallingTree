package fr.raksrinana.fallingtree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class LeavesBlockToBreak{
	private final World world;
	private final BlockPos blockPos;
	
	public LeavesBlockToBreak(World world, BlockPos blockPos){
		this.world = world;
		this.blockPos = blockPos;
	}
	
	public World getWorld(){
		return world;
	}
	
	public BlockPos getBlockPos(){
		return blockPos;
	}
}
