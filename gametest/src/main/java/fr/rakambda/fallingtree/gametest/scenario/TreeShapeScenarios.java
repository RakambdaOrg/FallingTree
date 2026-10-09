package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.function.Consumer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertOnlyBottomLogBroken;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeFloor;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeLeafCap;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeTrunk;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Which logs belong to the tree: log lists, mixed logs, branches, search radius and adjacent blocks.
 */
public final class TreeShapeScenarios{
	/**
	 * Horizontal branch going out of the third log of the default trunk.
	 */
	private static final List<BlockPos> BRANCH = List.of(TREE_BASE.above(2).east(), TREE_BASE.above(2).east(2));

	private TreeShapeScenarios(){
	}

	@GameTestCase
	public static void deniedLogIsNotChopped(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopDefaultTree(helper, mod, config -> config.getTrees().setDeniedLogs(List.of("minecraft:oak_log")));
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void deniedLogTagIsNotChopped(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopDefaultTree(helper, mod, config -> config.getTrees().setDeniedLogs(List.of("#minecraft:oak_logs")));
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void strippedLogsAreNotChoppedByDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopStrippedTree(helper, mod, config -> {});
		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.STRIPPED_OAK_LOG, TREE_BASE);
			assertColumn(helper, Blocks.STRIPPED_OAK_LOG, TREE_BASE.above(), TREE_HEIGHT - 1, true);
		});
	}

	@GameTestCase
	public static void allowedLogIsChopped(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopStrippedTree(helper, mod, config -> config.getTrees().setAllowedLogs(List.of("minecraft:stripped_oak_log")));
		helper.succeedWhen(() -> assertColumn(helper, Blocks.STRIPPED_OAK_LOG, TREE_BASE, TREE_HEIGHT, false));
	}

	private static void chopStrippedTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeFloor(helper);
		var top = placeTrunk(helper, TREE_BASE, TREE_HEIGHT, Blocks.STRIPPED_OAK_LOG);
		placeLeafCap(helper, top, Blocks.OAK_LEAVES, true);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}

	/**
	 * Oak logs at the bottom, birch at the top with the leaves. Without mixed logs the oak part is a separate "tree" without leaves.
	 */
	@GameTestCase
	public static void mixedLogsAreSeparateTreesByDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMixedTree(helper, mod, false);
		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			helper.assertBlockPresent(Blocks.OAK_LOG, TREE_BASE.above());
			assertColumn(helper, Blocks.BIRCH_LOG, TREE_BASE.above(2), 3, true);
		});
	}

	@GameTestCase
	public static void mixedLogsAllowed(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMixedTree(helper, mod, true);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 2, false);
			assertColumn(helper, Blocks.BIRCH_LOG, TREE_BASE.above(2), 3, false);
		});
	}

	private static void chopMixedTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, boolean allowMixedLogs){
		TestConfiguration.reset(mod, config -> config.getTrees().setAllowMixedLogs(allowMixedLogs));
		placeFloor(helper);
		placeTrunk(helper, TREE_BASE, 2, Blocks.OAK_LOG);
		var top = placeTrunk(helper, TREE_BASE.above(2), 3, Blocks.BIRCH_LOG);
		placeLeafCap(helper, top, Blocks.BIRCH_LEAVES, true);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}

	/**
	 * A tree right next to another one is cut with it (logs touch diagonally).
	 */
	@GameTestCase
	public static void touchingTreesAreCutTogether(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var otherBase = TREE_BASE.east().south();
		var otherTop = placeTrunk(helper, otherBase, TREE_HEIGHT, Blocks.OAK_LOG);
		placeLeafCap(helper, otherTop, Blocks.OAK_LEAVES, true);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertColumn(helper, Blocks.OAK_LOG, otherBase, TREE_HEIGHT, false);
		});
	}

	@GameTestCase
	public static void separateTreeIsNotCut(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var otherBase = new BlockPos(6, 1, 6);
		var otherTop = placeTrunk(helper, otherBase, TREE_HEIGHT, Blocks.OAK_LOG);
		placeLeafCap(helper, otherTop, Blocks.OAK_LEAVES, true);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertColumn(helper, Blocks.OAK_LOG, otherBase, TREE_HEIGHT, true);
		});
	}

	@GameTestCase
	public static void branchesAreCut(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopBranchTree(helper, mod, config -> {});
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			BRANCH.forEach(pos -> helper.assertBlockNotPresent(Blocks.OAK_LOG, pos));
		});
	}

	/**
	 * A search radius of 0 only keeps the logs in the column of the one hit.
	 */
	@GameTestCase
	public static void searchAreaRadiusExcludesBranches(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopBranchTree(helper, mod, config -> config.getTrees().setSearchAreaRadius(0));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			BRANCH.forEach(pos -> helper.assertBlockPresent(Blocks.OAK_LOG, pos));
		});
	}

	@GameTestCase
	public static void searchAreaRadiusIncludesCloseBranch(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopBranchTree(helper, mod, config -> config.getTrees().setSearchAreaRadius(1));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertBlockNotPresent(Blocks.OAK_LOG, BRANCH.get(0));
			helper.assertBlockPresent(Blocks.OAK_LOG, BRANCH.get(1));
		});
	}

	private static void chopBranchTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeDefaultTree(helper);
		BRANCH.forEach(pos -> helper.setBlock(pos, Blocks.OAK_LOG));
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}

	/**
	 * Without allowed adjacent blocks configured, anything can touch the tree.
	 */
	@GameTestCase
	public static void adjacentBlocksUnrestrictedByDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithCobblestone(helper, mod, TREE_BASE.above(2).east(), config -> {});
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	/**
	 * A block touching the tree that isn't allowed (nor air, leaves or logs) aborts the whole search.
	 */
	@GameTestCase
	public static void adjacentStopAll(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithCobblestone(helper, mod, TREE_BASE.above(2).east(), config -> {
			config.getTrees().setAllowedAdjacentBlocks(List.of("minecraft:dirt"));
			config.getTrees().setAdjacentStopMode(AdjacentStopMode.STOP_ALL);
		});
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	/**
	 * Allowed adjacent blocks don't stop the search (air, leaves and logs are always allowed).
	 */
	@GameTestCase
	public static void adjacentAllowedBlocksDoNotStop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithCobblestone(helper, mod, TREE_BASE.above(2).east(), config -> config.getTrees().setAllowedAdjacentBlocks(List.of("minecraft:dirt", "minecraft:cobblestone")));
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	/**
	 * Logs touching a block that isn't allowed are excluded from the tree, along with what is only reachable through them.
	 */
	@GameTestCase
	public static void adjacentStopBranch(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithCobblestone(helper, mod, TREE_BASE.above(3).east(), config -> {
			config.getTrees().setAllowedAdjacentBlocks(List.of("minecraft:dirt"));
			config.getTrees().setAdjacentStopMode(AdjacentStopMode.STOP_BRANCH);
			config.getTrees().setMinimumLeavesAroundRequired(0);
		});
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 3, false);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, true);
		});
	}

	private static void chopWithCobblestone(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull BlockPos cobblestone, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeDefaultTree(helper);
		helper.setBlock(cobblestone, Blocks.COBBLESTONE);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}

	private static void chopDefaultTree(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeDefaultTree(helper);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}
}
