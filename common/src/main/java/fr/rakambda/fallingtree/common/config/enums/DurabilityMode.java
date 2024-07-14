package fr.rakambda.fallingtree.common.config.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum DurabilityMode{
	ABORT(true, (breakCount, breakableCount) -> breakCount <= breakableCount ? -1 : breakCount),
	SAVE(true, (breakCount, breakableCount) -> breakCount <= breakableCount ? breakCount - 1 : breakCount),
	NORMAL(true, (breakCount, breakableCount) -> breakCount),
	BYPASS(false, (breakCount, breakableCount) -> breakableCount);
	
	private final boolean allowAbort;
	private final BiFunction<Integer, Integer, Integer> postProcessor;
	
	public int postProcess(int breakCount, int breakableCount){
		return postProcessor.apply(breakCount, breakableCount);
	}
}
