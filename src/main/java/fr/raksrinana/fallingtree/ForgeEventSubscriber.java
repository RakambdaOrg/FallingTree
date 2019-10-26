package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.Config;
import fr.raksrinana.fallingtree.tree.TreeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventSubscriber{
	public static final Logger LOGGER = LogManager.getLogger(FallingTree.MOD_NAME);
	
	@SubscribeEvent
	public static void onBlockBreakEvent(@Nonnull BlockEvent.BreakEvent event){
		if(!event.isCanceled() && !event.getWorld().isRemote()){
			if(!event.getPlayer().abilities.isCreativeMode && !event.getPlayer().isSneaking() && TreeHandler.canPlayerBreakTree(event.getPlayer())){
				TreeHandler.getTree(event.getWorld(), event.getPos()).ifPresent(tree -> {
					if(Config.SERVER.maxTreeSize.get() >= tree.getLogCount()){
						ItemStack tool = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
						if(Config.SERVER.ignoreDurabilityLoss.get() || !tool.isDamageable() || tree.getLogCount() <= (tool.getMaxDamage() - tool.getDamage())){
							TreeHandler.destroy(tree, event.getPlayer(), tool);
						}
					}
				});
			}
		}
	}
}
