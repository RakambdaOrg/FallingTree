package fr.rakambda.fallingtree.common.config.enums;

import fr.rakambda.fallingtree.common.tree.TreePart;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import java.util.Comparator;

@RequiredArgsConstructor
@Getter
public enum BreakOrder{
	FURTHEST_FIRST(Comparator.comparingInt(TreePart::sequence).reversed()),
	CLOSEST_FIRST(Comparator.comparingInt(TreePart::sequence)),
	LOWEST_FIRST(Comparator.comparingInt(part -> part.blockPos().getY()));

	@NonNull
	private final Comparator<? super TreePart> comparator;
}
