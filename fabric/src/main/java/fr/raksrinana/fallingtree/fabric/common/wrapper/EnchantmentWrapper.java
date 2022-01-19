package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IEnchantment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class EnchantmentWrapper implements IEnchantment{
	@NotNull
	@Getter
	private final Enchantment raw;
}
