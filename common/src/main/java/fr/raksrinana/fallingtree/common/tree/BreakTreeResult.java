package fr.raksrinana.fallingtree.common.tree;

import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import org.jetbrains.annotations.NotNull;

public record BreakTreeResult(
		boolean shouldCancel,
		@NotNull BreakMode breakMode
){
}
