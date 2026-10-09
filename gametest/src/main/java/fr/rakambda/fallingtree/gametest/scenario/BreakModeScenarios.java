package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDamage;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDropped;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertNoFallingBlocks;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.countBlocks;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.countDroppedItems;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeFloor;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeTrunk;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * The different break modes. The default tree has 5 logs and 5 persistent leaves.
 */
public final class BreakModeScenarios{

	private BreakModeScenarios(){
	}

	/**
	 * Each break removes the furthest log while the hit log stays: the tree shifts down.
	 */
	@GameTestCase
	public static void shiftDownRemovesOneLog(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.SHIFT_DOWN);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT - 1, true);
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE.above(TREE_HEIGHT - 1));
			assertDropped(helper, Items.OAK_LOG, 1);
			assertDamage(helper, player, 1);
		});
	}

	@GameTestCase
	public static void shiftDownTwiceRemovesTwoLogs(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.SHIFT_DOWN);
		playerBreak(helper, player, TREE_BASE);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT - 2, true);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(TREE_HEIGHT - 2), 2, false);
			assertDropped(helper, Items.OAK_LOG, 2);
			assertDamage(helper, player, 2);
		});
	}

	/**
	 * Shift down does not check for leaves around the tree.
	 */
	@GameTestCase
	public static void shiftDownWorksWithoutLeaves(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakMode(BreakMode.SHIFT_DOWN));
		placeFloor(helper);
		placeTrunk(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT - 1, true);
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE.above(TREE_HEIGHT - 1));
		});
	}

	/**
	 * The last log of a tree breaks normally.
	 */
	@GameTestCase
	public static void shiftDownBreaksLastLog(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakMode(BreakMode.SHIFT_DOWN));
		placeFloor(helper);
		helper.setBlock(TREE_BASE, Blocks.OAK_LOG);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			assertDropped(helper, Items.OAK_LOG, 1);
		});
	}

	/**
	 * Logs and leaves are dropped as items, the falling blocks are only visual.
	 */
	@GameTestCase(maxTicks = 100)
	public static void fallItem(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.FALL_ITEM);
		assertFallItem(helper, player);
	}

	@GameTestCase(maxTicks = 100)
	public static void fallItemStraight(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.FALL_ITEM_STRAIGHT);
		assertFallItem(helper, player);
	}

	private static void assertFallItem(@NonNull GameTestHelper helper, @NonNull ServerPlayer player){
		assertDamage(helper, player, TREE_HEIGHT);
		helper.succeedWhen(() -> {
			assertNoFallingBlocks(helper);
			helper.assertValueEqual(countBlocks(helper, Blocks.OAK_LOG), 0, "oak logs placed");
			helper.assertValueEqual(countBlocks(helper, Blocks.OAK_LEAVES), 0, "oak leaves placed");
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
		});
	}

	/**
	 * Logs fall and land as blocks, leaves are dropped as items.
	 */
	@GameTestCase(maxTicks = 100)
	public static void fallBlock(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.FALL_BLOCK);
		assertDamage(helper, player, TREE_HEIGHT);
		helper.succeedWhen(() -> {
			assertNoFallingBlocks(helper);
			assertLogsLanded(helper);
			helper.assertValueEqual(countBlocks(helper, Blocks.OAK_LEAVES), 0, "oak leaves placed");
		});
	}

	/**
	 * Logs and leaves fall and land as blocks.
	 */
	@GameTestCase(maxTicks = 100)
	public static void fallAllBlock(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.FALL_ALL_BLOCK);
		assertDamage(helper, player, TREE_HEIGHT);
		helper.succeedWhen(() -> {
			assertNoFallingBlocks(helper);
			assertLogsLanded(helper);
			var leaves = countBlocks(helper, Blocks.OAK_LEAVES) + countDroppedItems(helper, Items.OAK_LEAVES);
			helper.assertValueEqual(leaves, 5, "oak leaves placed or dropped");
			helper.assertTrue(countBlocks(helper, Blocks.OAK_LEAVES) > 0, "some oak leaves should have landed as blocks");
		});
	}

	/**
	 * Every log ends up as a block on the ground or as an item: logs falling in the same column compete for the same landing spot, and a falling block that can't land
	 * drops as an item.
	 */
	private static void assertLogsLanded(@NonNull GameTestHelper helper){
		var logBlocks = countBlocks(helper, Blocks.OAK_LOG);
		helper.assertValueEqual(logBlocks + countDroppedItems(helper, Items.OAK_LOG), TREE_HEIGHT, "oak logs placed or dropped");
		helper.assertTrue(logBlocks > 0, "some oak logs should have landed as blocks");
	}

	@GameTestCase
	public static void instantaneousIsDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithBreakMode(helper, mod, BreakMode.INSTANTANEOUS);
		assertTrunkCut(helper);
		assertNoFallingBlocks(helper);
		helper.succeedWhen(() -> {
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
			assertDamage(helper, player, TREE_HEIGHT);
		});
	}

	@NonNull
	private static ServerPlayer chopWithBreakMode(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull BreakMode breakMode){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakMode(breakMode));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);
		return player;
	}
}
