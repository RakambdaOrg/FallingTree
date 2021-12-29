package fr.raksrinana.fallingtree.fabric.mixin;

import fr.raksrinana.fallingtree.fabric.config.Configuration;
import fr.raksrinana.fallingtree.fabric.leaves.LeafBreakingHandler;
import fr.raksrinana.fallingtree.fabric.leaves.LeafBreakingSchedule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.EnumSet;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.isLeafBlock;
import static net.minecraft.world.level.block.Blocks.AIR;

@Mixin(Level.class)
public abstract class LevelMixin{
	@Inject(method = "updateNeighborsAt", at = @At(value = "TAIL"))
	public void updateNeighborsAlways(BlockPos pos, Block block, CallbackInfo callbackInfo){
		var level = (Level) (Object) this;
		if(level instanceof ServerLevel serverLevel){
			onUpdate(serverLevel, pos, level.getBlockState(pos), EnumSet.allOf(Direction.class));
		}
	}
	
	private static void onUpdate(ServerLevel level, BlockPos eventPos, BlockState eventState, EnumSet<Direction> directions){
		if(Configuration.getInstance().getTrees().isLeavesBreaking()){
			var eventBlock = eventState.getBlock();
			if(eventBlock.equals(AIR)){
				for(var direction : directions){
					var neighborPos = eventPos.relative(direction);
					var chunk = level.getChunk(neighborPos);
					var chunkPos = chunk.getPos();
					if(level.hasChunk(chunkPos.x, chunkPos.z)){
						var neighborState = level.getBlockState(neighborPos);
						if(isLeafBlock(neighborState.getBlock())){
							LeafBreakingHandler.scheduledLeavesBreaking.add(new LeafBreakingSchedule(level, neighborPos, 4));
						}
					}
				}
			}
		}
	}
	
	@Inject(method = "updateNeighborsAtExceptFromFacing", at = @At(value = "TAIL"))
	public void updateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo callbackInfo){
		var level = (Level) (Object) this;
		if(level instanceof ServerLevel serverLevel){
			var directions = EnumSet.allOf(Direction.class);
			directions.remove(direction);
			onUpdate(serverLevel, pos, level.getBlockState(pos), directions);
		}
	}
}
