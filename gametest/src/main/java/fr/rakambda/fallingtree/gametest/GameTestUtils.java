package fr.rakambda.fallingtree.gametest;

import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public final class GameTestUtils{
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

	public static void placeFloor(@NonNull GameTestHelper helper, int size){
		for(int x = 0; x < size; x++){
			for(int z = 0; z < size; z++){
				helper.setBlock(x, 0, z, Blocks.DIRT);
			}
		}
	}

	/**
	 * Places a straight trunk starting at {@code base} with a small persistent leaf canopy around and above its top.
	 *
	 * @return the position of the top log.
	 */
	@NonNull
	public static BlockPos placeSimpleTree(@NonNull GameTestHelper helper, @NonNull BlockPos base, int height, @NonNull Block log, @NonNull Block leaves){
		for(int i = 0; i < height; i++){
			helper.setBlock(base.above(i), log);
		}

		var top = base.above(height - 1);
		var leavesState = leaves.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
		for(var pos : new BlockPos[]{top.north(), top.south(), top.east(), top.west(), top.above()}){
			helper.setBlock(pos, leavesState);
		}
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
	 * Counts dropped items (summing stack sizes, as item entities can merge) around a position.
	 */
	public static int countDroppedItems(@NonNull GameTestHelper helper, @NonNull Item item, @NonNull BlockPos relativePos, double radius){
		return helper.getEntities(EntityTypes.ITEM, relativePos, radius).stream()
				.map(ItemEntity::getItem)
				.filter(stack -> stack.is(item))
				.mapToInt(ItemStack::getCount)
				.sum();
	}
}
