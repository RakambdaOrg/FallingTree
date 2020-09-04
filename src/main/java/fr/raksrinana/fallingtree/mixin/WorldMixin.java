package fr.raksrinana.fallingtree.mixin;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.leaves.LeafBreakingHandler;
import fr.raksrinana.fallingtree.leaves.LeafBreakingSchedule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.EnumSet;
import static fr.raksrinana.fallingtree.FallingTreeUtils.isLeafBlock;

@Mixin(World.class)
public abstract class WorldMixin{
	@Inject(method = "updateNeighborsAlways", at = @At(value = "TAIL"))
	public void updateNeighborsAlways(BlockPos pos, Block block, CallbackInfo callbackInfo){
		World world = (World) (Object) this;
		if(world instanceof ServerWorld){
			onUpdate((ServerWorld) world, pos, world.getBlockState(pos), EnumSet.allOf(Direction.class));
		}
	}
	
	private static void onUpdate(ServerWorld world, BlockPos eventPos, BlockState eventState, EnumSet<Direction> notifiedSides){
		if(FallingTree.config.getTreesConfiguration().isLeavesBreaking()){
			Block eventBlock = eventState.getBlock();
			if(eventBlock.is(Blocks.AIR)){
				for(Direction facing : notifiedSides){
					BlockPos neighborPos = eventPos.offset(facing);
					Chunk chunk = world.getChunk(neighborPos);
					ChunkPos chunkPos = chunk.getPos();
					if(world.isChunkLoaded(chunkPos.x, chunkPos.z)){
						BlockState neighborState = world.getBlockState(neighborPos);
						if(isLeafBlock(neighborState.getBlock())){
							LeafBreakingHandler.scheduledLeavesBreaking.add(new LeafBreakingSchedule(world, neighborPos, 4));
						}
					}
				}
			}
		}
	}
	
	@Inject(method = "updateNeighborsExcept", at = @At(value = "TAIL"))
	public void updateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo callbackInfo){
		World world = (World) (Object) this;
		if(world instanceof ServerWorld){
			EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
			directions.remove(direction);
			onUpdate((ServerWorld) world, pos, world.getBlockState(pos), directions);
		}
	}
}
