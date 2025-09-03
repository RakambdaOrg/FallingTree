package fr.rakambda.fallingtree.common.wrapper;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public interface IEnchantment extends IWrapper{
	@NonNull
	Optional<BreakMode> getBreakMode();
}
