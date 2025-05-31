package fr.rakambda.fallingtree.common.config.enums;

import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum DetectionMode{
	ABOVE_CUT(tree -> tree.getTopMostLog().stream()),
	ABOVE_Y(tree -> tree.getTopMostLog().stream()),
	BELOW_CUT(tree -> tree.getBottomMostLog().stream()),
	BELOW_Y(tree -> tree.getBottomMostLog().stream()),
	WHOLE_DOWNWARDS(tree -> tree.getBottomMostLog().stream()),
	WHOLE_TREE(tree -> tree.getTopMostLog().stream());
	
	private final Function<Tree, Stream<IBlockPos>> leafAroundPosProvider;
}
