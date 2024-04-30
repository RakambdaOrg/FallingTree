package fr.rakambda.fallingtree.neoforge.event;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.neoforge.FallingTree;
import fr.rakambda.fallingtree.neoforge.enchant.ChopperEnchantment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class FallingTreeEnchantments{
	private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, FallingTree.MOD_ID);
	
	public static DeferredHolder<Enchantment, ChopperEnchantment> CHOPPER_ENCHANTMENT;
	public static DeferredHolder<Enchantment, ChopperEnchantment> CHOPPER_INSTANTANEOUS_ENCHANTMENT;
	public static DeferredHolder<Enchantment, ChopperEnchantment> CHOPPER_FALL_BLOCK_ENCHANTMENT;
	public static DeferredHolder<Enchantment, ChopperEnchantment> CHOPPER_FALL_ITEM_ENCHANTMENT;
	public static DeferredHolder<Enchantment, ChopperEnchantment> CHOPPER_SHIFT_DOWN_ENCHANTMENT;
	
	public static void registerDefault(){
		CHOPPER_ENCHANTMENT = ENCHANTMENTS.register("chopper", () -> new ChopperEnchantment(FallingTree.getMod(), null));
	}
	
	public static void registerSpecific(){
		CHOPPER_INSTANTANEOUS_ENCHANTMENT = ENCHANTMENTS.register("chopper_instantaneous", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.INSTANTANEOUS));
		CHOPPER_FALL_BLOCK_ENCHANTMENT = ENCHANTMENTS.register("chopper_fall_block", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.FALL_BLOCK));
		CHOPPER_FALL_BLOCK_ENCHANTMENT = ENCHANTMENTS.register("chopper_fall_all_block", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.FALL_ALL_BLOCK));
		CHOPPER_FALL_ITEM_ENCHANTMENT = ENCHANTMENTS.register("chopper_fall_item", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.FALL_ITEM));
		CHOPPER_SHIFT_DOWN_ENCHANTMENT = ENCHANTMENTS.register("chopper_shift_down", () -> new ChopperEnchantment(FallingTree.getMod(), BreakMode.SHIFT_DOWN));
	}
	
	public static void commit(@NotNull IEventBus eventBus){
		ENCHANTMENTS.register(eventBus);
	}
}
