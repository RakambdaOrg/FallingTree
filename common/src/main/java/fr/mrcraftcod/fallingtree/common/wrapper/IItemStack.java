package fr.mrcraftcod.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;

public interface IItemStack extends IWrapper{
	boolean isDamageable();
	
	int getDamage();
	
	int getMaxDamage();
	
	void damage(int amount, @NotNull IPlayer player);
	
	@NotNull
	IItem getItem();
	
	int getChopperEnchantLevel();
}
