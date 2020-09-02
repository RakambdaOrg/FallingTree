package fr.raksrinana.fallingtree.mixin;

import fr.raksrinana.fallingtree.BlockBreakHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin{
	private static final BlockBreakHandler blockBreakHandler = new BlockBreakHandler();
	@Shadow
	public ServerWorld world;
	@Shadow
	public ServerPlayerEntity player;
	
	@Inject(method = "tryBreakBlock", at = @At(value = "INVOKE"), cancellable = true)
	private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> callback){
		boolean result = blockBreakHandler.beforeBlockBreak(world, player, pos);
		if(!result){
			callback.setReturnValue(false);
		}
	}
}
