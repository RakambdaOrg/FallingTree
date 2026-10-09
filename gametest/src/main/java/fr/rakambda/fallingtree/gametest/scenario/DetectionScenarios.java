package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import java.util.Map;
import java.util.function.Consumer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Detection modes, when cutting the middle log (third of five) of the default tree. Leaves are only around the top log.
 */
public final class DetectionScenarios{
	private DetectionScenarios(){
	}

	@GameTestCase
	public static void wholeTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionMode(DetectionMode.WHOLE_TREE));
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void aboveCut(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionMode(DetectionMode.ABOVE_CUT));
		helper.succeedWhen(() -> assertOnlyMiddleAndAboveCut(helper));
	}

	@GameTestCase
	public static void aboveY(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionMode(DetectionMode.ABOVE_Y));
		helper.succeedWhen(() -> assertOnlyMiddleAndAboveCut(helper));
	}

	/**
	 * Below modes check the leaves around the bottom-most log, there are none in the default tree.
	 */
	@GameTestCase
	public static void belowCutRequiresLeavesAroundBottomLog(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionMode(DetectionMode.BELOW_CUT));
		helper.succeedWhen(() -> assertOnlyMiddleCut(helper));
	}

	@GameTestCase
	public static void belowCut(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> {
			config.getTrees().setDetectionMode(DetectionMode.BELOW_CUT);
			config.getTrees().setMinimumLeavesAroundRequired(0);
		});
		helper.succeedWhen(() -> assertOnlyMiddleAndBelowCut(helper));
	}

	@GameTestCase
	public static void belowY(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> {
			config.getTrees().setDetectionMode(DetectionMode.BELOW_Y);
			config.getTrees().setMinimumLeavesAroundRequired(0);
		});
		helper.succeedWhen(() -> assertOnlyMiddleAndBelowCut(helper));
	}

	@GameTestCase
	public static void wholeTreeDownwardsRequiresLeavesAroundBottomLog(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionMode(DetectionMode.WHOLE_TREE_DOWNWARDS));
		helper.succeedWhen(() -> assertOnlyMiddleCut(helper));
	}

	@GameTestCase
	public static void wholeTreeDownwards(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMiddle(helper, mod, config -> {
			config.getTrees().setDetectionMode(DetectionMode.WHOLE_TREE_DOWNWARDS);
			config.getTrees().setMinimumLeavesAroundRequired(0);
		});
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void biomeOverrideApplies(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		helper.setBiome(Biomes.PLAINS);
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionModeBiomeOverride(Map.of("minecraft:plains", DetectionMode.ABOVE_CUT)));
		helper.succeedWhen(() -> assertOnlyMiddleAndAboveCut(helper));
	}

	@GameTestCase
	public static void biomeOverrideIgnoredInOtherBiome(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		helper.setBiome(Biomes.PLAINS);
		chopMiddle(helper, mod, config -> config.getTrees().setDetectionModeBiomeOverride(Map.of("minecraft:desert", DetectionMode.ABOVE_CUT)));
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	private static void chopMiddle(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE.above(2));
	}

	private static void assertOnlyMiddleCut(@NonNull GameTestHelper helper){
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 2, true);
		helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE.above(2));
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, true);
	}

	private static void assertOnlyMiddleAndAboveCut(@NonNull GameTestHelper helper){
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 2, true);
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(2), 3, false);
	}

	private static void assertOnlyMiddleAndBelowCut(@NonNull GameTestHelper helper){
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 3, false);
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, true);
	}
}
