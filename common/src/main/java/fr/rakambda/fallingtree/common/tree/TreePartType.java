package fr.rakambda.fallingtree.common.tree;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TreePartType{
	LEAF(false, true, false),
	LEAF_NEED_BREAK(true, true, true),
	LOG(true, false, true),
	NETHER_WART(true, false, true),
	MANGROVE_ROOTS(true, false, true),
	OTHER(false, false, false);
	
	@Getter
	private static final TreePartType[] values = values();
	
	private final boolean breakable;
	private final boolean edge;
	private final boolean includeInTree;
}
