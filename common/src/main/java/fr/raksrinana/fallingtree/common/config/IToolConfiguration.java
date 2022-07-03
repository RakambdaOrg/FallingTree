package fr.raksrinana.fallingtree.common.config;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.DamageRounding;
import fr.raksrinana.fallingtree.common.wrapper.IItem;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface IToolConfiguration{
	@NotNull
	Collection<IItem> getDeniedItems(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IItem> getAllowedItems(@NotNull FallingTreeCommon<?> common);
	
	boolean isPreserve();
	
	boolean isIgnoreTools();
	
	double getDamageMultiplicand();
	
	DamageRounding getDamageRounding();
	
	double getSpeedMultiplicand();
}
