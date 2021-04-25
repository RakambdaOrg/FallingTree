package fr.raksrinana.fallingtree.fabric.mixin;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.leaves.LeafBreakingHandler;
import fr.raksrinana.fallingtree.fabric.leaves.LeafBreakingSchedule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.EnumSet;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.isLeafBlock;
import static net.minecraft.world.level.block.Blocks.AIR;

@Mixin(Level.class)
public abstract class WorldMixin{
	@Inject(method = "updateNeighborsAt", at = @At(value = "TAIL"))
	public void updateNeighborsAlways(BlockPos pos, Block block, CallbackInfo callbackInfo){
		Level world = (Level) (Object) this;
		if(world instanceof ServerLevel){
			onUpdate((ServerLevel) world, pos, world.getBlockState(pos), EnumSet.allOf(Direction.class));
		}
	}
	
	private static void onUpdate(ServerLevel world, BlockPos eventPos, BlockState eventState, EnumSet<Direction> notifiedSides){
		if(FallingTree.config.getTreesConfiguration().isLeavesBreaking()){
			Block eventBlock = eventState.getBlock();
			if(eventBlock.is(AIR)){
				for(Direction facing : notifiedSides){
					BlockPos neighborPos = eventPos.relative(facing);
					ChunkAccess chunk = world.getChunk(neighborPos);
					ChunkPos chunkPos = chunk.getPos();
					if(world.hasChunk(chunkPos.x, chunkPos.z)){
						BlockState neighborState = world.getBlockState(neighborPos);
						if(isLeafBlock(neighborState.getBlock())){
							LeafBreakingHandler.scheduledLeavesBreaking.add(new LeafBreakingSchedule(world, neighborPos, 4));
						}
					}
				}
			}
		}
	}
	
	@Inject(method = "updateNeighborsAtExceptFromFacing", at = @At(value = "TAIL"))
	public void updateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo callbackInfo){
		Level world = (Level) (Object) this;
		if(world instanceof ServerLevel){
			EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
			directions.remove(direction);
			onUpdate((ServerLevel) world, pos, world.getBlockState(pos), directions);
		}
	}
}
