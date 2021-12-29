package fr.raksrinana.fallingtree.forge.enchant;

import fr.raksrinana.fallingtree.forge.FallingTree;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FallingTreeEnchantments{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FallingTree.MOD_ID);
	
	public static RegistryObject<Enchantment> CHOPPER_ENCHANTMENT = ENCHANTMENTS.register("chopper", ChopperEnchantment::new);
	
	public static void register(IEventBus eventBus) {
		ENCHANTMENTS.register(eventBus);
	}
}
