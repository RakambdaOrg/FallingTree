package fr.raksrinana.fallingtree.fabric;

import fr.raksrinana.fallingtree.fabric.config.Configuration;
import fr.raksrinana.fallingtree.fabric.leaves.LeafBreakingHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class FallingTree implements ModInitializer{
	public static Configuration config;
	
	@Override
	public void onInitialize(){
		config = Configuration.register();
		
		ServerTickEvents.END_SERVER_TICK.register(new LeafBreakingHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BlockBreakHandler());
	}
}
