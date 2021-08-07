package fr.raksrinana.fallingtree.forge.tree;

import fr.raksrinana.fallingtree.forge.FallingTree;
import fr.raksrinana.fallingtree.forge.FallingTreeBlockBreakEvent;
import fr.raksrinana.fallingtree.forge.config.BreakMode;
import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.tree.breaking.ITreeBreakingHandler;
import fr.raksrinana.fallingtree.forge.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.raksrinana.fallingtree.forge.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.raksrinana.fallingtree.forge.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.forge.tree.builder.TreeTooBigException;
import fr.raksrinana.fallingtree.forge.utils.CacheSpeed;
import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils.canPlayerBreakTree;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BreakingHandler{
	private static final Map<UUID, CacheSpeed> speedCache = new ConcurrentHashMap<>();
	
	@SubscribeEvent
	public static void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed event){
		if(Config.COMMON.getTrees().isTreeBreaking() && !event.isCanceled()){
			if(Config.COMMON.getTrees().getBreakMode() == BreakMode.INSTANTANEOUS){
				if(isPlayerInRightState(event.getPlayer())){
					var cacheSpeed = speedCache.compute(event.getPlayer().getUUID(), (pos, speed) -> {
						if(isNull(speed) || !speed.isValid(event.getPos())){
							speed = getSpeed(event);
						}
						return speed;
					});
					if(nonNull(cacheSpeed)){
						event.setNewSpeed(cacheSpeed.getSpeed());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(Config.COMMON.getTrees().isTreeBreaking() && !event.isCanceled() && !event.getWorld().isClientSide()){
			if(event instanceof FallingTreeBlockBreakEvent){
				return;
			}
			var player = event.getPlayer();
			if(isPlayerInRightState(player) && event.getWorld() instanceof Level level){
				try{
					TreeBuilder.getTree(player, level, event.getPos()).ifPresent(tree -> {
						var breakMode = Config.COMMON.getTrees().getBreakMode();
						getBreakingHandler(breakMode).breakTree(event, tree);
					});
				}
				catch(TreeTooBigException e){
					FallingTreeUtils.notifyPlayer(player, new TranslatableComponent("chat.fallingtree.tree_too_big", Config.COMMON.getTrees().getMaxSize()));
				}
			}
		}
	}
	
	public static ITreeBreakingHandler getBreakingHandler(BreakMode breakMode){
		return switch(breakMode){
			case INSTANTANEOUS -> InstantaneousTreeBreakingHandler.getInstance();
			case SHIFT_DOWN -> ShiftDownTreeBreakingHandler.getInstance();
		};
	}
	
	private static boolean isPlayerInRightState(Player player){
		if(player.isCreative() && !Config.COMMON.isBreakInCreative()){
			return false;
		}
		if(Config.COMMON.isReverseSneaking() != player.isCrouching()){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	private static CacheSpeed getSpeed(PlayerEvent.BreakSpeed event){
		var speedMultiplicand = Config.COMMON.getTools().getSpeedMultiplicand();
		try{
			var player = event.getPlayer();
			return speedMultiplicand <= 0 ? null :
					TreeBuilder.getTree(player, player.getCommandSenderWorld(), event.getPos())
							.map(tree -> new CacheSpeed(event.getPos(), event.getNewSpeed() / ((float) speedMultiplicand * tree.getLogCount())))
							.orElse(null);
		}
		catch(TreeTooBigException e){
			return null;
		}
	}
}
