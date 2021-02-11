package fr.raksrinana.fallingtree.mixin;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.tree.builder.TreeTooBigException;
import fr.raksrinana.fallingtree.utils.CacheSpeed;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import static fr.raksrinana.fallingtree.config.BreakMode.INSTANTANEOUS;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isPlayerInRightState;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin{
	private static final Map<UUID, CacheSpeed> speedCache = new HashMap<>();
	
	@Inject(method = "calcBlockBreakingDelta", at = @At(value = "TAIL"), cancellable = true)
	public void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> callbackInfoReturnable){
		if(FallingTree.config.getTreesConfiguration().isTreeBreaking() && FallingTree.config.getTreesConfiguration().getBreakMode() == INSTANTANEOUS){
			if(isPlayerInRightState(player)){
				CacheSpeed cacheSpeed = speedCache.compute(player.getUuid(), (uuid, speed) -> {
					if(Objects.isNull(speed) || !speed.isValid(pos)){
						speed = getSpeed(player, pos, callbackInfoReturnable.getReturnValue());
					}
					return speed;
				});
				if(Objects.nonNull(cacheSpeed)){
					callbackInfoReturnable.setReturnValue(cacheSpeed.getSpeed());
				}
			}
		}
	}
	
	private static CacheSpeed getSpeed(PlayerEntity player, BlockPos pos, float originalSpeed){
		double speedMultiplicand = FallingTree.config.getToolsConfiguration().getSpeedMultiplicand();
		try{
			return speedMultiplicand <= 0 ? null :
					TreeBuilder.getTree(player.getEntityWorld(), pos)
							.map(tree -> new CacheSpeed(pos, originalSpeed / ((float) speedMultiplicand * tree.getLogCount())))
							.orElse(null);
		}
		catch(TreeTooBigException e){
			return null;
		}
	}
}
