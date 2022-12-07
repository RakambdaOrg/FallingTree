package fr.rakambda.fallingtree.common.config;

import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import fr.rakambda.fallingtree.common.config.enums.SneakMode;
import org.jetbrains.annotations.NotNull;

public interface IConfiguration{
	@NotNull
	ITreeConfiguration getTrees();
	
	@NotNull
	IToolConfiguration getTools();
	
	@NotNull
	IPlayerConfiguration getPlayer();
	
	@NotNull
	IEnchantmentConfiguration getEnchantment();
	
	@NotNull
	SneakMode getSneakMode();
	
	boolean isBreakInCreative();
	
	boolean isLootInCreative();
	
	@NotNull
	NotificationMode getNotificationMode();
}
