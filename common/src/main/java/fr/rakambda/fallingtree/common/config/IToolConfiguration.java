package fr.rakambda.fallingtree.common.config;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.DamageRounding;
import fr.rakambda.fallingtree.common.config.enums.DurabilityMode;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import org.jspecify.annotations.NonNull;
import java.util.Collection;

public interface IToolConfiguration{
	@NonNull
	Collection<IItem> getDeniedItems(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IItem> getAllowedItems(@NonNull FallingTreeCommon<?> common);
	
	DurabilityMode getDurabilityMode();
	
	boolean isIgnoreTools();
	
	boolean isForceToolUsage();
	
	double getDamageMultiplicand();
	
	DamageRounding getDamageRounding();
	
	double getSpeedMultiplicand();
}
