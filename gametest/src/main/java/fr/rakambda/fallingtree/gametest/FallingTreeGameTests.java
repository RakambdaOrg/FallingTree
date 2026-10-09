package fr.rakambda.fallingtree.gametest;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import net.minecraft.gametest.framework.GameTestHelper;
import org.jspecify.annotations.NonNull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Test functions shared by every loader.
 * <p>
 * Each loader only registers these functions in {@link net.minecraft.core.registries.Registries#TEST_FUNCTION} under {@link #NAMESPACE}. The tests themselves (structure,
 * environment, timeout) are data driven and live in the shared datapack: {@code data/fallingtree_gametest/test_instance/<name>.json}.
 */
public final class FallingTreeGameTests{
	public static final String NAMESPACE = "fallingtree_gametest";

	private FallingTreeGameTests(){
	}

	@NonNull
	public static Map<String, Consumer<GameTestHelper>> testFunctions(@NonNull Supplier<FallingTreeCommon<?>> mod){
		var functions = new LinkedHashMap<String, Consumer<GameTestHelper>>();
		add(functions, mod, "basic_chop", TreeBreakingScenarios::basicChop);
		add(functions, mod, "sneaking_disables_chop", TreeBreakingScenarios::sneakingDisablesChop);
		add(functions, mod, "non_axe_does_not_chop", TreeBreakingScenarios::nonAxeDoesNotChop);
		add(functions, mod, "sneak_enable_chops_when_sneaking", TreeBreakingScenarios::sneakEnableChopsWhenSneaking);
		add(functions, mod, "sneak_enable_ignores_standing", TreeBreakingScenarios::sneakEnableIgnoresStanding);
		add(functions, mod, "damage_multiplicand_zero_costs_one", TreeBreakingScenarios::damageMultiplicandZeroCostsOne);
		add(functions, mod, "fall_item_chop", TreeBreakingScenarios::fallItemChop);
		return functions;
	}

	private static void add(@NonNull Map<String, Consumer<GameTestHelper>> functions, @NonNull Supplier<FallingTreeCommon<?>> mod, @NonNull String name, @NonNull BiConsumer<GameTestHelper, FallingTreeCommon<?>> scenario){
		functions.put(name, helper -> scenario.accept(helper, mod.get()));
	}
}
