package fr.rakambda.fallingtree.common.config.enums;

import fr.rakambda.fallingtree.common.tree.TreePart;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;

@RequiredArgsConstructor
@Getter
public enum BreakOrder{
	FURTHEST_FIRST(Comparator.comparingInt(TreePart::sequence).reversed()),
	CLOSEST_FIRST(Comparator.comparingInt(TreePart::sequence));
	
	@NotNull
	private final Comparator<? super TreePart> comparator;
}
