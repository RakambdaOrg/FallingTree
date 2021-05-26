package fr.raksrinana.fallingtree.fabric.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BreakMode{
	INSTANTANEOUS(true),
	SHIFT_DOWN(false);
	
	private final boolean checkLeavesAround;
}
