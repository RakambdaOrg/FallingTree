package fr.rakambda.fallingtree.gametest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@code public static void name(GameTestHelper helper, FallingTreeCommon<?> mod)} method of a class listed in {@link FallingTreeGameTests#SCENARIO_CLASSES} as a
 * GameTest.
 * <p>
 * The test is named after the method in snake_case. Its {@code test_instance} and {@code test_environment} data are generated from this annotation by
 * {@code gametest/gametest.gradle}, which reads the sources: keep the annotation directly above the method declaration, and only use literal values.
 * <p>
 * Every test gets its own environment, so tests never run in the same batch: each one can freely change the (global) mod configuration through
 * {@link TestConfiguration}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GameTestCase{
	/**
	 * Keep in sync with the default in {@code gametest/gametest.gradle}.
	 */
	int DEFAULT_MAX_TICKS = 40;

	int maxTicks() default DEFAULT_MAX_TICKS;
}
