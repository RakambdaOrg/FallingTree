package fr.rakambda.fallingtree.gametest;

import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public final class GameTestUtils{
	/**
	 * Size of the {@code fallingtree_gametest:empty} structure.
	 */
	public static final int STRUCTURE_SIZE = 8;
	/**
	 * Default position of the bottom log of test trees.
	 */
	public static final BlockPos TREE_BASE = new BlockPos(3, 1, 3);
	/**
	 * Default height of test trees.
	 */
	public static final int TREE_HEIGHT = 5;

	private static final BlockPos STRUCTURE_CENTER = new BlockPos(STRUCTURE_SIZE / 2, STRUCTURE_SIZE / 2, STRUCTURE_SIZE / 2);

	private GameTestUtils(){
	}

	/**
	 * Creates a player that is connected to the server, like {@link GameTestHelper#makeMockServerPlayerInLevel()}, but with a configurable game mode (the vanilla one is
	 * always creative, which the mod ignores by default).
	 * <p>
	 * Having a connection matters: the mod sends chat/action bar messages and block updates to the player.
	 */
	@NonNull
	public static ServerPlayer createPlayer(@NonNull GameTestHelper helper, @NonNull GameType gameType, @NonNull ItemStack mainHand){
		var level = helper.getLevel();
		var server = level.getServer();
		var cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "ft-test-player"), false);
		var player = new ServerPlayer(server, level, cookie.gameProfile(), cookie.clientInformation());
		var connection = new Connection(PacketFlow.SERVERBOUND);
		new EmbeddedChannel(connection);
		server.getPlayerList().placeNewPlayer(connection, player, cookie);

		player.setGameMode(gameType);
		player.setPos(helper.absoluteVec(new Vec3(0.5, 1, 0.5)));
		player.setItemInHand(InteractionHand.MAIN_HAND, mainHand);
		return player;
	}

	/**
	 * A survival player holding a diamond axe.
	 */
	@NonNull
	public static ServerPlayer createLumberjack(@NonNull GameTestHelper helper){
		return createPlayer(helper, GameType.SURVIVAL, new ItemStack(Items.DIAMOND_AXE));
	}

	/**
	 * @return a diamond axe with the given remaining durability.
	 */
	@NonNull
	public static ItemStack axeWithDurability(int remainingDurability){
		var axe = new ItemStack(Items.DIAMOND_AXE);
		axe.setDamageValue(axe.getMaxDamage() - remainingDurability);
		return axe;
	}

	@SafeVarargs
	@NonNull
	public static ItemStack enchant(@NonNull GameTestHelper helper, @NonNull ItemStack stack, @NonNull ResourceKey<Enchantment>... enchantments){
		var registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		for(var enchantment : enchantments){
			stack.enchant(registry.getOrThrow(enchantment), 1);
		}
		return stack;
	}

	public static void setCrouching(@NonNull ServerPlayer player){
		player.setShiftKeyDown(true);
		player.setPose(Pose.CROUCHING);
	}

	/**
	 * Breaks a block the same way a survival player would, so loader break events (and therefore the mod) are triggered.
	 */
	public static void playerBreak(@NonNull GameTestHelper helper, @NonNull ServerPlayer player, @NonNull BlockPos relativePos){
		player.gameMode.destroyBlock(helper.absolutePos(relativePos));
	}

	public static void placeFloor(@NonNull GameTestHelper helper){
		for(int x = 0; x < STRUCTURE_SIZE; x++){
			for(int z = 0; z < STRUCTURE_SIZE; z++){
				helper.setBlock(x, 0, z, Blocks.DIRT);
			}
		}
	}

	/**
	 * Places a straight column of logs.
	 *
	 * @return the position of the top log.
	 */
	@NonNull
	public static BlockPos placeTrunk(@NonNull GameTestHelper helper, @NonNull BlockPos base, int height, @NonNull Block log){
		for(int i = 0; i < height; i++){
			helper.setBlock(base.above(i), log);
		}
		return base.above(height - 1);
	}

	/**
	 * Places leaves on the 4 sides of and above a log.
	 */
	public static void placeLeafCap(@NonNull GameTestHelper helper, @NonNull BlockPos log, @NonNull Block leaves, boolean persistent){
		for(var pos : leafCapPositions(log)){
			helper.setBlock(pos, leafState(leaves, persistent));
		}
	}

	@NonNull
	public static BlockPos[] leafCapPositions(@NonNull BlockPos log){
		return new BlockPos[]{log.north(), log.south(), log.east(), log.west(), log.above()};
	}

	@NonNull
	public static BlockState leafState(@NonNull Block leaves, boolean persistent){
		var state = leaves.defaultBlockState();
		if(state.hasProperty(LeavesBlock.PERSISTENT)){
			state = state.setValue(LeavesBlock.PERSISTENT, persistent);
		}
		if(state.hasProperty(LeavesBlock.DISTANCE)){
			state = state.setValue(LeavesBlock.DISTANCE, 1);
		}
		return state;
	}

	/**
	 * The default test tree: a dirt floor and a {@link #TREE_HEIGHT} oak trunk at {@link #TREE_BASE} with a persistent leaf cap.
	 *
	 * @return the position of the top log.
	 */
	@NonNull
	public static BlockPos placeDefaultTree(@NonNull GameTestHelper helper){
		placeFloor(helper);
		var top = placeTrunk(helper, TREE_BASE, TREE_HEIGHT, Blocks.OAK_LOG);
		placeLeafCap(helper, top, Blocks.OAK_LEAVES, true);
		return top;
	}

	public static void assertColumn(@NonNull GameTestHelper helper, @NonNull Block block, @NonNull BlockPos from, int count, boolean present){
		for(int i = 0; i < count; i++){
			if(present){
				helper.assertBlockPresent(block, from.above(i));
			}
			else{
				helper.assertBlockNotPresent(block, from.above(i));
			}
		}
	}

	/**
	 * Asserts that the whole default trunk was cut.
	 */
	public static void assertTrunkCut(@NonNull GameTestHelper helper){
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, false);
	}

	/**
	 * Asserts that only the bottom log of the default trunk was broken, as in vanilla.
	 */
	public static void assertOnlyBottomLogBroken(@NonNull GameTestHelper helper){
		helper.assertBlockNotPresent(Blocks.OAK_LOG, TREE_BASE);
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE.above(), TREE_HEIGHT - 1, true);
	}

	/**
	 * Asserts that the default trunk is untouched.
	 */
	public static void assertTrunkIntact(@NonNull GameTestHelper helper){
		assertColumn(helper, Blocks.OAK_LOG, TREE_BASE, TREE_HEIGHT, true);
	}

	public static void assertDamage(@NonNull GameTestHelper helper, @NonNull ServerPlayer player, int expected){
		helper.assertValueEqual(player.getMainHandItem().getDamageValue(), expected, "tool damage");
	}

	public static void assertDropped(@NonNull GameTestHelper helper, @NonNull Item item, int expected){
		helper.assertValueEqual(countDroppedItems(helper, item), expected, "dropped " + item);
	}

	/**
	 * Counts dropped items in the whole test structure (summing stack sizes, as item entities can merge).
	 */
	public static int countDroppedItems(@NonNull GameTestHelper helper, @NonNull Item item){
		return countDroppedItems(helper, item, STRUCTURE_CENTER, STRUCTURE_SIZE);
	}

	/**
	 * Counts dropped items around a position (summing stack sizes, as item entities can merge).
	 */
	public static int countDroppedItems(@NonNull GameTestHelper helper, @NonNull Item item, @NonNull BlockPos relativePos, double radius){
		return helper.getEntities(EntityTypes.ITEM, relativePos, radius).stream()
				.map(ItemEntity::getItem)
				.filter(stack -> stack.is(item))
				.mapToInt(ItemStack::getCount)
				.sum();
	}

	/**
	 * Counts blocks in the whole test structure.
	 */
	public static int countBlocks(@NonNull GameTestHelper helper, @NonNull Block block){
		var count = 0;
		for(int x = 0; x < STRUCTURE_SIZE; x++){
			for(int y = 0; y < STRUCTURE_SIZE; y++){
				for(int z = 0; z < STRUCTURE_SIZE; z++){
					if(helper.getBlockState(new BlockPos(x, y, z)).is(block)){
						count++;
					}
				}
			}
		}
		return count;
	}

	public static void assertBlockCount(@NonNull GameTestHelper helper, @NonNull Block block, int expected){
		helper.assertValueEqual(countBlocks(helper, block), expected, "number of " + block);
	}

	public static void assertNoFallingBlocks(@NonNull GameTestHelper helper){
		helper.assertEntityNotPresent(EntityTypes.FALLING_BLOCK);
	}
}
