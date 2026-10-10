package fr.rakambda.fallingtree.gametest.scenario;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.SneakMode;
import fr.rakambda.fallingtree.gametest.GameTestCase;
import fr.rakambda.fallingtree.gametest.TestConfiguration;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import java.util.List;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_BASE;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.TREE_HEIGHT;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertColumn;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDamage;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertDropped;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertOnlyBottomLogBroken;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.assertTrunkCut;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createLumberjack;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.createPlayer;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.enchant;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.placeDefaultTree;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.playerBreak;
import static fr.rakambda.fallingtree.gametest.GameTestUtils.setCrouching;

/**
 * Player state: sneaking, game mode, tags, toggle command and required enchantments.
 * <p>
 * The test datapack puts {@code minecraft:efficiency} and {@code minecraft:unbreaking} in {@code #fallingtree:chopper_all}, and {@code minecraft:unbreaking} in
 * {@code #fallingtree:chopper_shift_down}.
 */
public final class PlayerScenarios{
	private PlayerScenarios(){
	}

	/**
	 * With the default {@link SneakMode#SNEAK_DISABLE}, a sneaking player only breaks the targeted log.
	 */
	@GameTestCase
	public static void sneakingDisablesChop(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		var player = chopWithSneakMode(helper, mod, SneakMode.SNEAK_DISABLE, true);
		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertDropped(helper, Items.OAK_LOG, 1);
			assertDamage(helper, player, 1);
		});
	}

	@GameTestCase
	public static void sneakEnableChopsWhenSneaking(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithSneakMode(helper, mod, SneakMode.SNEAK_ENABLE, true);
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void sneakEnableIgnoresStanding(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithSneakMode(helper, mod, SneakMode.SNEAK_ENABLE, false);
		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void sneakIgnoreChopsWhenSneaking(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithSneakMode(helper, mod, SneakMode.IGNORE, true);
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void sneakIgnoreChopsWhenStanding(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		chopWithSneakMode(helper, mod, SneakMode.IGNORE, false);
		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@NonNull
	private static ServerPlayer chopWithSneakMode(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod, @NonNull SneakMode sneakMode, boolean sneaking){
		TestConfiguration.reset(mod, config -> config.setSneakMode(sneakMode));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		if(sneaking){
			setCrouching(player);
		}
		playerBreak(helper, player, TREE_BASE);
		return player;
	}

	@GameTestCase
	public static void creativeDoesNotChopByDefault(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.CREATIVE, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertOnlyBottomLogBroken(helper);
			assertDropped(helper, Items.OAK_LOG, 0);
		});
	}

	/**
	 * Loot in creative is enabled by default: every log drops, including the one hit.
	 */
	@GameTestCase
	public static void breakInCreativeChops(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.setBreakInCreative(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.CREATIVE, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, TREE_HEIGHT);
		});
	}

	@GameTestCase
	public static void breakInCreativeWithoutLoot(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> {
			config.setBreakInCreative(true);
			config.setLootInCreative(false);
		});
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.CREATIVE, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertTrunkCut(helper);
			assertDropped(helper, Items.OAK_LOG, 0);
		});
	}

	@GameTestCase
	public static void adventureModeCannotBreak(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.ADVENTURE, new ItemStack(Items.DIAMOND_AXE));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, true));
	}

	@GameTestCase
	public static void allowedTagsBlockPlayerWithoutTag(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getPlayer().setAllowedTags(List.of("lumberjack")));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void allowedTagsAllowPlayerWithTag(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getPlayer().setAllowedTags(List.of("lumberjack")));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		player.addTag("lumberjack");
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void allowedTagsBlankIgnored(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getPlayer().setAllowedTags(List.of(" ", "")));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	@GameTestCase
	public static void toggleCommandDisablesChopping(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		runCommand(player, "fallingtree toggle");
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void toggleCommandTwiceReenablesChopping(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		runCommand(player, "fallingtree toggle");
		runCommand(player, "fallingtree toggle");
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	private static void runCommand(@NonNull ServerPlayer player, @NonNull String command){
		player.level().getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), command);
	}

	@GameTestCase
	public static void requireEnchantmentBlocksWithoutEnchantment(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getEnchantment().setRequireEnchantment(true));
		placeDefaultTree(helper);
		var player = createLumberjack(helper);
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void requireEnchantmentBlocksOtherEnchantment(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getEnchantment().setRequireEnchantment(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, enchant(helper, new ItemStack(Items.DIAMOND_AXE), Enchantments.SHARPNESS));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertOnlyBottomLogBroken(helper));
	}

	@GameTestCase
	public static void requireEnchantmentAllowsChopperEnchantment(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod, config -> config.getEnchantment().setRequireEnchantment(true));
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, enchant(helper, new ItemStack(Items.DIAMOND_AXE), Enchantments.EFFICIENCY));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> assertTrunkCut(helper));
	}

	/**
	 * An enchantment in a {@code #fallingtree:chopper_<mode>} tag overrides the configured break mode (here shift down: only the top log is removed).
	 */
	@GameTestCase
	public static void chopperEnchantmentOverridesBreakMode(@NonNull GameTestHelper helper, @NonNull FallingTreeCommon<?> mod){
		TestConfiguration.reset(mod);
		placeDefaultTree(helper);
		var player = createPlayer(helper, GameType.SURVIVAL, enchant(helper, new ItemStack(Items.DIAMOND_AXE), Enchantments.UNBREAKING));
		playerBreak(helper, player, TREE_BASE);

		helper.succeedWhen(() -> {
			assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT - 1, true);
			helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE.above(TREE_HEIGHT - 1));
		});
	}
}
