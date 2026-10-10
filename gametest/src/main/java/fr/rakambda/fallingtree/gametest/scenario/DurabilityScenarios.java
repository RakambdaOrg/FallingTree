package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.DamageRounding;
import fr.rakambda.fallingtree.common.config.enums.DurabilityMode;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import java.util.function.Consumer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDamage;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertOnlyBottomLogBroken;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkIntact;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.axeWithDurability;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createPlayer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Tool damage (multiplier, rounding) and durability modes. The default tree has 5 logs: the bottom one is broken by vanilla and 4 by the mod.
 */
public final class DurabilityScenarios{
	private DurabilityScenarios(){
	}

	/**
	 * The whole tree only costs the single durability point of the vanilla break.
	 */
	@GameTestCase
	public static void damageMultiplicandZeroCostsOne(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithFullAxe(helper, mod, config -> config.getTools().setDamageMultiplicand(0));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDamage(helper, player, 1);
		});
	}

	@GameTestCase
	public static void damageMultiplicandTwo(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithFullAxe(helper, mod, config -> config.getTools().setDamageMultiplicand(2));
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDamage(helper, player, 2 * TREE_HEIGHT);
		});
	}

	/**
	 * 5 logs * 0.5 = 2.5, rounded down.
	 */
	@GameTestCase
	public static void damageMultiplicandHalfRoundDown(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithFullAxe(helper, mod, config -> {
			config.getTools().setDamageMultiplicand(0.5);
			config.getTools().setDamageRounding(DamageRounding.ROUND_DOWN);
		});
		helper.succeedWhen(() -> assertDamage(helper, player, 2));
	}

	@GameTestCase
	public static void damageMultiplicandHalfRoundUp(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithFullAxe(helper, mod, config -> {
			config.getTools().setDamageMultiplicand(0.5);
			config.getTools().setDamageRounding(DamageRounding.ROUND_UP);
		});
		helper.succeedWhen(() -> assertDamage(helper, player, 3));
	}

	@GameTestCase
	public static void damageMultiplicandHalfRounding(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithFullAxe(helper, mod, config -> {
			config.getTools().setDamageMultiplicand(0.5);
			config.getTools().setDamageRounding(DamageRounding.ROUNDING);
		});
		helper.succeedWhen(() -> assertDamage(helper, player, 3));
	}

	@GameTestCase
	public static void damageMultiplicandHalfProbabilistic(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithFullAxe(helper, mod, config -> {
			config.getTools().setDamageMultiplicand(0.5);
			config.getTools().setDamageRounding(DamageRounding.PROBABILISTIC);
		});
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertValueInBetween(2, player.getMainHandItem().getDamageValue(), 3, "tool damage");
		});
	}

	/**
	 * Damage is not applied to creative players.
	 */
	@GameTestCase
	public static void creativeToolIsNotDamaged(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.setBreakInCreative(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.CREATIVE, axeWithDurability(1561));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDamage(helper, player, 0);
		});
	}

	@GameTestCase
	public static void abortModeCutsWithEnoughDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.ABORT, 100);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertRemainingDurability(helper, player, 100 - TREE_HEIGHT);
		});
	}

	/**
	 * The tool cannot cut the whole tree: only the vanilla break happens.
	 */
	@GameTestCase
	public static void abortModeDoesNotCutWithoutEnoughDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.ABORT, 3);
		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertRemainingDurability(helper, player, 2);
		});
	}

	/**
	 * With a single durability point left, the log cannot be broken at all.
	 */
	@GameTestCase
	public static void abortModePreventsBreakingWithLastDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.ABORT, 1);
		helper.succeedWhen(() -> {
			assertTrunkIntact(helper);
			assertRemainingDurability(helper, player, 1);
		});
	}

	@GameTestCase
	public static void saveModePreventsBreakingWithLastDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.SAVE, 1);
		helper.succeedWhen(() -> {
			assertTrunkIntact(helper);
			assertRemainingDurability(helper, player, 1);
		});
	}

	/**
	 * Cuts part of the tree (furthest logs first) but keeps 1 durability: 3 durability breaks the hit log and the 2 top ones.
	 */
	@GameTestCase
	public static void saveModeKeepsTheTool(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.SAVE, 3);
		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(), 2, true);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(3), 2, false);
			assertRemainingDurability(helper, player, 1);
		});
	}

	@GameTestCase
	public static void saveModeCutsWithEnoughDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.SAVE, 100);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertRemainingDurability(helper, player, 100 - TREE_HEIGHT);
		});
	}

	/**
	 * Cuts as many logs as the durability allows (furthest first), using the tool up: 3 durability breaks the hit log and the 3 top ones.
	 */
	@GameTestCase
	public static void normalModeCutsPartOfTreeWithLowDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.NORMAL, 3);
		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			helper.assertBlockPresent(Blocks.OAK_LOG, TREE_BASE.above());
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(2), 3, false);
			helper.assertTrue(player.getMainHandItem().isEmpty(), "tool should be broken");
		});
	}

	/**
	 * Exactly enough durability for the whole tree: everything is cut and the tool breaks on the last log.
	 */
	@GameTestCase
	public static void normalModeExactDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.NORMAL, TREE_HEIGHT);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertTrue(player.getMainHandItem().isEmpty(), "tool should be broken");
		});
	}

	@GameTestCase
	public static void normalModeOneSpareDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.NORMAL, TREE_HEIGHT + 1);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertRemainingDurability(helper, player, 1);
		});
	}

	/**
	 * Cuts the whole tree whatever the durability, breaking the tool.
	 */
	@GameTestCase
	public static void bypassModeCutsWholeTreeAndBreaksTool(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithDurability(helper, mod, DurabilityMode.BYPASS, 3);
		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			helper.assertTrue(player.getMainHandItem().isEmpty(), "tool should be broken");
		});
	}

	@NonNull
	private static ServerPlayer chopWithFullAxe(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Consumer<Configuration> overrides){
		TestConfiguration.reset(mod, overrides);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);
		return player;
	}

	@NonNull
	private static ServerPlayer chopWithDurability(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull DurabilityMode durabilityMode, int remainingDurability){
		TestConfiguration.reset(mod, config -> config.getTools().setDurabilityMode(durabilityMode));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, axeWithDurability(remainingDurability));
		playerBreak(helper, player, TREE_BASE);
		return player;
	}

	private static int remainingDurability(@NonNull ServerPlayer player){
		var tool = player.getMainHandItem();
		return tool.getMaxDamage() - tool.getDamageValue();
	}

	private static void assertRemainingDurability(@NonNull GameTestHelper helper, @NonNull ServerPlayer player, int expected){
		helper.assertFalse(player.getMainHandItem().isEmpty(), "tool should not be broken");
		helper.assertValueEqual(remainingDurability(player), expected, "remaining durability");
	}
}
