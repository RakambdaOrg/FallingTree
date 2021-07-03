package fr.raksrinana.fallingtree.fabric.mixin;

import fr.raksrinana.fallingtree.fabric.config.BreakMode;
import fr.raksrinana.fallingtree.fabric.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.fabric.tree.builder.TreeTooBigException;
import fr.raksrinana.fallingtree.fabric.utils.CacheSpeed;
import fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin{
	private static final Map<UUID, CacheSpeed> speedCache = new HashMap<>();
	
	@Inject(method = "getDestroyProgress", at = @At(value = "TAIL"), cancellable = true)
	public void calcBlockBreakingDelta(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> callbackInfoReturnable){
		if(config.getTrees().isTreeBreaking() && config.getTrees().getBreakMode() == BreakMode.INSTANTANEOUS){
			if(FallingTreeUtils.isPlayerInRightState(player)){
				var cacheSpeed = speedCache.compute(player.getUUID(), (uuid, speed) -> {
					if(isNull(speed) || !speed.isValid(pos)){
						speed = getSpeed(player, pos, callbackInfoReturnable.getReturnValue());
					}
					return speed;
				});
				if(nonNull(cacheSpeed)){
					callbackInfoReturnable.setReturnValue(cacheSpeed.getSpeed());
				}
			}
		}
	}
	
	private static CacheSpeed getSpeed(Player player, BlockPos pos, float originalSpeed){
		var speedMultiplicand = config.getTools().getSpeedMultiplicand();
		try{
			return speedMultiplicand <= 0 ? null :
					TreeBuilder.getTree(player, player.getCommandSenderWorld(), pos)
							.map(tree -> new CacheSpeed(pos, originalSpeed / ((float) speedMultiplicand * tree.getLogCount())))
							.orElse(null);
		}
		catch(TreeTooBigException e){
			return null;
		}
	}
}
