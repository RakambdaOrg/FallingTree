package fr.raksrinana.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IItemStack extends IWrapper{
	boolean isDamageable();
	
	int getDamage();
	
	int getMaxDamage();
	
	void damage(int amount, @NotNull IPlayer player);
	
	@NotNull
	IItem getItem();
	
	int getEnchantLevel(@Nullable IEnchantment enchantment);
}
