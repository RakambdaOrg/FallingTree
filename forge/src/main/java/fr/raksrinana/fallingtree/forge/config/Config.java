package fr.raksrinana.fallingtree.forge.config;

import fr.raksrinana.fallingtree.forge.FallingTree;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config{
	public static final CommonConfig COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static{
		var commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = commonPair.getLeft();
		COMMON_SPEC = commonPair.getRight();
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(ModConfigEvent configEvent){
		if(configEvent.getConfig().getSpec() == COMMON_SPEC){
			ConfigCache.getInstance().invalidate();
		}
	}
}
