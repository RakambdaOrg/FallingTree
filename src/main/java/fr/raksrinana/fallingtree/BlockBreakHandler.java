package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.tree.Tree;
import fr.raksrinana.fallingtree.tree.TreeHandler;
import fr.raksrinana.fallingtree.utils.FallingTreeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBreakHandler implements net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.Before{
	@Override
	public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity){
		if(FallingTree.config.getTreesConfiguration().isTreeBreaking() && !world.isClient()){
			if(FallingTreeUtils.isPlayerInRightState(player)){
				return TreeHandler.getTree(world, blockPos).map(tree -> {
					switch(FallingTree.config.getTreesConfiguration().getBreakMode()){
						case INSTANTANEOUS:
							return breakInstant(player, tree);
						case SHIFT_DOWN:
							return breakShiftDown(player, tree);
						default:
							return true;
					}
				}).orElse(true);
			}
		}
		return true;
	}
	
	private static boolean breakInstant(PlayerEntity player, Tree tree){
		if(FallingTree.config.getTreesConfiguration().getMaxSize() < tree.getLogCount()){
			player.sendSystemMessage(new TranslatableText("chat.falling_tree.tree_too_big", tree.getLogCount(), FallingTree.config.getTreesConfiguration().getMaxSize()), Util.NIL_UUID);
			return true;
		}
		return TreeHandler.destroyInstant(tree, player, player.getMainHandStack());
	}
	
	private static boolean breakShiftDown(PlayerEntity player, Tree tree){
		return TreeHandler.destroyShift(tree, player, player.getMainHandStack());
	}
}
