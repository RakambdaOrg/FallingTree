package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jetbrains.annotations.NotNull;

public record BreakTreeResult(
		boolean shouldCancel,
		@NotNull BreakMode breakMode
){
}
