package fr.raksrinana.fallingtree.fabric;

import fr.raksrinana.fallingtree.fabric.common.FallingTreeCommonsImpl;
import fr.raksrinana.fallingtree.fabric.enchant.ChopperEnchantment;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class FallingTree implements ModInitializer{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static final FallingTreeCommonsImpl mod = new FallingTreeCommonsImpl();
	
	public static final Enchantment CHOPPER_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new ResourceLocation(MOD_ID, "chopper"),
			new ChopperEnchantment()
	);
	
	@Override
	public void onInitialize(){
		mod.register();
	}
}
