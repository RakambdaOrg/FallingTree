package fr.raksrinana.fallingtree.forge.event;

import fr.raksrinana.fallingtree.forge.FallingTree;
import fr.raksrinana.fallingtree.forge.enchant.ChopperEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class FallingTreeEnchantments{
	private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FallingTree.MOD_ID);
	
	public static RegistryObject<Enchantment> CHOPPER_ENCHANTMENT = ENCHANTMENTS.register("chopper", ChopperEnchantment::new);
	
	public static void register(@NotNull IEventBus eventBus){
		ENCHANTMENTS.register(eventBus);
	}
}
