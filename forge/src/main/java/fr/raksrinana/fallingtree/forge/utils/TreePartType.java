package fr.raksrinana.fallingtree.forge.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TreePartType{
	LOG(true),
	NETHER_WART(true),
	LEAF_NEED_BREAK(true),
	LEAF(false),
	OTHER(false);
	private final boolean breakable;
}
