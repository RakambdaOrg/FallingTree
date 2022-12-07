package fr.rakambda.fallingtree.common.enchant;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface IFallingTreeEnchantment{
	@NotNull
	Optional<BreakMode> getBreakMode();
}
