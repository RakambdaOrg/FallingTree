package fr.raksrinana.fallingtree.common.config.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BreakMode{
	INSTANTANEOUS(true),
	SHIFT_DOWN(false);
	
	private final boolean checkLeavesAround;
}
