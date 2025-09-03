package fr.rakambda.fallingtree.common.enchant;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public interface IFallingTreeEnchantment{
	@NonNull
	Optional<BreakMode> getBreakMode();
}
