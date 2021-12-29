package fr.raksrinana.fallingtree.fabric;

import fr.raksrinana.fallingtree.fabric.enchant.ChopperEnchantment;
import fr.raksrinana.fallingtree.fabric.leaves.LeafBreakingHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FallingTree implements ModInitializer{
	public static final String MOD_ID = "fallingtree";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	
	public static Enchantment CHOPPER_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new ResourceLocation(MOD_ID, "chopper"),
			new ChopperEnchantment()
	);
	
	@Override
	public void onInitialize(){
		ServerTickEvents.END_SERVER_TICK.register(new LeafBreakingHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BlockBreakHandler());
	}
}
