package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDamage;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDropped;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertOnlyBottomLogBroken;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeFloor;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeLeafCap;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeTrunk;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Core tree cutting behavior, tree recognition and size limits.
 */
public final class TreeBreakingScenarios{
	private TreeBreakingScenarios(){
	}

	/**
	 * A survival player with an axe breaks the bottom log: the whole trunk is cut, every log drops and the axe takes one damage per log.
	 */
	@GameTestCase
	public static void basicChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
			assertDamage(helper, player, TREE_HEIGHT);
		});
	}

	@GameTestCase
	public static void chopFromMiddleCutsWholeTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE.above(2));

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
			assertDamage(helper, player, TREE_HEIGHT);
		});
	}

	/**
	 * The leaves are around the log that is hit.
	 */
	@GameTestCase
	public static void chopFromTopCutsWholeTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE.above(TREE_HEIGHT - 1));

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
		});
	}

	@GameTestCase
	public static void treeBreakingDisabled(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setTreeBreaking(false));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertDropped(helper, Items.OAK_LOG, 1);
			assertDamage(helper, player, 1);
		});
	}

	/**
	 * A pile of logs without leaves (e.g. a player build) is not a tree.
	 */
	@GameTestCase
	public static void trunkWithoutLeavesIsNotATree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeFloor(helper);
		placeTrunk(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void minimumLeavesZeroCutsBareTrunk(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMinimumLeavesAroundRequired(0));
		placeFloor(helper);
		placeTrunk(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	/**
	 * The default leaf cap has 5 leaves around the top log.
	 */
	@GameTestCase
	public static void notEnoughLeavesIsNotATree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMinimumLeavesAroundRequired(6));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void exactlyEnoughLeavesIsATree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMinimumLeavesAroundRequired(5));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	/**
	 * 4 logs to break (the one hit is broken by vanilla) is more than the max size: nothing more than vanilla happens.
	 */
	@GameTestCase
	public static void maxSizeAbort(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMaxSize(3));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertDropped(helper, Items.OAK_LOG, 1);
		});
	}

	/**
	 * Only max size logs are cut (furthest first by default), the rest of the tree stays.
	 */
	@GameTestCase
	public static void maxSizeCut(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> {
			config.getTrees().setMaxSize(3);
			config.getTrees().setMaxSizeAction(MaxSizeAction.CUT);
		});
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			helper.assertBlockPresent(Blocks.OAK_LOG, TREE_BASE.above());
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(2), 3, false);
			assertDropped(helper, Items.OAK_LOG, 4);
		});
	}

	@GameTestCase
	public static void minSizeNotReached(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMinSize(10));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	/**
	 * The min size is compared to the logs to break (excluding the one hit).
	 */
	@GameTestCase
	public static void minSizeReached(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMinSize(TREE_HEIGHT - 1));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void maxScanSizeExceeded(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setMaxScanSize(3));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void breakOrderFurthestFirst(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		cutTwoLogs(helper, mod, BreakOrder.FURTHEST_FIRST);
		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(), 2, true);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, false);
		});
	}

	@GameTestCase
	public static void breakOrderClosestFirst(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		cutTwoLogs(helper, mod, BreakOrder.CLOSEST_FIRST);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 3, false);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, true);
		});
	}

	@GameTestCase
	public static void breakOrderLowestFirst(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		cutTwoLogs(helper, mod, BreakOrder.LOWEST_FIRST);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 3, false);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, true);
		});
	}

	/**
	 * Breaks the bottom log with a max size of 2 logs (cut), so the break order decides which logs are cut.
	 */
	private static void cutTwoLogs(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull BreakOrder breakOrder){
		TestConfiguration.reset(mod, config -> {
			config.getTrees().setMaxSize(2);
			config.getTrees().setMaxSizeAction(MaxSizeAction.CUT);
			config.getTrees().setBreakOrder(breakOrder);
		});
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);
	}

	@GameTestCase
	public static void birchTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES);
	}

	@GameTestCase
	public static void spruceTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES);
	}

	@GameTestCase
	public static void jungleTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES);
	}

	@GameTestCase
	public static void acaciaTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES);
	}

	@GameTestCase
	public static void darkOakTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES);
	}

	@GameTestCase
	public static void cherryTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.CHERRY_LOG, Blocks.CHERRY_LEAVES);
	}

	@GameTestCase
	public static void paleOakTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.PALE_OAK_LOG, Blocks.PALE_OAK_LEAVES);
	}

	@GameTestCase
	public static void azaleaLeavesTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopsWoodType(helper, mod, Blocks.OAK_LOG, Blocks.FLOWERING_AZALEA_LEAVES);
	}

	private static void chopsWoodType(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Block log, @NonNull Block leaves){
		TestConfiguration.reset(mod);
		placeFloor(helper);
		var top = placeTrunk(helper, TREE_BASE, TREE_HEIGHT, log);
		placeLeafCap(helper, top, leaves, true);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, log, TREE_BASE, TREE_HEIGHT, false);
			assertDropped(helper, log.asItem(), TREE_HEIGHT);
		});
	}
}
