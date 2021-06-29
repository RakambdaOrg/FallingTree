package fr.raksrinana.fallingtree.forge.tree;

import fr.raksrinana.fallingtree.forge.utils.TreePartType;
import net.minecraft.util.math.BlockPos;

public record TreePart(
		BlockPos blockPos,
		TreePartType treePartType,
		int sequence
){
}
