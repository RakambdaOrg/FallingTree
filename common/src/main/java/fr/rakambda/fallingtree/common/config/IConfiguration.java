package fr.rakambda.fallingtree.common.config;

import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import fr.rakambda.fallingtree.common.config.enums.SneakMode;
import org.jspecify.annotations.NonNull;

public interface IConfiguration{
	@NonNull
	ITreeConfiguration getTrees();
	
	@NonNull
	IToolConfiguration getTools();
	
	@NonNull
	IPlayerConfiguration getPlayer();
	
	@NonNull
	IEnchantmentConfiguration getEnchantment();
	
	@NonNull
	SneakMode getSneakMode();
	
	boolean isBreakInCreative();
	
	boolean isLootInCreative();
	
	@NonNull
	NotificationMode getNotificationMode();
}
