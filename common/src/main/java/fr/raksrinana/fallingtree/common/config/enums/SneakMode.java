package fr.raksrinana.fallingtree.common.config.enums;

import lombok.RequiredArgsConstructor;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum SneakMode{
	SNEAK_DISABLE(val -> !val),
	SNEAK_ENABLE(val -> val),
	IGNORE(val -> true);
	
	private final Predicate<Boolean> sneakTester;
	
	public boolean test(boolean sneak){
		return sneakTester.test(sneak);
	}
}
