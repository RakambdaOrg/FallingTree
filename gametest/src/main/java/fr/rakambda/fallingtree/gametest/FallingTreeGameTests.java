package fr.rakambda.fallingtree.gametest;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.gametest.scenario.BreakModeScenarios;
import fr.rakambda.fallingtree.gametest.scenario.DetectionScenarios;
import fr.rakambda.fallingtree.gametest.scenario.DurabilityScenarios;
import fr.rakambda.fallingtree.gametest.scenario.LeafScenarios;
import fr.rakambda.fallingtree.gametest.scenario.LootScenarios;
import fr.rakambda.fallingtree.gametest.scenario.MetaScenarios;
import fr.rakambda.fallingtree.gametest.scenario.PlayerScenarios;
import fr.rakambda.fallingtree.gametest.scenario.SpecialTreeScenarios;
import fr.rakambda.fallingtree.gametest.scenario.ToolScenarios;
import fr.rakambda.fallingtree.gametest.scenario.TreeBreakingScenarios;
import fr.rakambda.fallingtree.gametest.scenario.TreeShapeScenarios;
import net.minecraft.gametest.framework.GameTestHelper;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Test functions shared by every loader.
 * <p>
 * Each loader only registers these functions in {@link net.minecraft.core.registries.Registries#TEST_FUNCTION} under {@link #NAMESPACE}. The tests themselves (structure,
 * environment, timeout) are data generated from the {@link GameTestCase} annotations, see {@code gametest/gametest.gradle}.
 */
public final class FallingTreeGameTests{
	public static final String NAMESPACE = "fallingtree_gametest";

	/**
	 * Classes holding {@link GameTestCase} methods.
	 */
	public static final List<Class<?>> SCENARIO_CLASSES = List.of(
			MetaScenarios.class,
			TreeBreakingScenarios.class,
			ToolScenarios.class,
			DurabilityScenarios.class,
			PlayerScenarios.class,
			BreakModeScenarios.class,
			DetectionScenarios.class,
			TreeShapeScenarios.class,
			LeafScenarios.class,
			LootScenarios.class,
			SpecialTreeScenarios.class
	);

	private FallingTreeGameTests(){
	}

	@NonNull
	public static Map<String, Consumer<GameTestHelper>> testFunctions(@NonNull Supplier<FallingTreeCommon<?>> mod){
		var functions = new TreeMap<String, Consumer<GameTestHelper>>();
		for(var scenarioClass : SCENARIO_CLASSES){
			Arrays.stream(scenarioClass.getDeclaredMethods())
					.filter(method -> method.isAnnotationPresent(GameTestCase.class))
					.forEach(method -> {
						var name = testName(method);
						if(functions.put(name, helper -> invoke(method, helper, mod.get())) != null){
							throw new IllegalStateException("Duplicate game test name " + name);
						}
					});
		}
		return functions;
	}

	/**
	 * Must give the same name as {@code gametest/gametest.gradle}.
	 */
	@NonNull
	public static String testName(@NonNull Method method){
		return method.getName().replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
	}

	private static void invoke(@NonNull Method method, @NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		if(!Modifier.isStatic(method.getModifiers())){
			throw new IllegalStateException("Game test " + method + " must be static");
		}
		try{
			method.invoke(null, helper, mod);
		}
		catch(InvocationTargetException e){
			if(e.getCause() instanceof RuntimeException runtimeException){
				throw runtimeException;
			}
			throw new IllegalStateException(e.getCause());
		}
		catch(IllegalAccessException e){
			throw new IllegalStateException("Game test " + method + " must be public", e);
		}
	}
}
