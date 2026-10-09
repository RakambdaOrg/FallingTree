package fr.rakambda.fallingtree.gametest;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.SneakMode;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.countDroppedItems;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createPlayer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeFloor;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeSimpleTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.setCrouching;

/**
 * Loader independent test scenarios, registered through {@link FallingTreeGameTests}.
 * <p>
 * Scenarios with non-default configuration must run in the environment named in their javadoc (see {@link TestConfiguration}), which is set in their test instance json.
 */
public final class TreeBreakingScenarios{
	/**
	 * Size of the {@code fallingtree_gametest:empty} structure.
	 */
	private static final int STRUCTURE_SIZE = 8;

	private static final BlockPos TREE_BASE = new BlockPos(3, 1, 3);
	private static final int TREE_HEIGHT = 5;

	private TreeBreakingScenarios(){
	}

	/**
	 * A survival player with an axe breaks the bottom log: the whole trunk is cut, every log drops and the axe takes one damage per log.
	 */
	public static void basicChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var axe = new ItemStack(Items.DIAMOND_AXE);
		var player = createPlayer(helper, GameType.SURVIVAL, axe);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, false);
			helper.assertValueEqual(countDroppedItems(helper, Items.OAK_LOG, TREE_BASE, STRUCTURE_SIZE), TREE_HEIGHT, "dropped oak logs");
			helper.assertValueEqual(player.getMainHandItem().getDamageValue(), TREE_HEIGHT, "axe damage");
		});
	}

	/**
	 * With the default {@link SneakMode#SNEAK_DISABLE}, a sneaking player only breaks the targeted log.
	 */
	public static void sneakingDisablesChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_AXE));
		setCrouching(player);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertVanillaBreak(helper, player));
	}

	/**
	 * Breaking a log with something that is not an axe behaves like vanilla.
	 */
	public static void nonAxeDoesNotChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.STICK));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(), TREE_HEIGHT - 1, true);
			helper.assertValueEqual(countDroppedItems(helper, Items.OAK_LOG, TREE_BASE, STRUCTURE_SIZE), 1, "dropped oak logs");
		});
	}

	/**
	 * Environment: {@code fallingtree_gametest:sneak_enable}. A sneaking player cuts the whole tree.
	 */
	public static void sneakEnableChopsWhenSneaking(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.setSneakMode(SneakMode.SNEAK_ENABLE));
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_AXE));
		setCrouching(player);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, false));
	}

	/**
	 * Environment: {@code fallingtree_gametest:sneak_enable}. A player who is not sneaking only breaks the targeted log.
	 */
	public static void sneakEnableIgnoresStanding(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.setSneakMode(SneakMode.SNEAK_ENABLE));
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertVanillaBreak(helper, player));
	}

	/**
	 * Environment: {@code fallingtree_gametest:damage_multiplicand_zero}. The whole tree only costs the single durability point of the vanilla break.
	 */
	public static void damageMultiplicandZeroCostsOne(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTools().setDamageMultiplicand(0));
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, false);
			helper.assertValueEqual(player.getMainHandItem().getDamageValue(), 1, "axe damage");
		});
	}

	/**
	 * Environment: {@code fallingtree_gametest:fall_item}. The falling animation cuts the whole trunk and the axe takes one damage per log.
	 */
	public static void fallItemChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakMode(BreakMode.FALL_ITEM));
		placeFloor(helper, STRUCTURE_SIZE);
		placeSimpleTree(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG, Blocks.OAK_LEAVES);

		var player = createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, false);
			helper.assertValueEqual(player.getMainHandItem().getDamageValue(), TREE_HEIGHT, "axe damage");
		});
	}

	private static void assertVanillaBreak(@NonNull GameTestHelper helper, @NonNull ServerPlayer player){
		helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(), TREE_HEIGHT - 1, true);
		helper.assertValueEqual(countDroppedItems(helper, Items.OAK_LOG, TREE_BASE, STRUCTURE_SIZE), 1, "dropped oak logs");
		helper.assertValueEqual(player.getMainHandItem().getDamageValue(), 1, "axe damage");
	}
}
