package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.gametest.FallingTreeGameTests;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;

/**
 * Checks on the test setup itself.
 */
public final class MetaScenarios{
	private MetaScenarios(){
	}

	/**
	 * Every registered test function has a generated test instance and vice versa. Fails if {@code gametest/gametest.gradle} could not see a {@link GameTestCase} (which would
	 * otherwise silently never run).
	 */
	@GameTestCase
	public static void everyTestHasData(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var instances = helper.getLevel().registryAccess().lookupOrThrow(Registries.TEST_INSTANCE);
		var missingInstances = new ArrayList<String>();
		BuiltInRegistries.TEST_FUNCTION.keySet().stream()
				.filter(id -> id.getNamespace().equals(FallingTreeGameTests.NAMESPACE))
				.filter(id -> instances.get(ResourceKey.create(Registries.TEST_INSTANCE, id)).isEmpty())
				.forEach(id -> missingInstances.add(id.toString()));
		helper.assertTrue(missingInstances.isEmpty(), "Test functions without generated test instance: " + missingInstances);

		var missingFunctions = new ArrayList<String>();
		instances.listElementIds()
				.map(ResourceKey::identifier)
				.filter(id -> id.getNamespace().equals(FallingTreeGameTests.NAMESPACE))
				.filter(id -> !BuiltInRegistries.TEST_FUNCTION.containsKey(id))
				.forEach(id -> missingFunctions.add(id.toString()));
		helper.assertTrue(missingFunctions.isEmpty(), "Test instances without registered function: " + missingFunctions);

		helper.succeed();
	}
}
