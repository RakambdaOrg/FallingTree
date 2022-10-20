package fr.raksrinana.fallingtree.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import java.util.stream.Stream;
import static net.minecraft.commands.arguments.EntityAnchorArgument.Anchor.EYES;

@GameTestHolder(FallingTree.MOD_ID)
public class FallingTreeTest{
	
	@BeforeBatch(batch = "instantaneous")
	public static void before(ServerLevel level){
	
	}
	
	@GameTest
	public static void treeIsCutWhenHit(GameTestHelper helper){
		var expectedDestroyedPos = BlockPos.betweenClosedStream(0, 0, 0, 5, 7, 5);
		
		var hitPos = new BlockPos(3, 2, 3);
		var standingPos = helper.relativePos(new BlockPos(1, 1, 3));
		
		var player = helper.makeMockPlayer();
		helper.startSequence()
				.thenExecute(() -> hitTreeWithAxe(player, standingPos, hitPos))
				.thenIdle(100)
				.thenExecute(() -> checkDestroyed(helper, expectedDestroyedPos))
				.thenSucceed();
	}
	
	private static void checkDestroyed(GameTestHelper helper, Stream<BlockPos> expectedDestroyedPos){
		expectedDestroyedPos.forEach(bp -> helper.assertBlockNotPresent(Blocks.OAK_LOG, helper.relativePos(bp)));
	}
	
	private static void hitTreeWithAxe(Player player, BlockPos standingPos, BlockPos hitPos){
		player.moveTo(standingPos, 0, 0);
		
		var lookAtPos = new Vec3(hitPos.getX(), hitPos.getY(), hitPos.getZ());
		player.lookAt(EYES, lookAtPos.add(0.5f, 0.5f, 0.5f));
		
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_AXE));
		//player.startUsingItem(InteractionHand.MAIN_HAND);
	}
}
