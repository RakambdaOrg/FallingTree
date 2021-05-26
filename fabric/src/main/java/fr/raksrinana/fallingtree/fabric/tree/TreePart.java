package fr.raksrinana.fallingtree.fabric.tree;

import fr.raksrinana.fallingtree.fabric.utils.TreePartType;
import net.minecraft.core.BlockPos;

public record TreePart(
		BlockPos blockPos,
		TreePartType treePartType,
		int sequence
){
}
