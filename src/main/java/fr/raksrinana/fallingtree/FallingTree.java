package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.Configuration;
import fr.raksrinana.fallingtree.leaves.LeafBreakingHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class FallingTree implements ModInitializer{
	public static Configuration config;
	
	@Override
	public void onInitialize(){
		config = Configuration.register();
		
		ServerTickEvents.END_SERVER_TICK.register(new LeafBreakingHandler());
	}
}
