package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import org.jetbrains.annotations.NotNull;

public record TreePart(
		@NotNull IBlockPos blockPos,
		@NotNull TreePartType treePartType,
		int sequence
){
}
