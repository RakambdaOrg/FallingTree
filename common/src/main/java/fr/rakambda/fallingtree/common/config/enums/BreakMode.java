package fr.rakambda.fallingtree.common.config.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BreakMode{
	INSTANTANEOUS(true, true),
	SHIFT_DOWN(false, false);
	
	@Getter
	private final static BreakMode[] values = values();
	
	private final boolean checkLeavesAround;
	private final boolean applySpeedMultiplier;
}
