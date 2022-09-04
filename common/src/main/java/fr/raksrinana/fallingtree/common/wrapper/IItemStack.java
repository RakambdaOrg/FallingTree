package fr.raksrinana.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;

public interface IItemStack extends IWrapper{
	boolean isEmpty();
	
	boolean isDamageable();
	
	int getDamage();
	
	int getMaxDamage();
	
	void damage(int amount, @NotNull IPlayer player);
	
	@NotNull
	IItem getItem();
	
	int getEnchantLevel(@Nullable IEnchantment enchantment);
	
	boolean hasOneOfEnchantAtLeast(@NotNull Collection<IEnchantment> enchantments, int minLevel);
	
	@NotNull
	Optional<IEnchantment> getAnyEnchant(@NotNull Collection<IEnchantment> enchantments);
	
	boolean canPerformAxeAction();
}
