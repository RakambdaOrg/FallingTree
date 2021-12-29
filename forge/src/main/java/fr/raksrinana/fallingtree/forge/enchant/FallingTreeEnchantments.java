package fr.raksrinana.fallingtree.forge.enchant;

import fr.raksrinana.fallingtree.forge.FallingTree;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(FallingTree.MOD_ID)
public class FallingTreeEnchantments{
	public static final Enchantment chopper = null;
	
	@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
	public static class RegistrationHandler
	{
		@SubscribeEvent
		public static void onEvent(RegistryEvent.Register<Enchantment> event)
		{
			var registry = event.getRegistry();
			registry.register(new ChopperEnchantment());
		}
	}
}
