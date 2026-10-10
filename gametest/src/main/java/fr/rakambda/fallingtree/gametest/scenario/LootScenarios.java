package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDropped;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.countDroppedItems;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Where and how much loot is dropped.
 */
public final class LootScenarios{
	private LootScenarios(){
	}

	/**
	 * By default, each log drops where it was: right after the cut, there are items up at the top of the trunk.
	 */
	@GameTestCase
	public static void itemsSpawnAtEachLogByDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		var top = TREE_BASE.above(TREE_HEIGHT - 1);
		helper.assertTrue(countDroppedItems(helper, Items.OAK_LOG, top, 1) > 0, "items should be dropped at the top log");
		helper.succeed();
	}

	@GameTestCase
	public static void spawnItemsAtBreakPoint(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setSpawnItemsAtBreakPoint(true));
		placeDefaultTree(helper);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		assertTrunkCut(helper);
		helper.assertValueEqual(countDroppedItems(helper, Items.OAK_LOG, TREE_BASE, 1.5), TREE_HEIGHT, "oak logs dropped at the break point");
		helper.succeed();
	}

	/**
	 * Half of the 4 logs cut by the mod drop, plus the one broken by vanilla.
	 */
	@GameTestCase
	public static void trunkLootPercentageHalf(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setTrunkLootPercentage(0.5F));
		placeDefaultTree(helper);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, 3);
		});
	}

	@GameTestCase
	public static void trunkLootPercentageZero(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setTrunkLootPercentage(0F));
		placeDefaultTree(helper);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, 1);
		});
	}
}
