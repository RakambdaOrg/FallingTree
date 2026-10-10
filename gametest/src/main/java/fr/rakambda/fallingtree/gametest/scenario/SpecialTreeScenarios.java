package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertBlockCount;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeFloor;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeLeafCap;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeTrunk;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Nether fungi (wart blocks, shroomlights) and mangroves (roots).
 */
public final class SpecialTreeScenarios{
	private SpecialTreeScenarios(){
	}

	@GameTestCase
	public static void crimsonFungusBreaksWarts(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopFungus(helper, mod, Blocks.CRIMSON_STEM, Blocks.NETHER_WART_BLOCK, true);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.CRIMSON_STEM, TREE_BASE, TREE_HEIGHT, false);
			assertBlockCount(helper, Blocks.NETHER_WART_BLOCK, 0);
			assertBlockCount(helper, Blocks.SHROOMLIGHT, 0);
		});
	}

	@GameTestCase
	public static void warpedFungusBreaksWarts(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopFungus(helper, mod, Blocks.WARPED_STEM, Blocks.WARPED_WART_BLOCK, true);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.WARPED_STEM, TREE_BASE, TREE_HEIGHT, false);
			assertBlockCount(helper, Blocks.WARPED_WART_BLOCK, 0);
			assertBlockCount(helper, Blocks.SHROOMLIGHT, 0);
		});
	}

	@GameTestCase
	public static void netherWartsKeptWhenDisabled(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopFungus(helper, mod, Blocks.CRIMSON_STEM, Blocks.NETHER_WART_BLOCK, false);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.CRIMSON_STEM, TREE_BASE, TREE_HEIGHT, false);
			assertBlockCount(helper, Blocks.NETHER_WART_BLOCK, 4);
			assertBlockCount(helper, Blocks.SHROOMLIGHT, 1);
		});
	}

	/**
	 * A stem trunk with wart blocks around its top and a shroomlight above it.
	 */
	private static void chopFungus(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull Block stem, @NonNull Block wart, boolean breakWarts){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakNetherTreeWarts(breakWarts));
		placeFloor(helper);
		var top = placeTrunk(helper, TREE_BASE, TREE_HEIGHT, stem);
		placeLeafCap(helper, top, wart, true);
		helper.setBlock(top.above(), Blocks.SHROOMLIGHT);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);
	}

	@GameTestCase
	public static void mangroveRootsAreBroken(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMangrove(helper, mod, true);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.MANGROVE_LOG, TREE_BASE.above(), TREE_HEIGHT - 1, false);
			assertBlockCount(helper, Blocks.MANGROVE_ROOTS, 0);
		});
	}

	@GameTestCase
	public static void mangroveRootsKeptWhenDisabled(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopMangrove(helper, mod, false);
		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.MANGROVE_LOG, TREE_BASE.above(), TREE_HEIGHT - 1, false);
			assertBlockCount(helper, Blocks.MANGROVE_ROOTS, 3);
		});
	}

	/**
	 * Roots under and around the bottom of a mangrove trunk.
	 */
	private static void chopMangrove(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, boolean breakRoots){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakMangroveRoots(breakRoots));
		placeFloor(helper);
		helper.setBlock(TREE_BASE, Blocks.MANGROVE_ROOTS);
		helper.setBlock(TREE_BASE.east(), Blocks.MANGROVE_ROOTS);
		helper.setBlock(TREE_BASE.west(), Blocks.MANGROVE_ROOTS);
		var top = placeTrunk(helper, TREE_BASE.above(), TREE_HEIGHT - 1, Blocks.MANGROVE_LOG);
		placeLeafCap(helper, top, Blocks.MANGROVE_LEAVES, true);
		playerBreak(helper, createLumberjack(helper), TREE_BASE.above());
	}
}
