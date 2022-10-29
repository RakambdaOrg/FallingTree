package fr.rakambda.fallingtree.common.wrapper;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface IEnchantment extends IWrapper{
	@NotNull
	Optional<BreakMode> getBreakMode();
}
