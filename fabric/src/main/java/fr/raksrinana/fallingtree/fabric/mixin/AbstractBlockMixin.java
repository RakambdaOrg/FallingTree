package fr.raksrinana.fallingtree.fabric.mixin;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import fr.raksrinana.fallingtree.fabric.common.wrapper.BlockPosWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.PlayerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin{
	@Inject(method = "getDestroyProgress", at = @At(value = "TAIL"), cancellable = true)
	public void calcBlockBreakingDelta(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> callbackInfoReturnable){
		var wrappedPlayer = new PlayerWrapper(player);
		var wrappedPos = new BlockPosWrapper(pos);
		
		var result = FallingTree.getMod().getTreeHandler().getBreakSpeed(wrappedPlayer, wrappedPos, callbackInfoReturnable.getReturnValue());
		if(result.isEmpty()){
			return;
		}
		
		callbackInfoReturnable.setReturnValue(result.get());
	}
}
