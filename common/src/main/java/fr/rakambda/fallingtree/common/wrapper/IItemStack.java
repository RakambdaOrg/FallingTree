package fr.rakambda.fallingtree.common.wrapper;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

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
	
	boolean isAxe();
	
	default int getDurability(){
		if(isDamageable()){
			return getMaxDamage() - getDamage();
		}
		return Integer.MAX_VALUE;
	}
}
