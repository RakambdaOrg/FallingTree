package fr.rakambda.fallingtree.forge.event;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.forge.enchant.ChopperEnchantment;
import fr.rakambda.fallingtree.forge.FallingTree;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class FallingTreeEnchantments{
	private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FallingTree.MOD_ID);
	
	public static RegistryObject<Enchantment> CHOPPER_ENCHANTMENT;
	public static RegistryObject<Enchantment> CHOPPER_INSTANTANEOUS_ENCHANTMENT;
	public static RegistryObject<Enchantment> CHOPPER_SHIFT_DOWN_ENCHANTMENT;
	
	public static void registerDefault(){
		CHOPPER_ENCHANTMENT = ENCHANTMENTS.register("chopper", () -> new ChopperEnchantment(FallingTree.getMod(), null));
	}
	
	public static void registerSpecific(){
		CHOPPER_INSTANTANEOUS_ENCHANTMENT = ENCHANTMENTS.register("chopper_instantaneous", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.INSTANTANEOUS));
		CHOPPER_SHIFT_DOWN_ENCHANTMENT = ENCHANTMENTS.register("chopper_shift_down", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.SHIFT_DOWN));
	}
	
	public static void commit(@NotNull IEventBus eventBus){
		ENCHANTMENTS.register(eventBus);
	}
}
