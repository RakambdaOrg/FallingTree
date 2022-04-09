package fr.raksrinana.fallingtree.fabric.mixin;

import fr.raksrinana.fallingtree.common.wrapper.DirectionCompat;
import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.common.wrapper.BlockPosWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.BlockStateWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ServerLevelWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.EnumSet;
import java.util.stream.Collectors;

@Mixin(ServerLevel.class)
public abstract class LevelMixin{
	@Inject(method = "updateNeighborsAt", at = @At(value = "TAIL"))
	public void updateNeighborsAlways(BlockPos pos, Block block, CallbackInfo callbackInfo){
		//noinspection ConstantConditions
		var serverLevel = (ServerLevel) (Object) this;
		FallingTree.getMod().getLeafBreakingHandler().onBlockUpdate(
				new ServerLevelWrapper(serverLevel),
				new BlockPosWrapper(pos),
				new BlockStateWrapper(serverLevel.getBlockState(pos)),
				EnumSet.allOf(DirectionCompat.class));
	}
	
	@Inject(method = "updateNeighborsAtExceptFromFacing", at = @At(value = "TAIL"))
	public void updateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo callbackInfo){
		//noinspection ConstantConditions
		var serverLevel = (ServerLevel) (Object) this;
		var directions = EnumSet.allOf(Direction.class);
		directions.remove(direction);
		
		FallingTree.getMod().getLeafBreakingHandler().onBlockUpdate(
				new ServerLevelWrapper(serverLevel),
				new BlockPosWrapper(pos),
				new BlockStateWrapper(serverLevel.getBlockState(pos)),
				directions.stream().map(FallingTree.getMod()::asDirectionCompat).collect(Collectors.toSet()));
	}
}
