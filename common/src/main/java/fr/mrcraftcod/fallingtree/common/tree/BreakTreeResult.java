package fr.mrcraftcod.fallingtree.common.tree;

import fr.mrcraftcod.fallingtree.common.config.enums.BreakMode;
import org.jetbrains.annotations.NotNull;

public record BreakTreeResult(
		boolean shouldCancel,
		@NotNull BreakMode breakMode
){
}
