package fr.rakambda.fallingtree.common.wrapper;

import java.util.Optional;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jspecify.annotations.NonNull;

public interface IItemStack extends IWrapper{
	boolean isEmpty();
	
	boolean isDamageable();
	
	int getDamage();
	
	int getMaxDamage();
	
	void damage(int amount, @NonNull IPlayer player);
	
	@NonNull
	IItem getItem();
	
	boolean hasChopperEnchant();
	
	@NonNull
	Optional<BreakMode> getBreakModeFromEnchant();
	
	boolean canPerformAxeAction();
	
	default int getDurability(){
		if(isDamageable()){
			return getMaxDamage() - getDamage();
		}
		return Integer.MAX_VALUE;
	}
}
