package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.function.Consumer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertBlockCount;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertOnlyBottomLogBroken;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.leafState;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeFloor;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeLeafCap;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeTrunk;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Leaves: what counts as leaves, and what happens to them once the tree is cut. Random ticks are disabled in test environments, so leaves only decay through the mod.
 */
public final class LeafScenarios{
	/**
	 * Leaves are scheduled to decay 4 ticks after a neighbor is removed.
	 */
	private static final int LEAF_CHECK_TICK = 30;

	private LeafScenarios(){
	}

	@GameTestCase
	public static void leavesDecayAfterChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, false, config -> {});
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertBlockCount(helper, Blocks.OAK_LEAVES, 0);
		});
	}

	@GameTestCase
	public static void leavesStayWhenLeavesBreakingDisabled(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, false, config -> config.getTrees().setLeavesBreaking(false));
		helper.runAtTickTime(LEAF_CHECK_TICK, () -> {
			assertTrunkCut(helper);
			assertBlockCount(helper, Blocks.OAK_LEAVES, 5);
			helper.succeed();
		});
	}

	@GameTestCase
	public static void persistentLeavesStayAfterChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, true, config -> {});
		helper.runAtTickTime(LEAF_CHECK_TICK, () -> {
			assertTrunkCut(helper);
			assertBlockCount(helper, Blocks.OAK_LEAVES, 5);
			helper.succeed();
		});
	}

	/**
	 * Force breaking removes leaves around the top log, even persistent ones.
	 */
	@GameTestCase
	public static void leavesForceRadiusRemovesLeaves(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, true, config -> config.getTrees().setLeavesBreakingForceRadius(2));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertBlockCount(helper, Blocks.OAK_LEAVES, 0);
		});
	}

	@GameTestCase
	public static void leavesForceRadiusKeepsFarLeaves(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var far = new BlockPos(7, 1, 7);
		TestConfiguration.reset(mod, config -> config.getTrees().setLeavesBreakingForceRadius(2));
		placeTree(helper, Blocks.OAK_LEAVES, true);
		helper.setBlock(far, leafState(Blocks.OAK_LEAVES, true));
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertBlockPresent(Blocks.OAK_LEAVES, far);
			assertBlockCount(helper, Blocks.OAK_LEAVES, 1);
		});
	}

	@GameTestCase
	public static void persistentLeavesNotCountedWhenExcluded(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, true, config -> config.getTrees().setIncludePersistentLeavesInRequiredCount(false));
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void naturalLeavesCountedWhenPersistentExcluded(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, false, config -> config.getTrees().setIncludePersistentLeavesInRequiredCount(false));
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void deniedLeavesAreNotLeaves(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.OAK_LEAVES, true, config -> config.getTrees().setDeniedLeaves(List.of("minecraft:oak_leaves")));
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void blocksAreNotLeavesByDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.GLASS, true, config -> {});
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void allowedLeavesCountAsLeaves(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.GLASS, true, config -> config.getTrees().setAllowedLeaves(List.of("minecraft:glass")));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertBlockCount(helper, Blocks.GLASS, 5);
		});
	}

	/**
	 * Non decaying leaves count as leaves and are broken along with the tree.
	 */
	@GameTestCase
	public static void allowedNonDecayLeavesAreBroken(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopTree(helper, mod, Blocks.HAY_BLOCK, true, config -> config.getTrees().setAllowedNonDecayLeaves(List.of("minecraft:hay_block")));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertBlockCount(helper, Blocks.HAY_BLOCK, 0);
		});
	}

	/**
	 * Leaves further than the max distance from a log are not part of the tree.
	 */
	@GameTestCase
	public static void maxLeafDistanceFromLog(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var top = chopHayColumnTree(helper, mod, 1);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertBlockNotPresent(Blocks.HAY_BLOCK, top.above());
			helper.assertBlockPresent(Blocks.HAY_BLOCK, top.above(2));
		});
	}

	@GameTestCase
	public static void maxLeafDistanceFromLogDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var top = chopHayColumnTree(helper, mod, 15);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertBlockNotPresent(Blocks.HAY_BLOCK, top.above());
			helper.assertBlockNotPresent(Blocks.HAY_BLOCK, top.above(2));
		});
	}

	/**
	 * A 4 logs trunk with non decaying leaves around its top and a column of 2 more above it.
	 *
	 * @return the position of the top log.
	 */
	@NonNull
	private static BlockPos chopHayColumnTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, int maxLeafDistance){
		TestConfiguration.reset(mod, config -> {
			config.getTrees().setAllowedNonDecayLeaves(List.of("minecraft:hay_block"));
			config.getTrees().setMaxLeafDistanceFromLog(maxLeafDistance);
		});
		placeFloor(helper);
		var top = placeTrunk(helper, TREE_BASE, TREE_HEIGHT - 1, Blocks.OAK_LOG);
		placeLeafCap(helper, top, Blocks.HAY_BLOCK, true);
		helper.setBlock(top.above(2), Blocks.HAY_BLOCK);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
		return top;
	}

	private static void chopTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Block leaves, boolean persistent, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeTree(helper, leaves, persistent);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}

	private static void placeTree(@NonNull GameTestHelper helper, @NonNull Block leaves, boolean persistent){
		placeFloor(helper);
		var top = placeTrunk(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG);
		placeLeafCap(helper, top, leaves, persistent);
	}
}
