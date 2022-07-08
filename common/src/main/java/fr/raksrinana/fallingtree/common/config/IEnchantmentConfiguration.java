package fr.raksrinana.fallingtree.common.config;

public interface IEnchantmentConfiguration{
	boolean isRegisterEnchant();
	
	boolean isRegisterSpecificEnchant();
	
	boolean isHideEnchant();
	
	default boolean isAtLeastOneEnchantRegistered(){
		return isRegisterEnchant() || isRegisterSpecificEnchant();
	}
}
