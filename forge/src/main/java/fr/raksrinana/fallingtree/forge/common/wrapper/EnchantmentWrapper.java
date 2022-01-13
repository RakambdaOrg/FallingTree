package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.IEnchantment;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class EnchantmentWrapper implements IEnchantment{
	@NotNull
	private final RegistryObject<Enchantment> raw;
	
	@Override
	@NotNull
	public Enchantment getRaw(){
		return raw.get();
	}
}
