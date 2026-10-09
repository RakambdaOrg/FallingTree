package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.ProtectionSimulator;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDamage;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDropped;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkIntact;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;

/**
 * Interaction with protection/claim mods vetoing block breaks (see {@link ProtectionSimulator}).
 */
public final class ProtectionScenarios{
	private ProtectionScenarios(){
	}

	/**
	 * The hit log is protected: the tree must not be cut at all (the protection must be asked before the tree is cut).
	 */
	@GameTestCase
	public static void protectedHitLogPreventsCut(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		ProtectionSimulator.deny(helper, TREE_BASE);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkIntact(helper);
			assertDropped(helper, Items.OAK_LOG, 0);
			assertDamage(helper, player, 0);
		});
	}

	/**
	 * Another log of the tree is protected: it is kept, the rest of the tree is cut.
	 */
	@GameTestCase
	public static void protectedOtherLogIsKept(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var protectedLog = TREE_BASE.above(2);
		ProtectionSimulator.deny(helper, protectedLog);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, 2, false);
			helper.assertBlockPresent(Blocks.OAK_LOG, protectedLog);
			assertColumn(helper, Blocks.OAK_LOG, protectedLog.above(), 2, false);
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT - 1);
		});
	}

	/**
	 * The hit log is protected in shift down mode: nothing is removed.
	 */
	@GameTestCase
	public static void protectedHitLogPreventsShiftDown(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getTrees().setBreakMode(BreakMode.SHIFT_DOWN));
		placeDefaultTree(helper);
		ProtectionSimulator.deny(helper, TREE_BASE);
		playerBreak(helper, createLumberjack(helper), TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkIntact(helper);
			assertDropped(helper, Items.OAK_LOG, 0);
		});
	}
}
