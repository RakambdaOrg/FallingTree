package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TreePart(
		@NonNull IBlockPos blockPos,
		@NonNull TreePartType treePartType,
		int sequence,
		@NonNull IBlockState blockState,
		@Nullable IBlockEntity blockEntity
){
}
