package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import java.util.List;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDamage;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDropped;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertOnlyBottomLogBroken;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkIntact;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createPlayer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Which tools cut trees, and the break speed multiplier.
 */
public final class ToolScenarios{
	private ToolScenarios(){
	}

	@GameTestCase
	public static void nonAxeDoesNotChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertDropped(helper, Items.OAK_LOG, 1);
		});
	}

	@GameTestCase
	public static void emptyHandDoesNotChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, ItemStack.EMPTY);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void pickaxeDoesNotChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_PICKAXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void woodenAxeChops(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.WOODEN_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDamage(helper, player, TREE_HEIGHT);
		});
	}

	@GameTestCase
	public static void netheriteAxeChops(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.NETHERITE_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDamage(helper, player, TREE_HEIGHT);
		});
	}

	@GameTestCase
	public static void ignoreToolsChopsWithStick(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setIgnoreTools(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
		});
	}

	@GameTestCase
	public static void ignoreToolsChopsWithEmptyHand(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setIgnoreTools(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, ItemStack.EMPTY);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void allowedToolChops(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setAllowed(List.of("minecraft:stick")));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void allowedToolTagChops(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setAllowed(List.of("#minecraft:shovels")));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.IRON_SHOVEL));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void deniedToolDoesNotChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setDenied(List.of("minecraft:diamond_axe")));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertDamage(helper, player, 1);
		});
	}

	@GameTestCase
	public static void deniedToolDoesNotAffectOtherAxes(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setDenied(List.of("minecraft:diamond_axe")));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.IRON_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void deniedToolWinsOverIgnoreTools(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> {
			config.getTools().setIgnoreTools(true);
			config.getTools().setDenied(List.of("minecraft:stick"));
		});
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	/**
	 * With force tool usage, a log cannot be broken at all without a valid tool.
	 */
	@GameTestCase
	public static void forceToolUsagePreventsBreakingLogWithoutTool(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setForceToolUsage(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkIntact(helper);
			assertDropped(helper, Items.OAK_LOG, 0);
		});
	}

	@GameTestCase
	public static void forceToolUsageAllowsAxe(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setForceToolUsage(true));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void forceToolUsageDoesNotAffectOtherBlocks(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setForceToolUsage(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		var dirt = new BlockPos(0, 0, 0);
		playerBreak(helper, player, dirt);

		helper.succeedWhen(() -> helper.assertBlockNotPresent(Blocks.DIRT, dirt));
	}

	/**
	 * With a speed multiplicand, breaking a tree log is slower: the vanilla speed is divided by {@code multiplicand * log count}, where the log count excludes the log being
	 * broken.
	 */
	@GameTestCase
	public static void speedMultiplicandSlowsBreaking(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setSpeedMultiplicand(1));
		var ratio = destroyProgressRatio(helper);
		helper.assertValueInBetween(TREE_HEIGHT - 1 - 0.01F, ratio, TREE_HEIGHT - 1 + 0.01F, "lone log / tree log destroy progress ratio");
		helper.succeed();
	}

	@GameTestCase
	public static void speedMultiplicandHalf(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setSpeedMultiplicand(0.5));
		var ratio = destroyProgressRatio(helper);
		helper.assertValueInBetween((TREE_HEIGHT - 1) / 2F - 0.01F, ratio, (TREE_HEIGHT - 1) / 2F + 0.01F, "lone log / tree log destroy progress ratio");
		helper.succeed();
	}

	@GameTestCase
	public static void speedMultiplicandZeroKeepsVanillaSpeed(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		var ratio = destroyProgressRatio(helper);
		helper.assertValueInBetween(0.99F, ratio, 1.01F, "lone log / tree log destroy progress ratio");
		helper.succeed();
	}

	/**
	 * @return the destroy progress of a lone log (not a tree) divided by the destroy progress of the bottom log of the default tree.
	 */
	private static float destroyProgressRatio(@NonNull GameTestHelper helper){
		placeDefaultTree(helper);
		var loneLog = new BlockPos(6, 1, 6);
		helper.setBlock(loneLog, Blocks.OAK_LOG);
		var player = createLumberjack(helper);

		var loneProgress = destroyProgress(helper, player, loneLog);
		var treeProgress = destroyProgress(helper, player, TREE_BASE);
		helper.assertTrue(treeProgress > 0, "tree log destroy progress should be positive");
		return loneProgress / treeProgress;
	}

	private static float destroyProgress(@NonNull GameTestHelper helper, @NonNull ServerPlayer player, @NonNull BlockPos relativePos){
		return helper.getBlockState(relativePos).getDestroyProgress(player, helper.getLevel(), helper.absolutePos(relativePos));
	}
}
