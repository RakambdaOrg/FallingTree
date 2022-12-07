package fr.rakambda.fallingtree.common.config;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.DamageRounding;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface IToolConfiguration{
	@NotNull
	Collection<IItem> getDeniedItems(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IItem> getAllowedItems(@NotNull FallingTreeCommon<?> common);
	
	boolean isPreserve();
	
	boolean isIgnoreTools();
	
	boolean isForceToolUsage();
	
	double getDamageMultiplicand();
	
	DamageRounding getDamageRounding();
	
	double getSpeedMultiplicand();
}
